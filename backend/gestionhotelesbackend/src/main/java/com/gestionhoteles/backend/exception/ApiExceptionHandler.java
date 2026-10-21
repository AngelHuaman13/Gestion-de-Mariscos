package com.gestionhoteles.backend.exception;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import org.springframework.web.bind.MethodArgumentNotValidException; import java.util.*;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<Map<String,Object>> missing(ResourceNotFoundException e){return response(HttpStatus.NOT_FOUND,e.getMessage());}
 @ExceptionHandler({BadRequestException.class,org.springframework.dao.DataIntegrityViolationException.class}) ResponseEntity<Map<String,Object>> bad(Exception e){return response(HttpStatus.BAD_REQUEST,e instanceof BadRequestException?e.getMessage():"Los datos no cumplen las restricciones de la base de datos.");}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> invalid(MethodArgumentNotValidException e){String m=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Solicitud inválida"); return response(HttpStatus.BAD_REQUEST,m);}
 @ExceptionHandler(Exception.class) ResponseEntity<Map<String,Object>> unexpected(Exception e){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Ocurrió un error interno.");}
 private ResponseEntity<Map<String,Object>> response(HttpStatus s,String m){return ResponseEntity.status(s).body(Map.of("codigo",s.value(),"mensaje",m));}
}
