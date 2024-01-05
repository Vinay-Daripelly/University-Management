package com.example.vnr_university;
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import  java.sql.*;
public class ConnectionHelper {
    @SuppressLint("ALL")
    public Connection connectionMethod()
    {
        StrictMode. ThreadPolicy  threadPolicy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        Connection connection=null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            String DB_USER = "Vinay";
            String DB_PASSWORD = "Vinay@2004";
            String db = "University";
            String port = "1433";
            String ip = "192.168.24.32";
            String connectionUrl="jdbc:jtds:sqlserver://"+ ip +":"+ port +";"+"databasename="+ db +";user="+ DB_USER +";password="+ DB_PASSWORD;
            connection=DriverManager.getConnection(connectionUrl);
            System.out.println(connection);
        }
        catch(Exception ex)
        {
            Log.e("ERROR",ex.getMessage());
        }
        return connection;
    }
}
