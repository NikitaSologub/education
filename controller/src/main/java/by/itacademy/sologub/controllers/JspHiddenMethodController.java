package by.itacademy.sologub.controllers;

import by.itacademy.sologub.filters.wrappers.HiddenMethodWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import static by.itacademy.sologub.constants.Constant.HIDDEN_METHOD_PARAMETER;

@Slf4j
public abstract class JspHiddenMethodController {
    protected void resetMethod(HttpServletRequest req) {
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
