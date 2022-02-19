package citra;

import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import citra.client.Address;
import citra.exception.*;

public class Database {

    private Source source;
    private String hostname;
    private String port;
    private String database;

    private Connection connection = null;
    private Statement statement = null;

    private static final ArrayList<Source> CheckForPort;
    static {
        CheckForPort = new ArrayList<>(1);
        CheckForPort.add(Source.MYSQL);
    }

    private static final ArrayList<Source> CheckByOwner;
    static {
        CheckByOwner = new ArrayList<>(1);
        CheckByOwner.add(Source.ORACLE);
    }

    private Database(){
        System.out.println("Connection Started !");
        this.port = "";
    }

    private static void initialize(Database db) throws SQLException {

        if(!db.searchTable("user_address_code_country")){
            db.statement.executeUpdate(
                    "create table user_address_code_country (" +
                            "cID int primary key," +
                            "Country varchar(100)" +
                            ");"
            );
        }

        if(!db.searchTable("user_address_code_state")){
            db.statement.executeUpdate(
                    "create table user_address_code_state (" +
                            "sID int primary key," +
                            "State varchar(100), " +
                            "Country int, " +
                            "foreign key (Country) references user_address_code_country(cID)" +
                            ");"
            );
        }

        if(!db.searchTable("user_address_code_postal")){
            db.statement.executeUpdate(
                    "create table user_address_code_postal (" +
                            "pID int primary key," +
                            "City varchar(100)," +
                            "State int, " +
                            "foreign key (State) references user_address_code_state(sID)" +
                            ");"
            );
        }

        if(!db.searchTable("user_address")){
            db.statement.executeUpdate(
                    "create table user_address (" +
                            "aID int primary key, " +
                            "AddressLine1 varchar(100), " +
                            "AddressLine2 varchar(100), " +
                            "PostalCode int, " +
                            "CountryCode int, " +
                            "foreign key (PostalCode) references user_address_code_postal(pID), " +
                            "foreign key (CountryCode) references user_address_code_country(cID)" +
                            ");"
            );
        }

        if(!db.searchTable("user_master")){
            db.statement.executeUpdate(
                    "create table user_master (" +
                            "uID int primary key, " +
                            "User varchar(128) unique, " +
                            "Name varchar(100), " +
                            "DOB date, " +
                            "Address int, " +
                            "foreign key (Address) references user_address(aID)" +
                            ");"
            );
        }

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
                            "Email varchar(150) unique, " +
                            "Mobile varchar(10) unique" +
                            ");"
            );
        }
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
                initialize(db);

                return db;
            }
            catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed !");
                e.printStackTrace();
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
                initialize(db);

                return db;
            }
            catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed !");
                e.printStackTrace();
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
        return resultSet.next();
    }

    public boolean validateUser(String user, String token) throws SQLException{
        if(checkForUser(user)) {
            ResultSet resultSet = statement.executeQuery(
                    "select token " +
                            "from token_master " +
                            "where uID = any(" +
                                "select uID from user_master where User = '"+user+"'" +
                            ");"
            );
            return resultSet.next() && resultSet.getString("Token").equals(token);
        }
        return false;
    }

    public boolean checkCode(String user, String code) throws SQLException{
        if(checkForUser(user)) {
            ResultSet resultSet = statement.executeQuery(
                    "select code " +
                            "from token_master " +
                            "where uID = any(" +
                            "select uID from user_master where User = '"+user+"'" +
                            ");"
            );
            return resultSet.next() && resultSet.getString("Code").equals(code);
        }
        return false;
    }

    public void addNewUser(Client client){

        if(Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(client.token().token()).matches()){
            System.out.println("Password not valid");
            return;
        }

        if(client.token().code().length() != 7){
            System.out.println("Security Code must exactly be of 7 digits");
            return;
        }

        if(Pattern.compile("^(?=.{7,150})[a-zA-Z0-9+._-]+@[a-zA-Z0-9.]+$").matcher(client.comm().email()).matches()){
            System.out.println("Invalid E-mail ID");
            return;
        }

        if(client.comm().mobile().length() != 10){
            System.out.println("Invalid Mobile Number");
            return;
        }

        if(client.address().postal().length() != 6){
            System.out.println("Pin-code is of 6 digits");
            return;
        }

        try{
            if (!checkForUser(client.user().username())) {
                int id;
                ResultSet resultSet = statement.executeQuery("select max(uid)+1 from user_master;");
                id = resultSet.next() ? resultSet.getInt(1) : 1;

                statement.executeUpdate(
                        "insert into token_master (" +
                                id + "," +
                                "'" + client.token().token() + "'," +
                                "'" + client.token().code() + "'" +
                                ");"
                );

                statement.executeUpdate(
                        "insert into comm_master (" +
                                id + "," +
                                "'" + client.comm().email() + "'," +
                                "'" + client.comm().mobile() + "'" +
                                ");"
                );

                statement.executeUpdate(
                        "insert into token_master (" +
                                id + "," +
                                "'" + client.token().token() + "'," +
                                "'" + client.token().code() + "'" +
                                ");"
                );

            }
        }
        catch (SQLException e) {
            System.out.println("Task Failed!");
        }
    }

    private void setCountryCode(Address address) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select cID from user_address_code_country where Country = '"+address.country()+"';");
        if(!resultSet.next()){
            resultSet = statement.executeQuery("select max(cID)+1 from user_address_code_country;");
            statement.executeUpdate(
                    "insert into user_address_code_country (" +
                            (resultSet.next() ? resultSet.getInt(1) : 1) + "," +
                            "'" + address.country() + "'" +
                            ");"
            );
        }
    }

    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

}
