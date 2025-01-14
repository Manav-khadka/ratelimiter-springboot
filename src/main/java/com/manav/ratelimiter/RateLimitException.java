package com.manav.ratelimiter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException extends RuntimeException{
    public RateLimitException(String message) {
        super(message);
    }
    public ApiErroMessage toApiErroMessage(final String path){
        return new ApiErroMessage(HttpStatus.TOO_MANY_REQUESTS.value(),HttpStatus.TOO_MANY_REQUESTS.name(),this.getMessage(),path);
    }
}
