package citra.client;

public record User(String username, String name, java.time.LocalDate dob) {
    public User(String name, java.time.LocalDate dob){
        this(null, name, dob);
    }
}
