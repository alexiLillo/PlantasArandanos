package cl.lillo.plantasarandanos.Gestion;

import android.content.ContentValues;
import android.content.Context;
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
public class GestionPlanta {
    private ConexionHelperSQLite helper;
    private ConexionHelperSQLServer helperSQLServer;

    public GestionPlanta(Context context) {
        helper = new ConexionHelperSQLite(context);
        helperSQLServer = new ConexionHelperSQLServer();
    }

    public String insertar(String id_planta, String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String estado, String fecha, String observaciones, int id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Planta", id_planta);
        cv.put("ID_Hilera", id_hilera);
        cv.put("ID_Cuartel", id_cuartel);
        cv.put("ID_Sector", id_sector);
        cv.put("ID_Potrero", id_potrero);
        cv.put("ID_Fundo", id_fundo);
        cv.put("Estado", estado);
        cv.put("Fecha_Plantacion", fecha);
        cv.put("Observaciones", observaciones);
        cv.put("ID_Mapeo", id_mapeo);
        data.insertWithOnConflict("Planta", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Planta Insertada!";
    }

    public String insertarSyncPlanta(String id_planta, String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String estado, String fecha, String observaciones, int id_mapeo, String operacionSQL) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Planta", id_planta);
        cv.put("ID_Hilera", id_hilera);
        cv.put("ID_Cuartel", id_cuartel);
        cv.put("ID_Sector", id_sector);
        cv.put("ID_Potrero", id_potrero);
        cv.put("ID_Fundo", id_fundo);
        cv.put("Estado", estado);
        cv.put("Fecha_Plantacion", fecha);
        cv.put("Observaciones", observaciones);
        cv.put("ID_Mapeo", id_mapeo);
        cv.put("OperacionSQL", operacionSQL);
        data.insertWithOnConflict("SyncPlanta", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Planta Insertada!";
    }

    public boolean insertarServer(String id_planta, String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String estado, String fecha, String observaciones, int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "insert into Planta values ('" + id_planta + "','" + id_hilera + "','" + id_cuartel + "','" + id_sector + "','" + id_potrero + "','" + id_fundo + "', '" + estado + "', '" + fecha + "', '" + observaciones + "', " + id_mapeo + ")";
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

    public String actualizar(String id_planta, String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String estado, String fecha, String observaciones, int id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Estado", estado);
        //cv.put("Fecha_Plantacion", fecha);
        cv.put("Observaciones", observaciones);
        data.update("Planta", cv, "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Planta='" + id_planta + "' AND ID_Mapeo='" + id_mapeo + "'", null);
        data.close();
        return "Planta Actualizada!";
    }

    public boolean actualizarServer(String id_planta, String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String estado, String fecha, String observaciones, int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "update Planta set Estado='" + estado + "', Observaciones='" + observaciones + "' where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' and ID_Sector='" + id_sector + "' and ID_Cuartel='" + id_cuartel + "' and ID_Hilera='" + id_hilera + "' AND ID_Planta='" + id_planta + "' AND ID_Mapeo='" + id_mapeo + "'";
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

    public String eliminar(String id_planta, String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, int id_mapeo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("Planta", "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Planta='" + id_planta + "' AND ID_Mapeo='" + id_mapeo + "'", null);
        data.close();
        return "Atención! Planta Eliminada";
    }

    public String limpiarSyncPlanta() {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("SyncPlanta", null, null);
        data.close();
        return "Atención! Plantas Eliminada";
    }

    public boolean eliminarServer(String id_planta, String id_hilera, String id_cuartel, String id_sector, String id_potrero, String id_fundo, String estado, String fecha, String observaciones, int id_mapeo) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "delete from Planta where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' and ID_Sector='" + id_sector + "' and ID_Cuartel='" + id_cuartel + "' and ID_Hilera='" + id_hilera + "' AND ID_Planta='" + id_planta + "' AND ID_Mapeo=" + id_mapeo + "";
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

    public ArrayList<String> getListaPlantas(String id_fundo, String id_potrero, String id_sector, String id_cuartel, String id_hilera, int id_mapeo) {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Planta where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Mapeo='" + id_mapeo + "' order by ID_Planta", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getString(4) + "," + cursor.getString(5) + "," + cursor.getString(6) + "," + cursor.getString(7) + "," + cursor.getString(8) + "," + cursor.getInt(9));
        }
        data.close();
        return lista;
    }

    public String getObservacion(String id_fundo, String id_potrero, String id_sector, String id_cuartel, String id_hilera, String id_planta, int id_mapeo) {
        String ob = "";
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select Observaciones from Planta where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Mapeo='" + id_mapeo + "' AND ID_Planta='" + id_planta + "'", null);
        while (cursor.moveToNext()) {
            ob = cursor.getString(0);
        }
        data.close();
        return ob;
    }

    public ArrayList<String> getSyncPlantas() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from SyncPlanta ORDER BY idSync", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getString(4) + "," + cursor.getString(5) + "," + cursor.getString(6) + "," + cursor.getString(7) + "," + cursor.getString(8) + "," + cursor.getInt(9) + "," + cursor.getString(10));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getAllPlantas() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Planta", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getString(4) + "," + cursor.getString(5) + "," + cursor.getString(6) + "," + cursor.getString(7) + "," + cursor.getString(8) + "," + cursor.getInt(9));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getListaPlantasServer(String id_fundo, String id_potrero, String id_sector, String id_cuartel, String id_hilera, int id_mapeo) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from Planta where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Mapeo='" + id_mapeo + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString(0) + "," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + "," + rs.getString(6) + "," + rs.getString(7) + "," + rs.getString(8) + "," + rs.getInt(9));
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
                String query = "select * from Planta";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Planta") + "," + rs.getString("ID_Hilera") + "," + rs.getString("ID_Cuartel") + "," + rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getString("Estado") + "," + rs.getString("Fecha_Plantacion") + "," + rs.getString("Observaciones") + "," + rs.getInt("ID_Mapeo"));
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
                String query = "select * from SyncPlanta ORDER BY idSync";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Planta") + "," + rs.getString("ID_Hilera") + "," + rs.getString("ID_Cuartel") + "," + rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getString("Estado") + "," + rs.getString("Fecha_Plantacion") + "," + rs.getString("Observaciones") + "," + rs.getInt("ID_Mapeo") + "," + rs.getString("OperacionSQL"));
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
                String query = "delete from SyncPlanta";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("server erro: " + ex.toString());
        }
    }

    public int getMaxPlanta(String id_fundo, String id_potrero, String id_sector, String id_cuartel, String id_hilera, int id_mapeo) {
        int maxPlanta = 0;
        ArrayList<Integer> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select ID_Planta from Planta where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "' AND ID_Hilera='" + id_hilera + "' AND ID_Mapeo='" + id_mapeo + "'", null);
        while (cursor.moveToNext()) {
            lista.add(Integer.parseInt(cursor.getString(0).replace("PL", "")));
        }
        data.close();
        if (!lista.isEmpty()) {
            maxPlanta = Collections.max(lista);
        }
        return maxPlanta;
    }
}
