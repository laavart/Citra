import Exception.*;

public class Main {
    public static void main(String[] Args) throws DBInvalidException {
        Database d = Database.connect(Source.MYSQL,"localhost","citra","root","1234");
    }
}
