package by.itacademy.sologub.filters;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static by.itacademy.sologub.constants.Constant.ALL_URL;

@WebFilter(ALL_URL)
@Slf4j
public class LogRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain ch) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        String path = httpReq.getServletPath();
        String address = httpReq.getRemoteAddr();

        log.info("Input request Date:{} Servlet path:{} Call from:{}", LocalDateTime.now(), path, address);

        ch.doFilter(req, res);

        HttpServletResponse httpResp = (HttpServletResponse) res;
        int status = httpResp.getStatus();
        String contentType = httpResp.getContentType();

        log.info("Output response Date:{} http status:{} Content type:{}", LocalDateTime.now(), status, contentType);
    }
}