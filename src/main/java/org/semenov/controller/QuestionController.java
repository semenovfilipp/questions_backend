package org.semenov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.semenov.dto.QuestionDTO;
import org.semenov.model.Question;
import org.semenov.model.User;
import org.semenov.repository.QuestionRepository;
import org.semenov.service.QuestionService;
import org.semenov.utils.ControllerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        try {
            Page<QuestionDTO> page = questionService.questionList(pageable, filter, user);

            model.addAttribute("page", page);
            model.addAttribute("url", "/main");
            model.addAttribute("filter", filter);

            return "main";
        } catch (Exception e) {
            log.error("Error occurred while processing main page request: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Question question,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        try {
            question.setAuthor(user);

            if (bindingResult.hasErrors()) {
                Map<String, String> errorsMap = ControllerUtil.getErrors(bindingResult);
                model.mergeAttributes(errorsMap);
                model.addAttribute("question", question);
            } else {
                saveFile(question, file);
                model.addAttribute("question", null);
                questionRepository.save(question);
            }

            Iterable<Question> questions = questionRepository.findAll();
            model.addAttribute("questions", questions);

            return "main";
        } catch (Exception e) {
            log.error("Error occurred while adding question: {}", e.getMessage());
            throw e;
        }
    }

    private void saveFile(@Valid Question question, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            question.setFileName(resultFilename);
        }
    }

    @GetMapping("/user-questions/{author}")
    public String userQuestions(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            Model model,
            @RequestParam(required = false) Question question,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<QuestionDTO> page = questionService.questionListForUser(pageable, currentUser, author);

        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscribers().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("page", page);
        model.addAttribute("question", question);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("url", "/user-questions/" + author.getId());

        return "userQuestions";
    }

    @PostMapping("/user-questions/{user}")
    public String updateQuestion(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Question question,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (question.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                question.setText(text);
            }

            if (!StringUtils.isEmpty(tag)) {
                question.setTag(tag);
            }

            saveFile(question, file);

            questionRepository.save(question);
        }

        return "redirect:/user-questions/" + user;
    }

    @GetMapping("/questions/{question}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Question question,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> likes = question.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .forEach(redirectAttributes::addAttribute);

        return "redirect:" + components.getPath();
    }
}
