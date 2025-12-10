package gr.ckaraiskos.candlefactory.candle.exception.handler;

import gr.ckaraiskos.candlefactory.candle.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex) {
        log.error("EntityNotFoundException", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistsException(EntityAlreadyExistsException ex) {
        log.error("EntityAlreadyExistsException", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(LeaveComponentFailureException.class)
    public ResponseEntity<String> handleLeaveComponentFailureException(LeaveComponentFailureException ex) {
        log.error("LeaveComponentFailureException", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
