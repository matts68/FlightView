package fr.flight.view.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Instant;

/**
 * On part du principe qu'une erreur du backend est une internalServerError
 * Les "bad requests" sont issus du front qui ne respectent pas le contrat REST,
 * donc spring renverra par défaut un 400 BAD_REQUEST.
 * On utilise l'objet standard fourni par spring pour décrire les erreurs ie : RFC 7807 (Problem Details for HTTP APIs)
 * Du coup, il faut a minima que l'attribut 'message' de la pile d'objets des exceptions de base soit rempli
 * Coté front, la gestion des erreurs est du coup centralisable
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ProblemDetail> handleGlobalException(HttpServletRequest request, Exception ex) {

        // Si c'est une exception "framework" (ErrorResponse)
        if (ex instanceof ErrorResponse errorResponse) {
            ProblemDetail problemDetail = errorResponse.getBody();
            HttpStatusCode statusCode = errorResponse.getStatusCode();

            populateCommonProperties(problemDetail, request, ex);

            return ResponseEntity
                    .status(statusCode)
                    .body(problemDetail);
        }
        // Sinon, nos exceptions "maisons"
        else {
            HttpStatus httpStatusInternalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
            String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Error occur";

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatusInternalServerError, message);
            problemDetail.setType(URI.create("about:blank")); // Problème générique cf doc spring

            populateCommonProperties(problemDetail, request, ex);

            return ResponseEntity
                    .status(httpStatusInternalServerError)
                    .body(problemDetail);
        }
    }

    /**
     * Construit l'URL complète (path + queryString).
     */
    private String buildFullUrl(HttpServletRequest request) {
        StringBuilder fullUrl = new StringBuilder(request.getRequestURL());
        if (request.getQueryString() != null) {
            fullUrl.append("?").append(request.getQueryString());
        }
        return fullUrl.toString();
    }

    /**
     * Remplit les propriétés communes dans le ProblemDetail.
     */
    private void populateCommonProperties(ProblemDetail problemDetail,
            HttpServletRequest request,
            Exception ex) {

        problemDetail.setProperty("stackTrace", ex.getStackTrace());
        problemDetail.setProperty("requestURI", request.getRequestURI());
        problemDetail.setProperty("httpMethod", request.getMethod());
        problemDetail.setProperty("timestamp", Instant.now().toString());

        String fullUrl = buildFullUrl(request);
        problemDetail.setInstance(URI.create(fullUrl));

        if (request.getQueryString() != null) {
            problemDetail.setProperty("queryString", request.getQueryString());
        }
    }

}