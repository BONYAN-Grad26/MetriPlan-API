package com.abdelaziz26.metriplate.responses.Result_;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T, E extends Error> {

    private T data;
    private E error;
    private boolean success;

    private Result(T data, E error, boolean success ) {
        this.data = data;
        this.error = error;
        this.success = success;
    }

    public static <T, E extends Error> Result<T, E> CreateSuccessResult(T data)
    {
        return new Result<T, E>(data,  (E) Errors.None(), true);
    }

    public static <T, E extends Error> Result<T, E> CreateErrorResult(E error)
    {
        return new Result<>(null, error, false);
    }
}
