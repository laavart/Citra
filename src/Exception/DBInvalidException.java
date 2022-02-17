package Exception;

public class DBInvalidException extends Exception{
    public DBInvalidException() {
        super("Database and Arguments not match !");
    }
}