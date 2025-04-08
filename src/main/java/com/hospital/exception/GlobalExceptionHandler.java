package com.hospital.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        ApiError apiError;

        if (exception instanceof EntityNotFoundException) {
            apiError = new ApiError(Response.Status.NOT_FOUND.getStatusCode(),
                    exception.getMessage(), uriInfo.getPath());
            return Response.status(Response.Status.NOT_FOUND).entity(apiError).build();
        }

        if (exception instanceof BusinessException) {
            apiError = new ApiError(Response.Status.BAD_REQUEST.getStatusCode(),
                    exception.getMessage(), uriInfo.getPath());
            return Response.status(Response.Status.BAD_REQUEST).entity(apiError).build();
        }

        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) exception;
            apiError = new ApiError(Response.Status.BAD_REQUEST.getStatusCode(),
                    "Erro de validação", uriInfo.getPath());

            for (ConstraintViolation<?> violation : constraintViolation.getConstraintViolations()) {
                apiError.addError(violation.getPropertyPath() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(apiError).build();
        }

        if (exception instanceof WebApplicationException) {
            WebApplicationException webAppException = (WebApplicationException) exception;
            apiError = new ApiError(webAppException.getResponse().getStatus(),
                    webAppException.getMessage(), uriInfo.getPath());
            return Response.status(webAppException.getResponse().getStatus()).entity(apiError).build();
        }

        // Exceções genéricas não mapeadas
        apiError = new ApiError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Erro interno do servidor", uriInfo.getPath());
        apiError.addError(exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(apiError).build();
    }
}