package com.shousi.shousioj.judge.strategy;

import com.shousi.shousioj.model.dto.question.JudgeCase;
import com.shousi.shousioj.judge.codesandox.model.JudgeInfo;
import com.shousi.shousioj.model.entity.Question;
import com.shousi.shousioj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
