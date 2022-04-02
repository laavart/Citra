package citra.exception;

public class DBInvalidException extends Exception{
    public DBInvalidException() {
        super("Database and Arguments does not match!");
    }
}