package co.tz.werelay.common.exception;

/**
 * Base type for expected, business-rule failures (as opposed to bugs/infra failures).
 * Caught centrally by the {@code @ControllerAdvice} in app-api and turned into a
 * standardized RFC 7807 problem-details response — see GlobalExceptionHandler.
 */
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
