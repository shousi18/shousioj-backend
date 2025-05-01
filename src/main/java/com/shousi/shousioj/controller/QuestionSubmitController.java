package com.shousi.shousioj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shousi.shousioj.common.BaseResponse;
import com.shousi.shousioj.common.ErrorCode;
import com.shousi.shousioj.common.ResultUtils;
import com.shousi.shousioj.exception.BusinessException;
import com.shousi.shousioj.exception.ThrowUtils;
import com.shousi.shousioj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.shousi.shousioj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.shousi.shousioj.model.entity.QuestionSubmit;
import com.shousi.shousioj.model.entity.User;
import com.shousi.shousioj.model.vo.QuestionSubmitVO;
import com.shousi.shousioj.service.QuestionSubmitService;
import com.shousi.shousioj.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/question_submit")
public class QuestionSubmitController {

    @Resource
    private UserService userService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @PostMapping("/doSubmit")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        User loginUser = userService.getLoginUser(request);
        Long questionId = questionSubmitAddRequest.getQuestionId();
        questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionId);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(questionSubmitQueryRequest == null, ErrorCode.PARAMS_ERROR);
        int current = questionSubmitQueryRequest.getCurrent();
        int size = questionSubmitQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser(request);
        // 从数据库中查询原始分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        // 信息脱敏
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }
}
