package by.itacademy.sologub.config.auth_checker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UrlRedirectAfterAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        String targetUrl = determineTargetUrl(auth);
        if (res.isCommitted()) {
            log.info("response is already committed we can't redirect our request to url={}", targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(req, res, targetUrl);
    }

    private String determineTargetUrl(Authentication auth) {
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String url;
        if (isAdmin(roles)) {
            url = "/view/admin_front_page.jsp";
        } else if (isTeacher(roles)) {
            url = "/view/teacher_app/teacher_front_page.jsp";
        } else if (isStudent(roles)) {
            url = "/view/student_app/student_front_page.jsp";
        } else {
            url = "/Access_Denied";
        }
        log.info("our URL set like {} and we go to this jsp page", url);
        return url;
    }

    private boolean isAdmin(List<String> roles) {
        return roles.contains("ROLE_ADMIN") || roles.contains("ADMIN");
    }

    private boolean isTeacher(List<String> roles) {
        return roles.contains("ROLE_TEACHER") || roles.contains("TEACHER");
    }

    private boolean isStudent(List<String> roles) {
        return roles.contains("ROLE_STUDENT") || roles.contains("STUDENT");
    }
}