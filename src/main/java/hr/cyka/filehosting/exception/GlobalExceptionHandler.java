package hr.cyka.filehosting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ErrorDetails> handleIOException(IOException ex) {
        ErrorDetails errorDetails = this.prepareErrorDetails(ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = StorageException.class)
    public ResponseEntity<ErrorDetails> handleStorageException(StorageException ex) {
        ErrorDetails errorDetails = this.prepareErrorDetails(ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MalformedURLException.class)
    public ResponseEntity<ErrorDetails> handleMalformedURLException(MalformedURLException ex) {
        ErrorDetails errorDetails = this.prepareErrorDetails(ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorDetails prepareErrorDetails(Exception exception){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage(exception.getMessage());
        return errorDetails;
    }
}
