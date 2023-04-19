package carsharing;

import java.sql.*;

public class DatabaseUtil {

    private static Connection connection = null;
    private static final String databaseFilePath = "./src/carsharing/db/";
    private static String databaseFileName = "dbName";

    public static void setDatabaseFileName(String databaseFileName){
        DatabaseUtil.databaseFileName = databaseFileName;
    }

    public static void setUpTables(){
        try(Statement statement = getConnection().createStatement()) {
            String query =
//                    "DROP TABLE IF EXISTS CUSTOMER; " +
//                    "DROP TABLE IF EXISTS CAR; " +
//                    "DROP TABLE IF EXISTS COMPANY; " +
                    "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INTEGER not NULL AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) not NULL UNIQUE, " +
                    " PRIMARY KEY ( ID )" +
                    ");" +
                    "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INTEGER not NULL AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) not NULL UNIQUE, " +
                    "COMPANY_ID INTEGER not NULL, " +
                    "PRIMARY KEY ( ID ), " +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)" +
                    ");"+
                    "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INTEGER not NULL AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) not NULL UNIQUE, " +
                    "RENTED_CAR_ID INTEGER NULL, " +
                    "PRIMARY KEY ( ID ), " +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                    ");";
            statement.executeUpdate(query);
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + databaseFilePath+databaseFileName);
            connection.setAutoCommit(true); //to pass hyperskill tests
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

}
