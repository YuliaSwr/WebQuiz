package app.web.quiz.service;

import app.web.quiz.model.CompletedQuiz;
import app.web.quiz.model.User;
import app.web.quiz.repository.CompletedQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CompletedQuizService {

    @Autowired
    CompletedQuizRepository completedQuizRepository;

    @Autowired
    UserService userService;

    public Page getCompleted(int pageNum, int userId) {
        User user = userService.findById(userId);
        Pageable paging = PageRequest.of(pageNum, 10, Sort.by("completedAt").descending());
        return completedQuizRepository.findAllByUserId(userId, paging);
    }

    public void save(CompletedQuiz solvedQuiz) {
        completedQuizRepository.save(solvedQuiz);
    }
}
