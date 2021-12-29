package app.web.quiz.controller;

import app.web.quiz.model.*;
import app.web.quiz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WebQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private CompletedQuizService completedQuizService;


    @PostMapping("/quizzes/{id}/solve")
    public Response answer(@PathVariable int id,
                               @RequestBody Map<String, List<Integer>> answer,
                               @AuthenticationPrincipal User user) {

        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Integer> rightAnswers =  quizService.getById(id).getAnswer().stream().sorted().collect(Collectors.toList());
        List<Integer> userAnswers = answer.get("answer").stream().sorted().collect(Collectors.toList());

        if(rightAnswers.equals(userAnswers)) {
            completedQuizService.save(new CompletedQuiz(id, LocalDateTime.now(), user.getId()));
            return new Response(true);
        }

        return new Response(false);
    }

    @PostMapping("/quizzes")
    public Question createQuiz(@Valid @RequestBody Question question,
                               @AuthenticationPrincipal User user) {
        question.setUser(user);
        quizService.save(question);
        return quizService.getById(question.getId());
    }

    @GetMapping("/quizzes/{id}")
    public Question getQuizById(@PathVariable int id) {
        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return quizService.getById(id);
    }

    @GetMapping("/quizzes")
    public Page getAllQuizzes(@RequestParam(required = false, defaultValue = "0") Integer page) {
        return quizService.getAll(page);
    }

    @GetMapping("/quizzes/completed")
    public Page getAllCompletedQuizzes(@RequestParam(required = false, defaultValue = "0") Integer page,
                                    @AuthenticationPrincipal User user) {
        return completedQuizService.getCompleted(page, user.getId());
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable int id,
                                        @AuthenticationPrincipal User user) {
        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Question quiz = quizService.getById(id);
        if (quiz.getUser().getId() == user.getId()) {
            quizService.delete(quiz);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
}
