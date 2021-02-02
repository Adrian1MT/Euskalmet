package com.elorrieta.euskalmet;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientThread implements Runnable {
    private String sResultado;
    private  String sql;
    public String columnaResultado;
    ArrayList<String> Resultado = new ArrayList<String>();

    public ClientThread(String consulta) {
        sql=consulta;
    }
    @Override
    public void run() {
        ResultSet rs = null;
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
            //sIP = "192.168.1.38";
            sIP = "192.168.56.1";

            sPuerto = "3306";
            sBBDD = "euskalmet";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection( url, "Root", "1234");
            //con = DriverManager.getConnection( url, "root", "");
            // Consulta sencilla en este caso.
            // sql = "SELECT * FROM municipio";
            //String sql = "SELECT Nombre FROM municipio WHERE Nombre='Amurrio'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            //
            Resultado.clear();
            while (rs.next()) {
                String var1 = rs.getString(columnaResultado);
                Log.i("XXXXXXX", var1);
                sResultado = var1;
                Resultado.add(sResultado);
            }
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
                // Cerrar ResultSet
                if(rs!=null) {
                    rs.close();
                }
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
    public ArrayList getResponse() {
        return Resultado;
    }
}