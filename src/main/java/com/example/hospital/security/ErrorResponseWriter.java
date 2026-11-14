package com.example.hospital.security;

import com.example.hospital.dto.ErrorResponse;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ErrorResponseWriter {

    private final JsonMapper mapper;

    public void write(HttpServletResponse res, int status, String message, String path) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        String errorName = HttpStatus.resolve(status) != null
                ? HttpStatus.resolve(status).getReasonPhrase()
                : "Error";
        mapper.writeValue(res.getOutputStream(),
                new ErrorResponse(Instant.now(), status, errorName, message, path));
    }
}
