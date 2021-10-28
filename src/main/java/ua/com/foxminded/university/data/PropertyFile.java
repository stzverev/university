package ua.com.foxminded.university.data;

public enum PropertyFile {

    QUERIES("queries.properties"),
    CONFIG("config.properties");

    public final String name;

    private PropertyFile(String name) {
        this.name = name;
    }

}
