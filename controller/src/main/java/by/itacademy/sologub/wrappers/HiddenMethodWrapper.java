package by.itacademy.sologub.wrappers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Locale;

public class HiddenMethodWrapper extends HttpServletRequestWrapper {
    private final String originalMethod;
    private final String hiddenMethod;
    private boolean isHidden = true;

    public HiddenMethodWrapper(HttpServletRequest request, String hiddenMethod) {
        super(request);
        this.originalMethod = request.getMethod().toUpperCase(Locale.ROOT);
        this.hiddenMethod = hiddenMethod.toUpperCase(Locale.ROOT);
    }

    @Override
    public String getMethod() {
        if(isHidden){
            return hiddenMethod;
        }
        return originalMethod;
    }

    public void changeMethodFromHiddenToOriginal(){
        isHidden = !isHidden;
    }
}