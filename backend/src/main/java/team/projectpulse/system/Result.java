package team.projectpulse.system;

public record Result(boolean flag, int code, String message, Object data) {

    public static Result success(String message, Object data) {
        return new Result(true, StatusCode.SUCCESS, message, data);
    }

    public static Result failure(int code, String message) {
        return new Result(false, code, message, null);
    }
}
