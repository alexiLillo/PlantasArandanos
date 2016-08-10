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
public class GestionSector {
    private ConexionHelperSQLite helper;
    private ConexionHelperSQLServer helperSQLServer;

    public GestionSector(Context context) {
        helper = new ConexionHelperSQLite(context);
        helperSQLServer = new ConexionHelperSQLServer();
    }

    public String insertar(String id_sector, String id_potrero, String id_fundo, String nombre) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Fundo", id_fundo);
        cv.put("ID_Potrero", id_potrero);
        cv.put("ID_Sector", id_sector);
        cv.put("Nombre", nombre);
        data.insertWithOnConflict("Sector", null, cv,SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Sector Insertado!";
    }

    public boolean insertarServer(String id_sector, String id_potrero, String id_fundo, String nombre) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "insert into Sector values ('" + id_sector + "','" + id_potrero + "', '" + id_fundo + "', '" + nombre + "')";
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

    public String actualizar(String id_sector, String id_potrero, String id_fundo, String nombre) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Nombre", nombre);
        data.update("Sector", cv, "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "'", null);
        data.close();
        return "Sector Actualizado!";
    }

    public boolean actualizarServer(String id_sector,  String id_potrero, String id_fundo, String nombre) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "update Sector set Nombre='" + nombre + "' where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "'";
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

    public String eliminar(String id_sector, String id_potrero, String id_fundo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("Sector", "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "'", null);
        data.close();
        return "Atención! Sector Eliminado";
    }

    public boolean eliminarServer(String id_sector, String id_potrero, String id_fundo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "delete from Sector where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "'";
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

    public ArrayList<String> getListaSectores(String id_fundo, String id_potrero) {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Sector where ID_Fundo = '" + id_fundo + "' AND ID_Potrero = '" + id_potrero + "'", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getListaSectoresServer(String id_fundo, String id_potrero) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from Sector where ID_Fundo='" + id_fundo + "' AND ID_Potrero = '" + id_potrero + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString(0) + "," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3));
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
                String query = "select * from Sector";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getString("Nombre"));
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
                String query = "select * from SyncSector ORDER BY idSync";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getString("Nombre") + "," + rs.getString("OperacionSQL"));
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
                String query = "delete from SyncSector";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("server erro: " + ex.toString());
        }
    }
}
