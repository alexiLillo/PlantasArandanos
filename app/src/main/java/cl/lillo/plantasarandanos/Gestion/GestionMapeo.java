package cl.lillo.plantasarandanos.Gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import cl.lillo.plantasarandanos.Conexion.ConexionHelperSQLServer;
import cl.lillo.plantasarandanos.Conexion.ConexionHelperSQLite;

/**
 * Created by Alexi on 29/06/2016.
 */
public class GestionMapeo {

    private ConexionHelperSQLite helper;
    private ConexionHelperSQLServer helperSQLServer;

    public GestionMapeo(Context context) {
        helper = new ConexionHelperSQLite(context);
        helperSQLServer = new ConexionHelperSQLServer();
    }

    public String insertar(int id_mapeo, String fecha_inicio, String fecha_termino) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Mapeo", id_mapeo);
        cv.put("Fecha_Inicio", fecha_inicio);
        cv.put("Fecha_Termino", fecha_termino);
        data.insertWithOnConflict("Map", null, cv,SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Mapeo Insertado!";
    }

    public boolean insertarServer(String fecha_inicio, String fecha_termino) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "insert into Map values ('" + fecha_inicio + "', '" + fecha_termino + "')";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public String actualizar(String fecha_inicio, String fecha_termino, int id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Fecha_Inicio", fecha_inicio);
        cv.put("Fecha_Termino", fecha_termino);
        data.update("Map", cv, "ID_Mapeo='" + id_mapeo + "'", null);
        data.close();
        return "Mapeo Actualizado!";
    }

    public boolean actualizarServer(String fecha_inicio, String fecha_termino, int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "update Map set Fecha_Inicio='" + fecha_inicio + "', Fecha_Termino='" + fecha_termino + "' where ID_Mapeo=" + id_mapeo + "";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public String eliminar(String id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("Map", "ID_Mapeo='" + id_mapeo + "'", null);
        data.close();
        return "Atención! Mapeo Eliminado";
    }

    public boolean eliminarServer(int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "delete from Map where ID_Mapeo=" + id_mapeo + "";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public ArrayList<String> getListaMapeos() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Map", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "-" + cursor.getString(1) + "," + cursor.getString(2));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getAllServer() {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from Map";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getInt("ID_Mapeo") + "," + rs.getString("Fecha_Inicio") + "," + rs.getString("Fecha_Termino"));
                }
                con.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return lista;
    }

    public ArrayList<String> getAllSyncServer() {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from SyncMap ORDER BY idSync";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getInt("ID_Mapeo") + "," + rs.getString("Fecha_Inicio") + "," + rs.getString("Fecha_Termino") + "," + rs.getString("OperacionSQL"));
                }
                con.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return lista;
    }

    public void clearSyncServer(){
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "delete from SyncMap";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("server erro: " + ex.toString());
        }
    }

    public int getLastMapeo() {
        int map = 0;
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select max(ID_Mapeo) from Map", null);
        while (cursor.moveToNext()) {
            map = cursor.getInt(0);
        }
        return map;
    }
}
