public enum DBSource {
    MYSQL(
            "com.mysql.jdbc.Driver",
            "jdbc:mysql://",
            "/",
            "show tables;"
    ),
    ORACLE(
            "oracle.jdbc.driver.OracleDriver",
            "jdbc:oracle:thin:@",
            ":",
            "select table_name, owner from all_table where owner = \""
    ),
    DB2(
            "COM.ibm.db2.jdbc.net.DB2Driver",
            "jdbc:db2:",
            "/",
            ""
    ),
    SYBASE(
            "com.sybase.jdbc.SybDriver",
            "jdbc:sybase:Tds:",
            "/",
            ""
    );

    final private String DB_driver;
    final private String Path;
    final private String Link;
    final private String ShowT;

    DBSource(String DB_driver, String Path, String Link, String ShowT) {
        this.DB_driver = DB_driver;
        this.Path = Path;
        this.Link = Link;
        this.ShowT = ShowT;
    }

    public String getDriver(){
        return DB_driver;
    }

    public String getPath(String hostname, String Port, String Database){
        return Path + hostname + ":" + Port + Link + Database;
    }

    public String getPath(String Hostname, String Database){
        return Path + Hostname + Link + Database;
    }

    public String getTables(String owner){
        return ShowT + owner + "\";";
    }

    public String getTables(){
        return ShowT;
    }
}
