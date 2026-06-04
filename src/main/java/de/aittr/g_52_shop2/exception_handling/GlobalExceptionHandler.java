package de.aittr.g_52_shop2.exception_handling;

import de.aittr.g_52_shop2.exception_handling.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;

/*
Аннотация @RestControllerAdvice говорит о том, что перед нами - контроллер адвайс,
глобальный обработчик ошибок, которые возникают во всём проекте.
 Он не только обрабатывает ошибки, но и упаковывает их в response и отправляет клиенту.
Он одновременно является и restController и обработчиком ошибок.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

     /*
    ResponseEntity - это специальный объект, внутрь которого мы можем
    заложить статус ответа, который получит наш клиент, а также
    любой объект, какой хотим, который будет отправлен клиенту.
    В данном случае помимо статуса мы в объект ResponseEntity закладываем
    ещё и объект своего Response, заложив в него сообщение об ошибке.
     */

    /*
    ПЛЮС -  мы создаём глобальный обработчик ошибок, который умеет ловить
            ошибки, возникающие во всем проекте и обрабатывать их в одном месте
    ПЛЮС -  логика обработки ошибок вынесена в отдельный класс, таким образом
            исходные методы содержат только чистую бизнес-логику, не
            нагруженную обработкой ошибок
    МИНУС - такой подход нам не подойдёт, если нам нужна разная логика обработки
            ошибок для разных контроллеров. В таком случае лучше воспользоваться
            первыми двумя способами
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException e) {
        String message = e.getMessage();
        logger.warn(message);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    //    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Response> handleException(EntityNotFoundException e) {
//        Response response = new Response(e.getMessage());
//        logger.warn(response.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleException(NullPointerException e) {
        String message = e.getMessage();
        logger.error(message, e);
        return new ResponseEntity<>(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleException(RegistrationException e) {
        String message = e.getMessage();
        logger.warn(message, e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> handleException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        messages.forEach(logger::warn);
        return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<String> handleException(FileUploadException e) {
        String message = e.getMessage();
        logger.warn(message, e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleException(IOException e) {
        String message = e.getMessage();
        logger.error(message, e);
        return new ResponseEntity<>(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<String> handleException(EmailSendingException e) {
        String message = e.getMessage();
        logger.error(message, e);
        return new ResponseEntity<>(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

//    @ExceptionHandler(ProductValidationException.class)
//    public ResponseEntity<Response> handleException(ProductValidationException e) {
//        Response response = new Response(e.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(CustomerNotFoundException.class)
//    public ResponseEntity<Response> handleException(CustomerNotFoundException e) {
//        Response response = new Response(e.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(CustomerValidationException.class)
//    public ResponseEntity<Response> handleException(CustomerValidationException e) {
//        Response response = new Response(e.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
}
