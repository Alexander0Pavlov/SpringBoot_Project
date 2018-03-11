package src.main.java.hello.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// от него если что потом отнаследоваться если понадобится кидать более детальные
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "500 Internal server error")
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException() {
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

}