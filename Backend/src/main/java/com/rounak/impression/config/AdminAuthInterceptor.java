package com.rounak.impression.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {


    private static final String HARDCODED_PASSWORD = "YourVentureSecretPassword123";
    private static final String ADMIN_PASSWORD_HEADER = "X-Admin-Password";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("POST".equalsIgnoreCase(request.getMethod()) ||
                "PUT".equalsIgnoreCase(request.getMethod()) ||
                "DELETE".equalsIgnoreCase(request.getMethod())) {

            String providedPassword = request.getHeader(ADMIN_PASSWORD_HEADER);

            // Check if the password header is missing or if the password doesn't match
            if (providedPassword == null || !providedPassword.equals(HARDCODED_PASSWORD)) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Incorrect or missing admin password.");
                return false;
            }
        }
        // For GET requests, or if the password check passes for other methods, continue the request
        return true;
    }

    // You can optionally implement postHandle and afterCompletion if you need logic
    // to run after the controller, but for auth, preHandle is sufficient.
}