package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.*;

@RestController
@RequestMapping("/api")
public class WebQuizController {

    private final Map<Integer, Question> quizBase = new TreeMap<>();

    @PostMapping("/quizzes/{id}/solve")
    public Response postAnswer(@PathVariable int id, @RequestBody Map<String, List<Integer>> answer) {

        if (!quizBase.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Question question = quizBase.get(id);
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

    @PostMapping("/quizzes")
    public Question postQuestion(@Valid @RequestBody Question question) {
        quizBase.put(question.getId(), question);

        return quizBase.get(question.getId());
    }

    @GetMapping("/quizzes/{id}")
    public Question getQuizById(@PathVariable int id) {

        if (!quizBase.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return quizBase.get(id);
    }

    @GetMapping("/quizzes")
    public Collection<Question> getAllQuiz() {
        return quizBase.values();
    }
}
