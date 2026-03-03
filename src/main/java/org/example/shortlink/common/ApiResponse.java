package org.example.shortlink.common;

public class ApiResponse<T>
{
    private int code;
    private T data;
    private String message;

    public ApiResponse(int code, T data, String message)
    {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data)
    {
        return new ApiResponse<>(200, data, "success");
    }

    public static <T> ApiResponse<T> success(T data, String message)
    {
        return new ApiResponse<>(200, data, message);
    }

    public static <T> ApiResponse<T> error(String message)
    {
        return new ApiResponse<>(400, null, message);
    }

    public int getCode() { return code; }
    public T getData() { return data; }
    public String getMessage() { return message; }
}
