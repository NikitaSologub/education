package by.itacademy.sologub.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.ALL_URL;
import static by.itacademy.sologub.constants.Constant.UTF_8;

@WebFilter(ALL_URL)
public class EncodingFilter implements Filter {
    private String encoding;

    @Override
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) encoding = UTF_8;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (null == req.getCharacterEncoding()) {
            req.setCharacterEncoding(encoding);
        }
        res.setContentType("text/html;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        chain.doFilter(req, res);
    }
}