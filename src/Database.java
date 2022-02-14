import java.sql.*;

public class Database {

    private Connection connection = null;
    private Statement statement = null;

    private Database(){
        System.out.println("Connection Started !");
    }

    static Database connect(DBSource DB , String Hostname, String Port, String Database, String DB_User, String DB_AccessToken){
        try {
            Class.forName(DB.getDriver());
            Database database = new Database();
            database.connection = DriverManager.getConnection( DB.getPath(Hostname,Port,Database), DB_User, DB_AccessToken);
            System.out.println("Connection Established !");
            database.statement = database.connection.createStatement();
            return database;
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed !");
            return null;
        }
    }

    static Database connect(DBSource DB , String Hostname, String Database, String DB_User, String DB_AccessToken){
        try {
            Class.forName(DB.getDriver());
            Database database = new Database();
            database.connection = DriverManager.getConnection( DB.getPath(Hostname,Database), DB_User, DB_AccessToken);
            System.out.println("Connection Established !");
            database.statement = database.connection.createStatement();
            return database;
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed !");
            return null;
        }
    }

    public void searchInDB(String name) throws SQLException {
        ResultSet resultSet = statement.executeQuery("");
    }

}
