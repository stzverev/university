package ua.com.foxminded.university.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ua.com.foxminded.university.exceptions.ObjectNotFoundById;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ObjectNotFoundById.class})
    public ResponseEntity<Object> handleObjectNotFoundById(ObjectNotFoundById ex, WebRequest request) {
        logger.error(ex.getMessage());
        String message = "The page was not found. The data may have been deleted.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }


}
