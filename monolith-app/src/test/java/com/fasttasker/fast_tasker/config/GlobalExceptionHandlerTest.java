package com.fasttasker.fast_tasker.config;

import com.fasttasker.fast_tasker.application.exception.AccountNotFoundException;
import com.fasttasker.fast_tasker.application.exception.DomainException;
import com.fasttasker.fast_tasker.application.exception.EmailAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleEmailExists_ShouldReturnConflict() {
        // arrange
        String existingEmail = "gato@gmail.com";
        String messageEx = "the email address '" + existingEmail + "' is already in use";
        var ex = new EmailAlreadyExistsException(existingEmail);

        // act
        ResponseEntity<ProblemDetail> response = handler.handleEmailExists(ex);

        // assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conflict", response.getBody().getTitle());
        assertEquals(messageEx, response.getBody().getDetail());
        assertEquals(URI.create("urn:problem:email-exists"), response.getBody().getType());
        assertNotNull(response.getBody().getProperties());
        assertTrue(response.getBody().getProperties().containsKey("timestamp"));
    }

    @Test
    void handleDomainValidation_ShouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Invalid domain data";
        var ex = new DomainException(errorMessage);

        // Act
        ResponseEntity<ProblemDetail> response = handler.handleDomainValidation(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad Request", response.getBody().getTitle());
        assertEquals(errorMessage, response.getBody().getDetail());
        assertEquals(URI.create("urn:problem:invalid-data"), response.getBody().getType());
    }

    @Test
    void handleNotFound_ShouldReturnNotFound() {
        // Arrange
        String errorMessage = "Account not found";
        AccountNotFoundException ex = new AccountNotFoundException(errorMessage);

        // Act
        ResponseEntity<ProblemDetail> response = handler.handleNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not Found", response.getBody().getTitle());
        assertEquals(errorMessage, response.getBody().getDetail());
    }

    @Test
    void handleGeneric_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new RuntimeException("Something went wrong");

        // Act
        ResponseEntity<ProblemDetail> response = handler.handleGeneric(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getTitle());
        assertEquals("An unexpected error occurred", response.getBody().getDetail());
    }
}