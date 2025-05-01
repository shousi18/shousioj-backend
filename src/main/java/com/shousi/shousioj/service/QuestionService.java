package com.shousi.shousioj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shousi.shousioj.model.dto.question.QuestionQueryRequest;
import com.shousi.shousioj.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.shousioj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86172
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-05-01 19:41:09
*/
public interface QuestionService extends IService<Question> {

    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
