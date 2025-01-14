package com.manav.ratelimiter;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLImitAspect {
    public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!\"";
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Value("${API_RATE_LIMIT:#2}")
    private int rateLimit;

    @Value("${API_RATE_LIMIT_DURATION:#1000}")
    private int rateLimitDuration;


    @Before("@annotation(com.manav.ratelimiter.WithRateLimitProtection)")
    public void rateLimit() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String ip = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(ip, new ArrayList<>());
        requestCounts.get(ip).add(currentTime);
        cleanUpRequestCounts( currentTime);
        if (requestCounts.get(ip).size()> rateLimit){
             throw new RateLimitException(String.format(ERROR_MESSAGE,requestAttributes.getRequest().getRequestURI(),ip,rateLimitDuration));
        }
        
    }

    private void cleanUpRequestCounts(final long currentTime) {
        requestCounts.values().forEach(e -> e.removeIf(t -> timeIsTooOld(currentTime,t)));
    }

    private boolean timeIsTooOld(final long currentTime,final Long t) {
        return currentTime - t > rateLimitDuration;
    }
}
