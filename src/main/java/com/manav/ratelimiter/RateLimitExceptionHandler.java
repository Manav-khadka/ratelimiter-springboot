package com.manav.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RateLimitExceptionHandler.class);

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiErroMessage> handleInvalidFieldsInValidJson(final RateLimitException rateLimitException, HttpServletRequest request) {
        final ApiErroMessage apiErroMessage = rateLimitException.toApiErroMessage(request.getRequestURI());
        logIncommingCallException(rateLimitException, apiErroMessage);
        return new ResponseEntity<>(apiErroMessage, HttpStatus.TOO_MANY_REQUESTS);
    }

    private static void logIncommingCallException(final RateLimitException rateLimitException, final ApiErroMessage apiErroMessage) {
        LOG.error(String.format("%s: %s", rateLimitException.getClass().getSimpleName(), apiErroMessage.toString()));
    }
}
