package com.shousi.shousioj.judge;

import com.shousi.shousioj.judge.strategy.DefaultJudgeStrategy;
import com.shousi.shousioj.judge.strategy.JavaLanguageJudgeStrategy;
import com.shousi.shousioj.judge.strategy.JudgeContext;
import com.shousi.shousioj.judge.strategy.JudgeStrategy;
import com.shousi.shousioj.judge.codesandox.model.JudgeInfo;
import com.shousi.shousioj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判断管理
 */
@Service
public class JudgeManager {

    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
