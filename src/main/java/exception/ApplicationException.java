package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ApplicationException {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleExceptionNotFound(Throwable throwable) {
        ModelAndView modelAndView = new ModelAndView("not-found");
        modelAndView.addObject("message", throwable.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleExceptionServerError(Exception exception){
        ModelAndView modelAndView = new ModelAndView("server-error");
        modelAndView.addObject("message",exception.getMessage());
        return modelAndView;
    }
}
