package com.example.sweater_app.controller;

import com.example.sweater_app.constants.ViewName;
import com.example.sweater_app.domain.Question;
import com.example.sweater_app.dto.QuestionDTO;
import com.example.sweater_app.repository.QuestionRepository;
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
import java.util.Map;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/")
public class QuestionController {

    private  QuestionRepository questionRepository;
    private QuestionService questionService;


    @GetMapping("/")
    public ModelAndView getAllQuestions(){
        List<QuestionDTO> questionDTO = questionService.getAllQuestionsDTO();
        return new ModelAndView(ViewName.MAIN_PAGE, "questionDTO", questionDTO);
    }

    @PostMapping("/add")
    public String addQuestion(
            @RequestParam(name = "title") String questionTitle,
            @RequestParam(name = "description") String questionDescription,
            @RequestParam(name = "tag") String questionTag
    ) {
        questionService.addQuestion(questionTitle,questionDescription,questionTag);
        return "redirect:/";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam(name = "tag") String tag,
                         Map<String, Object> model){
       Iterable<Question> questions;
       if (tag != null && !tag.isEmpty()){
           questions = questionRepository.findQuestionByTag(tag);
       } else {
           questions = questionRepository.findAll();
       }
        model.put("questions", questions);
        return "main";
    }

}
