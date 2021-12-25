package engine;

import lombok.*;

@Getter
@Setter
public class Response {
    private boolean success;
    private String feedback;

    public Response(boolean success) {
        this.success = success;
        this.feedback = success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";
    }
}
