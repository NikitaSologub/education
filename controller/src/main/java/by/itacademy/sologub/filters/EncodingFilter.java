package by.itacademy.sologub.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebFilter(ALL_URL)
public class EncodingFilter implements Filter {
    private String encoding;

    @Override
    public void init(FilterConfig config) {
        encoding = config.getInitParameter(REQUEST_ENCODING);
        if (encoding == null) encoding = UTF_8;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (null == req.getCharacterEncoding()) {
            req.setCharacterEncoding(encoding);
        }
        res.setContentType(CONTENT_TYPE);
        res.setCharacterEncoding(UTF_8);
        chain.doFilter(req, res);
    }
}