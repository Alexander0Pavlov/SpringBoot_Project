package src.main.java.hello.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// от него если что потом отнаследоваться если понадобится кидать более детальные
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "403 Forbidden")
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }

}