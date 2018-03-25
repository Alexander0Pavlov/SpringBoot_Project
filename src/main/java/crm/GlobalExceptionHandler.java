package src.main.java.crm;

import src.main.java.crm.exceptions.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;

// для передачи запрашиваемого url в сообщение об ошибке
import javax.servlet.http.HttpServletRequest;

// Throwable Throwables.getRootCause(Throwable ex)
import com.google.common.base.Throwables;

@ControllerAdvice
public class GlobalExceptionHandler {
    private boolean printFullStackTrace = true;

    public GlobalExceptionHandler() {
        System.out.println(getClass() + " started");
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND /*, reason = "Not Found"  --- убираем чтобы выкидывать json errorMessage*/)
    @ExceptionHandler(value = {NotFoundException.class, UsernameNotFoundException.class})
    @ResponseBody
    public ErrorMessage handleNotFoundException(HttpServletRequest req, Exception ex) {

        Throwable exRootCause = Throwables.getRootCause(ex);

        ErrorMessage errorMessage = new ErrorMessage(new Timestamp(System.currentTimeMillis()),
                ex.getClass().getName(),
                exRootCause.getStackTrace()[0].getClassName(),
                exRootCause.getStackTrace()[0].getMethodName(),
                req.getRequestURI(), 404, "NOT_FOUND"/*ex.getMessage()*/);

        if (printFullStackTrace)
            System.out.println(errorMessage + "\n" + Throwables.getStackTraceAsString(ex) + "\n");
        else System.out.println(errorMessage);

        return errorMessage;
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadRequestException.class, MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class, HttpMediaTypeNotSupportedException.class,
            MultipartException.class})
    @ResponseBody
    public ErrorMessage handleBadRequestException(HttpServletRequest req, Exception ex) {

        Throwable exRootCause = Throwables.getRootCause(ex);

        ErrorMessage errorMessage = new ErrorMessage(new Timestamp(System.currentTimeMillis()),
                ex.getClass().getName(),
                exRootCause.getStackTrace()[0].getClassName(),
                exRootCause.getStackTrace()[0].getMethodName(),
                req.getRequestURI(), 400, "BAD_REQUEST"/*ex.getMessage()*/);

        if (printFullStackTrace)
            System.out.println(errorMessage + "\n" + Throwables.getStackTraceAsString(ex) + "\n");
        else System.out.println(errorMessage);

        return errorMessage;
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ErrorMessage handleInternalServerErrorException(HttpServletRequest req, Exception ex) {

        Throwable exRootCause = Throwables.getRootCause(ex);

        ErrorMessage errorMessage = new ErrorMessage(new Timestamp(System.currentTimeMillis()),
                ex.getClass().getName(),
                exRootCause.getStackTrace()[0].getClassName(),
                exRootCause.getStackTrace()[0].getMethodName(),
                req.getRequestURI(), 500, "INTERNAL_SERVER_ERROR"/*ex.getMessage()*/);

        if (printFullStackTrace)
            System.out.println(errorMessage + "\n" + Throwables.getStackTraceAsString(ex) + "\n");
        else System.out.println(errorMessage);

        return errorMessage;
    }


    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {ForbiddenException.class,
            AccessDeniedException.class})
    @ResponseBody
    public ErrorMessage handleForbiddenException(HttpServletRequest req, Exception ex) {

        Throwable exRootCause = Throwables.getRootCause(ex);

        ErrorMessage errorMessage = new ErrorMessage(new Timestamp(System.currentTimeMillis()),
                ex.getClass().getName(),
                exRootCause.getStackTrace()[0].getClassName(),
                exRootCause.getStackTrace()[0].getMethodName(),
                req.getRequestURI(), 403, "FORBIDDEN"/*ex.getMessage()*/);

        if (printFullStackTrace)
            System.out.println(errorMessage + "\n" + Throwables.getStackTraceAsString(ex) + "\n");
        else System.out.println(errorMessage);

        return errorMessage;
    }

    public void setPrintFullStackTrace(boolean printFullStackTrace) {
        this.printFullStackTrace = printFullStackTrace;
    }

    public boolean getPrintFullStackTrace() {
        return this.printFullStackTrace;
    }


}