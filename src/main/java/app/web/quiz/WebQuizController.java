package app.web.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class WebQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @PostMapping("/quizzes/{id}/solve")
    public Response postAnswer(@PathVariable int id, @RequestBody Map<String, List<Integer>> answer) {

        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Question question = quizService.getById(id);
        List<Integer> rightAnswers = question.getAnswer();
        List<Integer> userAnswers = answer.get("answer");

        if ((rightAnswers.isEmpty() && !userAnswers.isEmpty()) || (!rightAnswers.isEmpty() && userAnswers.isEmpty())) {
            return new Response(false);
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
                return new Response(false);
            }
        }
        return new Response(true);
    }

    @PostMapping("quizzes")
    public Question createQuiz(@Valid @RequestBody Question question,
                               @AuthenticationPrincipal User user) {
        question.setUser(user);
        quizService.save(question);
        return quizService.getById(question.getId());
    }

    @GetMapping("/quizzes/{id}")
    public Question getQuestionById(@PathVariable int id) {
        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return quizService.getById(id);
    }

    @GetMapping("quizzes")
    public List<Question> getQuizzes() {
        return quizService.getAll();
    }

    @DeleteMapping("quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable int id,
                                        @AuthenticationPrincipal User user) {
        if (!quizService.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Question question = quizService.getById(id);

        if (question.getUser().getId() == user.getId()) {
            quizService.delete(question);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
}
