package com.abdelaziz26.metriplate.security;

import com.abdelaziz26.metriplate.utils.MagicValues;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter{

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit ->
                        limit.capacity(MagicValues.BUCKET_CAPACITY)
                                .refillGreedy(MagicValues.REFILL_RATE, Duration.ofSeconds(1)))
                .build();
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().contains("/actuator/health")){
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

        if(bucket.tryConsume(1)){
            filterChain.doFilter(request, response);
            return;
        }

        response.setContentType("application/json");
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write(MagicValues.RATE_LIMIT_EXCEEDED_MSG);
    }
}
