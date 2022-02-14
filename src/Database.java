import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private DBSource source;
    private String hostname;
    private String port;
    private String database;
    private String user;
    private String token;

    private Connection connection = null;
    private Statement statement = null;

    private static ArrayList<DBSource> CheckForPort;
    static {
        CheckForPort = new ArrayList<>(1);
        CheckForPort.add(DBSource.MYSQL);
    }

    private static ArrayList<DBSource> CheckByOwner;
    static {
        CheckByOwner = new ArrayList<>(1);
        CheckByOwner.add(DBSource.ORACLE);
    }

    private Database(){
        System.out.println("Connection Started !");
        this.port = "";
    }

    static Database connect(DBSource source , String hostname, String port, String database, String user, String token) throws DBInvalidException {
        if(CheckForPort.contains(source)){
            throw new DBInvalidException();
        }
        else{
            try {
                Class.forName(source.getDriver());
                Database db = new Database();
                db.source = source;
                db.hostname = hostname;
                db.port = port;
                db.database = database;
                db.user = user;
                db.token = token;

                db.connection = DriverManager.getConnection(source.getPath(hostname, port, database), user, token);
                System.out.println("Connection Established !");

                db.statement = db.connection.createStatement();

                return db;
            }
            catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed !");
                return null;
            }
        }
    }

    static Database connect(DBSource source , String hostname, String database, String user, String token) throws DBInvalidException {
        if(!CheckForPort.contains(source)){
            throw new DBInvalidException();
        }
        else {
            try {
                Class.forName(source.getDriver());
                Database db = new Database();
                db.source = source;
                db.hostname = hostname;
                db.database = database;
                db.user = user;
                db.token = token;

                db.connection = DriverManager.getConnection(source.getPath(hostname, database), user, token);
                System.out.println("Connection Established !");

                db.statement = db.connection.createStatement();

                return db;
            }
            catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed !");
                return null;
            }
        }
    }

    public boolean searchTable(String name) throws SQLException {
        {
            ResultSet resultSet =
                    CheckByOwner.contains(source)
                            ? statement.executeQuery(source.getTables(database))
                            : statement.executeQuery(source.getTables()) ;
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(name))
                    return true;
            }
            return false;
        }
    }

    public void createTable(String table, HashMap<String,String> fields) throws SQLException {
        String query = "";
        statement.executeUpdate(query);
    }

}
