package app.web.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepo;

    @Autowired
    public QuizService(QuizRepository quizRepo) {
        this.quizRepo = quizRepo;
    }

    public void saveQuestion(Question question) {
        quizRepo.save(question);
    }

    public Question getQuestionById(int id) {
        return quizRepo.findById(id).get();
    }

    public List<Question> getAllQuestions() {
        return quizRepo.findAll();
    }

    public boolean existById(int id) {
        return quizRepo.existsById(id);
    }
}
