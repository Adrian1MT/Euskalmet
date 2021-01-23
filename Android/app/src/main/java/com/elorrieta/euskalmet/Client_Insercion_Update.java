package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Client_Insercion_Update implements Runnable{

    private  String sql;

    public Client_Insercion_Update(String consulta) {
        sql=consulta;
    }
    @Override
    public void run() {
        PreparedStatement st = null;
        Connection con = null;
        String sIP;
        String sPuerto;
        String sBBDD;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //Aqui pondriamos la IP y puerto.
            //sIP = "192.168.13.236";
            //sIP = "192.168.13.233";
            sIP = "192.168.56.1";
            sPuerto = "3306";
            sBBDD = "euskalmet";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection( url, "Root", "1234");
            //con = DriverManager.getConnection( url, "root", "");

            st = con.prepareStatement(sql);
            st.executeUpdate();


        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException", "");
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("SQLException", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Exception", "");
            e.printStackTrace();
        } finally {
            // Intentamos cerrar _todo.
            try {
                // Cerrar PreparedStatement
                if(st!=null) {
                    st.close();
                }
                // Cerrar Connection
                if(con!=null) {
                    con.close();
                }
            } catch (Exception e) {
                Log.e("Exception_cerrando todo", "");
                e.printStackTrace();
            }
    }
}
}