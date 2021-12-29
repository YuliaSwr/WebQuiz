package app.web.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletedQuiz {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonProperty("id")
    private int quizId;

    private LocalDateTime completedAt;

    @JsonIgnore
    private int userId;

    public CompletedQuiz(int quizId, LocalDateTime completedAt, int userId) {
        this.quizId = quizId;
        this.completedAt = completedAt;
        this.userId = userId;
    }
}
