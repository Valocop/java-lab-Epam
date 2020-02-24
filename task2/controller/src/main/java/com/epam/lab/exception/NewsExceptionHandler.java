package com.epam.lab.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class NewsExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIME = "time";
    private static final String STATUS = "status";
    private static final String ERRORS = "errors";

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME, new Date().toString());
        body.put(STATUS, status.value());

        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        body.put(ERRORS, errors);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME, new Date().toString());
        body.put(STATUS, e.getStatus().value());

        Map<String, List<String>> stringListMap = new LinkedHashMap<>();
        e.getHeaders().forEach((s, s2) -> stringListMap.put(s, Collections.singletonList(s2)));
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>(stringListMap);
        HttpHeaders httpHeaders = new HttpHeaders(multiValueMap);

        List<String> errors = Collections.singletonList(e.getReason());
        body.put(ERRORS, errors);
        return new ResponseEntity<>(body, httpHeaders, e.getStatus());
    }

    @Override
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME, new Date().toString());
        body.put(STATUS, status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put(ERRORS, errors);
        return new ResponseEntity<>(body, headers, status);
    }
}
