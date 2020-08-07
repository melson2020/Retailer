package com.melson.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseResource {

    private static final Logger logger = LoggerFactory.getLogger(BaseResource.class);

    @ExceptionHandler()
    @ResponseStatus(reason = "系统错误", value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Exception e) {
        logger.error("系统错误", e);
        return e.getMessage();
    }

    public Result GenerateResult(ResultType type, String message) {
        Result result = new Result();
        switch (type) {
            case AccessNeeded:
                result.setResultStatus(-1);
                result.setMessage("AccessNeeded");
                break;
            case AccessDenied:
                result.setResultStatus(-1);
                result.setMessage("AccessDenied");
                break;
            case ExceptionCatched:
                result.setResultStatus(-1);
                result.setMessage(message);
                break;
            default:
                break;
        }
        return result;
    }

    public Result GenerateResult(ResultType type) {
        Result result = new Result();
        switch (type) {
            case AccessNeeded:
                result.setResultStatus(-1);
                result.setMessage("AccessNeeded");
                break;
            case AccessDenied:
                result.setResultStatus(-1);
                result.setMessage("AccessDenied");
                break;
            case ExceptionCatched:
                result.setResultStatus(-1);
                result.setMessage("Exception Catched");
                break;
            case ParametersNeeded:
                result.setResultStatus(-1);
                result.setMessage("Params Needed");
            default:
                break;
        }
        return result;
    }

}
