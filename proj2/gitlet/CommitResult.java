package gitlet;

public class CommitResult {
    private String result;
    private String message;

    public CommitResult(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
