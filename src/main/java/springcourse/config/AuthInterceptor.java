package springcourse.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import springcourse.controllers.AuthController;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        boolean isAuth = session != null && Boolean.TRUE.equals(session.getAttribute(AuthController.ADMIN_AUTH));

        if (isAuth) return true;

        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}