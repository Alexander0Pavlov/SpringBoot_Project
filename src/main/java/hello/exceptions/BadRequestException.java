package src.main.java.hello.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// от него если что потом отнаследоваться если понадобится кидать более детальные
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "400 Bad Request")
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

}