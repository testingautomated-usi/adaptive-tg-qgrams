package po_utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class MySqlConnection {

    public MySqlConnection(){
    }

    public void resetUsingSqlScript(String username, String password, String dbName, int port, String aSQLScriptFilePath){
        Optional<Connection> optionalConnection = this.establishDBConnection(username, password, port, dbName);
        if(optionalConnection.isPresent()){
            Connection connection = optionalConnection.get();
            boolean enablePrint = false;
            ScriptRunner scriptRunner = new ScriptRunner(connection, enablePrint);
            try {
                Reader reader = new BufferedReader(new FileReader(aSQLScriptFilePath));
                scriptRunner.runScript(reader);
                scriptRunner.closeConnection();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            throw new IllegalStateException(this.getClass().getName() + ": connection failed");
        }
    }

    private Optional<Connection> establishDBConnection(String username, String password, int port, String dbName){
        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // Setup the connection with the DB
            Connection connection = DriverManager.getConnection("jdbc:mysql://pagekit:" + port + "/" + dbName, username, password);
            return Optional.of(connection);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void close(Statement statement){
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
