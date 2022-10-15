package com.dreamgames.alihan.game.advise;

import com.dreamgames.alihan.game.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedArgument(MethodArgumentTypeMismatchException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(exception.getName(), exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleGenericException(RuntimeException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Exception is: ", exception.getCause().toString());
        return errorMap;
    }



    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}