package app.web.quiz.repository;

import app.web.quiz.model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Question, Integer> {

}
