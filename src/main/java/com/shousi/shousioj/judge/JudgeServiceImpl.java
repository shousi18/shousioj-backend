package com.shousi.shousioj.judge;

import cn.hutool.json.JSONUtil;
import com.shousi.shousioj.common.ErrorCode;
import com.shousi.shousioj.exception.BusinessException;
import com.shousi.shousioj.exception.ThrowUtils;
import com.shousi.shousioj.judge.codesandox.CodeSandbox;
import com.shousi.shousioj.judge.codesandox.CodeSandboxFactory;
import com.shousi.shousioj.judge.codesandox.CodeSandboxProxy;
import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeRequest;
import com.shousi.shousioj.judge.codesandox.model.ExecuteCodeResponse;
import com.shousi.shousioj.judge.strategy.JudgeContext;
import com.shousi.shousioj.model.dto.question.JudgeCase;
import com.shousi.shousioj.judge.codesandox.model.JudgeInfo;
import com.shousi.shousioj.model.entity.Question;
import com.shousi.shousioj.model.entity.QuestionSubmit;
import com.shousi.shousioj.model.enums.QuestionSubmitStatusEnum;
import com.shousi.shousioj.service.QuestionService;
import com.shousi.shousioj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1.根据id，获取到对应题目的信息和一些提交的信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        ThrowUtils.throwIf(questionSubmit == null, ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        // 2.如果题目状态不为等待中，就不用重复执行了
        Integer status = questionSubmit.getStatus();
        if (status != null && !status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3.设置题目为判题中
        QuestionSubmit updateQuestionSubmit = new QuestionSubmit();
        updateQuestionSubmit.setId(questionSubmitId);
        updateQuestionSubmit.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(updateQuestionSubmit);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "题目状态更新错误");
        // 4.调用沙箱，获取执行结果
        // 创建对应的实例
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        // 获取编程语言和代码
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .inputList(inputList)
                .language(language)
                .code(code)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5.对比沙箱输出和数据库输出，设置状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6.修改数据库数据和判题结果
        updateQuestionSubmit = new QuestionSubmit();
        updateQuestionSubmit.setId(questionSubmitId);
        updateQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        updateQuestionSubmit.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        update = questionSubmitService.updateById(updateQuestionSubmit);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "题目状态更新错误");
        return questionSubmitService.getById(questionId);
    }
}
