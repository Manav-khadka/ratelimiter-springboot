package com.manav.ratelimiter;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErroMessage {
    private final UUID id = UUID.randomUUID();
    private  final int status;
    private final String error;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
    private final String path;

    public ApiErroMessage(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
