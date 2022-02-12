public enum Database {
    MYSQL("com.mysql.jdbc.Driver"),
    ORACLE("oracle.jdbc.driver.OracleDriver"),
    DB2("COM.ibm.db2.jdbc.net.DB2Driver"),
    SYBASE("com.sybase.jdbc.SybDriver");

    final String DB_driver;
    Database(String DB_driver) {
        this.DB_driver = DB_driver;
    }

    public String getDriver(){
        return DB_driver;
    }
}
