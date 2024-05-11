package com.khu.gitbox.auth.handler;

import com.khu.gitbox.common.response.ErrorResponse;
import com.khu.gitbox.util.HttpResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증된 사용자가 필요한 권한없이 접근하려고 할 때 발생하는 예외 처리
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Access Denied: ", accessDeniedException);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.toString(), "권한이 없습니다.");
        HttpResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN, errorResponse);
    }

}
