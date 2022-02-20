import citra.*;
import citra.exception.*;
import citra.client.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] Args) throws DBInvalidException {
        Database d = Database.connect(Source.MYSQL,"localhost","citra","root","1234");

        Token t = new Token("Abcde@1234","5678910");
        Comm c = new Comm("abc012@mail.com", "0000000000");
        Address a = new Address("a1", "a2", "229001", "Raebareli", "Uttar Pradesh", "India");
        User u = new User("abc","jkl", LocalDate.now());
        Client C = new Client(u,t,c,a);

        d.addNewUser(C);
    }
}
