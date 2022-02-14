public enum DBSource {
    MYSQL("com.mysql.jdbc.Driver","jdbc:mysql://","/"),
    ORACLE("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@",":"),
    DB2("COM.ibm.db2.jdbc.net.DB2Driver","jdbc:db2:","/"),
    SYBASE("com.sybase.jdbc.SybDriver","jdbc:sybase:Tds:","/");

    final String DB_driver;
    final String Path;
    final String Link;

    DBSource(String DB_driver, String Path, String Link) {
        this.DB_driver = DB_driver;
        this.Path = Path;
        this.Link = Link;
    }

    public String getDriver(){
        return DB_driver;
    }

    public String getPath(String Hostname, String Port, String Database){
        return Path + Hostname + ":" + Port + Link + Database;
    }

    public String getPath(String Hostname, String Database){
        return Path + Hostname + Link + Database;
    }
}
