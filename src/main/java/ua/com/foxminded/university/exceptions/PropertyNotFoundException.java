package ua.com.foxminded.university.exceptions;

public class PropertyNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6925242515545239477L;

    public PropertyNotFoundException(String message) {
        super(message);
    }

}
