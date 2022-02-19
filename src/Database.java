import java.sql.*;
import java.util.ArrayList;

import Exception.*;

public class Database {

    private Source source;
    private String hostname;
    private String port;
    private String database;

    private Connection connection = null;
    private Statement statement = null;

    private static ArrayList<Source> CheckForPort;
    static {
        CheckForPort = new ArrayList<>(1);
        CheckForPort.add(Source.MYSQL);
    }

    private static ArrayList<Source> CheckByOwner;
    static {
        CheckByOwner = new ArrayList<>(1);
        CheckByOwner.add(Source.ORACLE);
    }

    private Database(){
        System.out.println("Connection Started !");
        this.port = "";
    }

    public static Database connect(Source source , String hostname, String port, String database, String user, String token) throws DBInvalidException {
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

                db.connection = DriverManager.getConnection(source.getPath(hostname, port, database), user, token);
                System.out.println("Connection Established !");

                db.statement = db.connection.createStatement();

                if(!db.searchTable("token_master")){
                    db.statement.executeUpdate(
                            "create table token_master (" +
                                    "uID int primary key, " +
                                    "Password varchar(128) unique, " +
                                    "SecurityCode varchar(7) unique" +
                                    ");"
                    );
                }
                if(!db.searchTable("comm_master")){
                    db.statement.executeUpdate(
                            "create table comm_master (" +
                                    "uID int primary key, " +
                                    "Email varchar(40) unique, " +
                                    "Mobile varchar(10) unique" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_master")){
                    db.statement.executeUpdate(
                            "create table user_master (" +
                                    "uID int primary key, " +
                                    "Name varchar(35), " +
                                    "DOB date, " +
                                    "Address int, " +
                                    "foreign key (Address) references user_address(aID)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address")){
                    db.statement.executeUpdate(
                            "create table user_address (" +
                                    "aID int primary key, " +
                                    "AddressLine1 varchar(40), " +
                                    "AddressLine2 varchar(40), " +
                                    "PostalCode int, " +
                                    "CountryCode int, " +
                                    "foreign key (PostalCode) references user_address_code_postal(pID), " +
                                    "foreign key (CountryCode) references user_address_code_country(cID)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address_code_postal")){
                    db.statement.executeUpdate(
                            "create table user_address_code_postal (" +
                                    "pID int primary key," +
                                    "City varchar(40)," +
                                    "State int, " +
                                    "foreign key (State) references user_address_code_state(sID)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address_code_state")){
                    db.statement.executeUpdate(
                            "create table user_address_code_state (" +
                                    "sID int primary key," +
                                    "State varchar(40)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address_code_country")){
                    db.statement.executeUpdate(
                            "create table user_address_code_country (" +
                                    "cID int primary key," +
                                    "Country varchar(40)" +
                                    ");"
                    );
                }

                return db;
            }
            catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed !");
                return null;
            }
        }
    }

    public static Database connect(Source source , String hostname, String database, String user, String token) throws DBInvalidException {
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

                db.connection = DriverManager.getConnection(source.getPath(hostname, database), user, token);
                System.out.println("Connection Established !");

                db.statement = db.connection.createStatement();

                if(!db.searchTable("token_master")){
                    db.statement.executeUpdate(
                            "create table token_master (" +
                                    "uID int primary key, " +
                                    "Token varchar(128) unique, " +
                                    "Code varchar(7) unique" +
                                    ");"
                    );
                }
                if(!db.searchTable("comm_master")){
                    db.statement.executeUpdate(
                            "create table comm_master (" +
                                    "uID int primary key, " +
                                    "Email varchar(40) unique, " +
                                    "Mobile varchar(10) unique" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_master")){
                    db.statement.executeUpdate(
                            "create table user_master (" +
                                    "uID int primary key, " +
                                    "User varchar(128) unique, " +
                                    "Name varchar(35), " +
                                    "DOB date, " +
                                    "Address int, " +
                                    "foreign key (Address) references user_address(aID)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address")){
                    db.statement.executeUpdate(
                            "create table user_address (" +
                                    "aID int primary key, " +
                                    "AddressLine1 varchar(40), " +
                                    "AddressLine2 varchar(40), " +
                                    "PostalCode int, " +
                                    "CountryCode int, " +
                                    "foreign key (PostalCode) references user_address_code_postal(pID), " +
                                    "foreign key (CountryCode) references user_address_code_country(cID)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address_code_postal")){
                    db.statement.executeUpdate(
                            "create table user_address_code_postal (" +
                                    "pID int primary key," +
                                    "City varchar(40)," +
                                    "State int, " +
                                    "foreign key (State) references user_address_code_state(sID)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address_code_state")){
                    db.statement.executeUpdate(
                            "create table user_address_code_state (" +
                                    "sID int primary key," +
                                    "State varchar(40)" +
                                    ");"
                    );
                }

                if(!db.searchTable("user_address_code_country")){
                    db.statement.executeUpdate(
                            "create table user_address_code_country (" +
                                    "cID int primary key," +
                                    "Country varchar(40)" +
                                    ");"
                    );
                }

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

    public boolean checkForUser(String user) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "select * " +
                        "from user_master " +
                        "where User = '"+user+"';");
        while(resultSet.next()) return true;
        return false;
    }

    public boolean validateUser(String user, String token) throws SQLException{
        if(checkForUser(user)) {
            ResultSet resultSet = statement.executeQuery(
                    "select Token " +
                            "from token_master " +
                            "where uID = any(" +
                                "select uID from user_master where User = '"+user+"'" +
                            ");"
            );
            while (resultSet.next() && resultSet.getString("Token").equals(token)) return true;
        }
        return false;
    }

    public boolean checkCode(String user, String code) throws SQLException{
        if(checkForUser(user)) {
            ResultSet resultSet = statement.executeQuery(
                    "select Code " +
                            "from token_master " +
                            "where uID = any(" +
                            "select uID from user_master where User = '"+user+"'" +
                            ");"
            );
            while (resultSet.next() && resultSet.getString("Code").equals(code)) return true;
        }
        return false;
    }

    public void addNewUser(Client client) throws SQLException{
        if(checkForUser(client.user().username())){
            statement.executeUpdate(

            );

            statement.executeUpdate(
                    ""
            );
        }
    }

    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

}
