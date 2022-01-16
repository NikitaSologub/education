package by.itacademy.sologub.interceptors;

import by.itacademy.sologub.wrappers.HiddenMethodWrapper;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static by.itacademy.sologub.constants.Constant.HIDDEN_METHOD_PARAMETER;

@Slf4j
public class HiddenMethodInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object h, ModelAndView mav) {
        String hiddenParameter = req.getParameter(HIDDEN_METHOD_PARAMETER);
        if (hiddenParameter != null) {
            log.debug("Скрытый параметр={}. Установим метод POST для обёртки", hiddenParameter);
            HiddenMethodWrapper wrapper = (HiddenMethodWrapper) req;
            wrapper.changeMethodFromHiddenToOriginal();
        } else {
            log.debug("Скрытый параметр=null. Не нужно ничего менять");
        }
    }
}