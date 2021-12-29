package app.web.quiz.service;

import app.web.quiz.model.Question;
import app.web.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private final QuizRepository quizRepo;

    @Autowired
    public QuizService(QuizRepository quizRepo) {
        this.quizRepo = quizRepo;
    }

    public void save(Question question) {
        quizRepo.save(question);
    }

    public Question getById(int id) {
        return quizRepo.findById(id).get();
    }

    public Page<Question> getAll(int pageNum) {
        Pageable paging = PageRequest.of(pageNum, 10, Sort.by("id").ascending());
        return quizRepo.findAll(paging);
    }

    public boolean existById(int id) {
        return quizRepo.existsById(id);
    }

    public void delete(Question quiz) {
        quizRepo.deleteById((quiz.getId()));
    }
}
