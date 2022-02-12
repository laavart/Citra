import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Citra {

    static Connection connectDB(Database DB , String Hostname, int Port, String DB_User, String DB_AccessToken){
        try {
            Class.forName(DB.getDriver());
            String Path = "";
            return DriverManager.getConnection( Path , DB_User, DB_AccessToken);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
