package cl.lillo.plantasarandanos.Gestion;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import cl.lillo.plantasarandanos.Conexion.ConexionHelperSQLServer;
import cl.lillo.plantasarandanos.Conexion.ConexionHelperSQLite;

/**
 * Created by Alexi on 29/06/2016.
 */
public class GestionHilera {
    private ConexionHelperSQLite helper;
    private ConexionHelperSQLServer helperSQLServer;

    public GestionHilera(Context context) {
        helper = new ConexionHelperSQLite(context);
        helperSQLServer = new ConexionHelperSQLServer();
    }

    public String insertar(String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String variedad, int id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Fundo", id_fundo);
        cv.put("ID_Potrero", id_potrero);
        cv.put("ID_Sector", id_sector);
        cv.put("ID_Cuartel", id_cuartel);
        cv.put("ID_Hilera", id_hilera);
        cv.put("Variedad", variedad);
        cv.put("ID_Mapeo", id_mapeo);
        data.insertWithOnConflict("Hilera", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Hilera Insertada!";
    }

    public String insertarSyncHilera(String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String variedad, int id_mapeo, String operacionSQL) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Fundo", id_fundo);
        cv.put("ID_Potrero", id_potrero);
        cv.put("ID_Sector", id_sector);
        cv.put("ID_Cuartel", id_cuartel);
        cv.put("ID_Hilera", id_hilera);
        cv.put("Variedad", variedad);
        cv.put("ID_Mapeo", id_mapeo);
        cv.put("OperacionSQL", operacionSQL);
        data.insertWithOnConflict("SyncHilera", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Hilera Insertada!";
    }

    public boolean insertarServer(String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String variedad, int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "insert into Hilera values ('" + id_hilera + "','" + id_cuartel + "','" + id_sector + "','" + id_potrero + "','" + id_fundo + "','" + variedad + "','" + id_mapeo + "')";
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

    public String actualizar(String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String variedad, int id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Variedad", variedad);
        data.update("Hilera", cv, "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Mapeo ='" + id_mapeo + "'", null);
        data.close();
        return "Hilera Actualizada!";
    }

    public boolean actualizarServer(String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String variedad, int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "update Hilera set Variedad='" + variedad + "' where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' and ID_Sector='" + id_sector + "' and ID_Cuartel='" + id_cuartel + "' and ID_Hilera='" + id_hilera + "' and ID_Mapeo='" + id_mapeo + "'";
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

    public String eliminar(String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, int id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("Planta", "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Mapeo='" + id_mapeo + "'", null);
        data.delete("Hilera", "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Mapeo='" + id_mapeo + "'", null);
        data.close();
        return "Atención! Hilera Eliminada";
    }

    public String limpiarSyncHilera() {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("SyncHilera", null, null);
        data.close();
        return "Atención! Hileras Eliminada";
    }

    public boolean eliminarServer(String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "delete from Hilera where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' and ID_Sector='" + id_sector + "' and ID_Cuartel='" + id_cuartel + "' and ID_Hilera='" + id_hilera + "' and ID_Mapeo='" + id_mapeo + "'";
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

    public ArrayList<String> getListaHileras(String id_fundo, String id_potrero, String id_sector, String id_cuartel, int id_mapeo) {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Hilera where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Mapeo='" + id_mapeo + "'", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getString(4) + "," + cursor.getString(5) + "," + cursor.getInt(6));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getAllHileras() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Hilera", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getString(4) + "," + cursor.getString(5) + "," + cursor.getInt(6));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getSyncHilera() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from SyncHilera ORDER BY idSync", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getString(4) + "," + cursor.getString(5) + "," + cursor.getInt(6) + "," + cursor.getString(7));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getListaHilerasServer(String id_fundo, String id_potrero, String id_sector, String id_cuartel, int id_mapeo) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from Hilera where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Mapeo='" + id_mapeo + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString(0) + "," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5)  + "," + rs.getInt(6));
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
                String query = "select * from Hilera";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Hilera") + "," + rs.getString("ID_Cuartel") + "," + rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getString("Variedad") + "," + rs.getInt("ID_Mapeo"));
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
                String query = "select * from SyncHilera ORDER BY idSync";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Hilera") + "," + rs.getString("ID_Cuartel") + "," + rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getString("Variedad") + "," + rs.getInt("ID_Mapeo") + "," + rs.getString("OperacionSQL"));
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
                String query = "delete from SyncHilera";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("server erro: " + ex.toString());
        }
    }

    public int getMaxHilera(String id_fundo, String id_potrero, String id_sector, String id_cuartel, int id_mapeo) {
        int maxHilera = 0;
        ArrayList<Integer> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select ID_Hilera from Hilera where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Mapeo='" + id_mapeo + "'", null);
        while (cursor.moveToNext()) {
            lista.add(Integer.parseInt(cursor.getString(0).replace("H", "")));
        }
        data.close();
        if (!lista.isEmpty()) {
            maxHilera = Collections.max(lista);
        }
        return maxHilera;
    }
}
