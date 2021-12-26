package app.web.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class WebQuizController {

    private QuizService quizService;

    @Autowired
    public WebQuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/quizzes/{id}/solve")
    public engine.Response postAnswer(@PathVariable int id, @RequestBody Map<String, List<Integer>> answer) {

        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Question question = quizService.getQuestionById(id);
        List<Integer> rightAnswers = question.getAnswer();
        List<Integer> userAnswers = answer.get("answer");

        if ((rightAnswers.isEmpty() && !userAnswers.isEmpty()) || (!rightAnswers.isEmpty() && userAnswers.isEmpty())) {
            return new engine.Response(false);
        }

        if (!rightAnswers.isEmpty() && !userAnswers.isEmpty()) {
            boolean isRightAnswer = true;
            for (Integer rightAnswer : rightAnswers) {
                if (!userAnswers.contains(rightAnswer)) {
                    isRightAnswer = false;
                    break;
                }
            }
            if (rightAnswers.size() != userAnswers.size() || !isRightAnswer) {
                return new engine.Response(false);
            }
        }
        return new engine.Response(true);
    }

    @PostMapping("/quizzes")
    public Question postQuestion(@Valid @RequestBody Question question) {
        quizService.saveQuestion(question);
        return quizService.getQuestionById(question.getId());
    }

    @GetMapping("/quizzes/{id}")
    public Question getQuestionById(@PathVariable int id) {
        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return quizService.getQuestionById(id);
    }

    @GetMapping("/quizzes")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(quizService.getAllQuestions(), HttpStatus.OK);
    }
}
