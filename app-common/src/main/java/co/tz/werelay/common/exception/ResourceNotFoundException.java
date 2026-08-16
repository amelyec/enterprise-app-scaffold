package co.tz.werelay.common.exception;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceType, String id) {
        super("RESOURCE_NOT_FOUND", resourceType + " with id [" + id + "] was not found");
    }
}
