package com.khu.gitbox.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khu.gitbox.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class HttpResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
            throws IOException {
        String responseBody = objectMapper.writeValueAsString(ApiResponse.ok(body));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }

    public static void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
