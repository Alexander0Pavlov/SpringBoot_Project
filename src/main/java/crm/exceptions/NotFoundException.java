package src.main.java.crm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// от него если что потом отнаследоваться если понадобится кидать более детальные
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "404 Not found")
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

}