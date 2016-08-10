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
public class GestionVariedad {
    private ConexionHelperSQLite helper;
    private ConexionHelperSQLServer helperSQLServer;

    public GestionVariedad(Context context) {
        helper = new ConexionHelperSQLite(context);
        helperSQLServer = new ConexionHelperSQLServer();
    }

    public String insertar(String id_variedad, String nombre, String id_producto) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Variedad", id_variedad);
        cv.put("Nombre", nombre);
        cv.put("ID_Producto", id_producto);
        data.insertWithOnConflict("Variedad", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Variedad Insertada!";
    }

    public boolean insertarServer(String id_variedad, String nombre, String id_producto) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "insert into Variedad values ('" + id_variedad + "', '" + nombre + "', '" + id_producto + "')";
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

    public String actualizar(String id_variedad, String nombre, String id_producto) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Nombre", nombre);
        data.update("Variedad", cv, "ID_Variedad='" + id_variedad + "' AND ID_Producto='" + id_producto + "'", null);
        data.close();
        return "Variedad Actualizada!";
    }

    public String eliminar(String id_variedad, String id_producto) {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("Variedad", "ID_Variedad='" + id_variedad + "' AND ID_Producto='" + id_producto + "'", null);
        data.close();
        return "Atención! Variedad Eliminada";
    }

    public boolean eliminarServer(String id_variedad, String id_producto) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "delete from Variedad where ID_Variedad='" + id_variedad + "' AND ID_Producto='" + id_producto + "'";
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

    public ArrayList<String> getListaVariedades(String id_producto) {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select Nombre from Variedad where ID_Producto='" + id_producto + "'", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getAllServer(String id_producto) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select Nombre from Variedad where ID_Producto='" + id_producto + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("Nombre"));
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
                String query = "select * from SyncVariedad ORDER BY idSync";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Variedad") + "," + rs.getString("Nombre") + "," + rs.getString("ID_Producto") + "," + rs.getString("OperacionSQL"));
                }
                con.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return lista;
    }

    public void clearSyncServer() {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "delete from SyncVariedad";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("server erro: " + ex.toString());
        }
    }
}
