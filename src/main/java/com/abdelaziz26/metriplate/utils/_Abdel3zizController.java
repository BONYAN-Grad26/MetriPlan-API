package com.abdelaziz26.metriplate.utils;

import com.abdelaziz26.metriplate.responses.Result_.Error;
import com.abdelaziz26.metriplate.responses.Result_.Result;
import org.springframework.http.HttpStatus;

public abstract class _Abdel3zizController {
    protected HttpStatus resolveStatus(Result<?, Error> result) {
        return switch (result.getError().getType()){
            case FORBIDDEN_ERR -> HttpStatus.FORBIDDEN;
            case  UNAUTHORIZED_ERR -> HttpStatus.UNAUTHORIZED;
            case INTERNAL_SERVER_ERR, VALIDATION_ERR -> HttpStatus.INTERNAL_SERVER_ERROR;
            case BAD_REQUEST_ERR -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND_ERR ->  HttpStatus.NOT_FOUND;
            default -> HttpStatus.OK;
        };
    }
}
