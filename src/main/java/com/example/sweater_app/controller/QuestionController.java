package com.example.sweater_app.controller;

import com.example.sweater_app.constants.ModelName;
import com.example.sweater_app.constants.ViewName;
import com.example.sweater_app.dto.QuestionDTO;
import com.example.sweater_app.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/q")
public class QuestionController {

    private QuestionService questionService;


    @GetMapping
    public ModelAndView getAllQuestions(){
        List<QuestionDTO> questionDTO = questionService.getAllQuestionsDTO();
        return new ModelAndView(ViewName.MAIN_PAGE, ModelName.QUESTION_DTO, questionDTO);
    }

    @PostMapping("/add")
    public ModelAndView addQuestion(
            @RequestParam(name = "title") String questionTitle,
            @RequestParam(name = "description") String questionDescription,
            @RequestParam(name = "tag") String questionTag
    ) {
        questionService.addQuestion(questionTitle,questionDescription,questionTag);
        return new ModelAndView("redirect:/q");
    }

    @PostMapping("/filter")
    public ModelAndView filter(@RequestParam(name = "tag") String tag){
       Iterable<QuestionDTO> filteredQuestions = questionService.findQuestionsByTag(tag);
        return new ModelAndView(ViewName.MAIN_PAGE, ModelName.QUESTION_DTO, filteredQuestions);
    }

}
