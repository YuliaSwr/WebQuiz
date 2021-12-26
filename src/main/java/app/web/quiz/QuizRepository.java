package app.web.quiz;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends CrudRepository<Question, Integer> {

    @Override
    List<Question> findAll();
}
