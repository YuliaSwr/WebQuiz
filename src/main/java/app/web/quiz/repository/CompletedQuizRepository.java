package app.web.quiz.repository;

import app.web.quiz.model.CompletedQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Integer> {
    Page findAllByUserId(int id, Pageable pageable);
}
