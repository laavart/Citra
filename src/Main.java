import citra.*;
import citra.exception.*;
import citra.client.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] Args) throws DBInvalidException {
        Database d = Database.connect(Source.MYSQL,"localhost","citra","root","1234");

        User u = new User("abc","jkl", LocalDate.now());
        Token t = new Token("Abcde@1234","5678910");
        Comm c = new Comm("abc012@mail.com", "0000000000");
        Address a = new Address("a1", "a2", "229001", "Raebareli", "Uttar Pradesh", "India");
        Client C = new Client(u,t,c,a);

        d.addNewUser(C);

        u = new User("abc123","jkl", LocalDate.now());
        t = new Token("Abcde@1234","5678910");
        c = new Comm("abc012@mail.com", "0000000000");
        a = new Address("a1", "a2", "229001", "Raebareli", "Uttar Pradesh", "India");
        C = new Client(u,t,c,a);

        d.addNewUser(C);
    }
}
