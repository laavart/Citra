import java.sql.*;

public class Citra {

    private Connection connection = null;
    private Statement statement = null;

    static Connection connectDB(Database DB , String Path, String DB_User, String DB_AccessToken){
        try {
            Class.forName(DB.getDriver());
            return DriverManager.getConnection( Path , DB_User, DB_AccessToken);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Statement generateStatement(Connection DB) throws SQLException {
        return DB.createStatement();
    }

    public void searchInDB(String name) throws SQLException {
        ResultSet resultSet = statement.executeQuery("show tables;");
    }

}
