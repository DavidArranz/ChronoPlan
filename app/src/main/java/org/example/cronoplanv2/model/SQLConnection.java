package org.example.cronoplanv2.model;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    private static String ip = "cronoplan.database.windows.net";// this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc
    private static String port = "1433";// the port sql server runs on
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";// the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql
    private static String database = "cronoplan";// the data base name
    private static String username = "azureadmin";// the user name
    private static String password = "6VL_L#g&q&3Un,?>uFj{1.)N%o";// the password
    private static String url = "jdbc:sqlserver://cronoplan.database.windows.net:1433;database=cronoplan;user=azureadmin@cronoplan;password=6VL_L#g&q&3Un,?>uFj{1.)N%o;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"; // the connection url string

    public Connection connection;
    public static SQLConnection instancia;

    private SQLConnection(){
        connection = null;
    }
    public Connection conectar(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            this.connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
   }

    public void desconectar(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized static SQLConnection getInstancia(){
        if (instancia==null){
            instancia=new SQLConnection();
        }
        return instancia;
    }

}
