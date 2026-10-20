package com.acme.salary.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.acme.salary.api.dto.ErrorResponse;
import com.acme.salary.service.BadRequestException;
import com.acme.salary.service.ConflictException;
import com.acme.salary.service.NotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.reduce((left, right) -> left + "; " + right)
				.orElse("Request is invalid");
		return ResponseEntity.badRequest().body(new ErrorResponse(message));
	}

	@ExceptionHandler(BadRequestException.class)
	ResponseEntity<ErrorResponse> badRequest(BadRequestException exception) {
		return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
	}

	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<ErrorResponse> notFound(NotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
	}

	@ExceptionHandler(ConflictException.class)
	ResponseEntity<ErrorResponse> conflict(ConflictException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
	}
}
