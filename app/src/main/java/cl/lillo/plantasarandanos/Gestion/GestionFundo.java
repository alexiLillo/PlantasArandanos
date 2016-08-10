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
public class GestionFundo {

    private ConexionHelperSQLite helper;
    private ConexionHelperSQLServer helperSQLServer;

    public GestionFundo(Context context) {
        helper = new ConexionHelperSQLite(context);
        helperSQLServer = new ConexionHelperSQLServer();
    }

    public String insertar(String id_fundo, String nombre) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Fundo", id_fundo);
        cv.put("Nombre", nombre);
        data.insertWithOnConflict("Fundo", null, cv,SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Fundo Insertado!";
    }

    public boolean insertarServer(String id_fundo, String nombre) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "insert into Fundo values ('" + id_fundo + "', '" + nombre + "')";
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

    public String actualizar(String id_fundo, String nombre) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Nombre", nombre);
        data.update("Fundo", cv, "ID_Fundo='" + id_fundo + "'", null);
        data.close();
        return "Fundo Actualizado!";
    }

    public boolean actualizarServer(String id_fundo, String nombre) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "update Fundo set Nombre='" + nombre + "' where ID_Fundo='" + id_fundo + "'";
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

    public String eliminar(String id_fundo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("Fundo", "ID_Fundo='" + id_fundo + "'", null);
        data.close();
        return "Atención! Fundo Eliminado";
    }

    public boolean eliminarServer(String id_fundo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "delete from Fundo where ID_Fundo='" + id_fundo + "'";
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

    public ArrayList<String> getListaFundos() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Fundo", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getListaFundosServer() {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from Fundos";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString(0) + "," + rs.getString(1));
                }
                con.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return lista;
    }

    public ArrayList<String> getAllServer() {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from Fundo";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Fundo") + "," + rs.getString("Nombre"));
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
                String query = "select * from SyncFundo ORDER BY idSync";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Fundo") + "," + rs.getString("Nombre") + "," + rs.getString("OperacionSQL"));
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
                String query = "delete from SyncFundo";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("server erro: " + ex.toString());
        }
    }
}
