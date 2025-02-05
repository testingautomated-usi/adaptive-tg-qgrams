package po_utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class ResetAppState {

    public static void main(String[] args) {
        reset();
    }

    public static void reset(){
        int dbPort = 5432;
        clearDB("postgres","postgres","phoenix_trello_dev", dbPort);
    }

    public static void resetClient(WebDriver driver){
        clearStorage(driver);
        String appUrl = "http://phoenix:4000";
        driver.get(appUrl);
    }

    private static void clearDB(String username, String password, String dbName, int port){
        MyPSqlConnection myPSqlConnection = new MyPSqlConnection();
        myPSqlConnection.reset(username, password, dbName, port,"false");
    }

    private static void clearStorage(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(String.format(
                "return window.localStorage.removeItem('%s');", "phoenixAuthToken"));
    }
}
