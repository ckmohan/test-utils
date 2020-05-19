package uk.gov.justice.services.test.utils.core.schema;

public class SchemaCheckerException extends RuntimeException{

    public SchemaCheckerException(String message) {
        super(message);
    }

    public SchemaCheckerException(Throwable cause) {
        super(cause);
    }
}
