package cl.lillo.plantasarandanos.Gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import cl.lillo.plantasarandanos.Conexion.ConexionHelperSQLServer;
import cl.lillo.plantasarandanos.Conexion.ConexionHelperSQLite;

/**
 * Created by Alexi on 29/06/2016.
 */
public class GestionCuartel {
    private ConexionHelperSQLite helper;
    private ConexionHelperSQLServer helperSQLServer;

    public GestionCuartel(Context context) {
        helper = new ConexionHelperSQLite(context);
        helperSQLServer = new ConexionHelperSQLServer();
    }

    public String insertar(String id_cuartel, String id_sector, String id_potrero, String id_fundo, float superficie, String nombre) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID_Fundo", id_fundo);
        cv.put("ID_Potrero", id_potrero);
        cv.put("ID_Sector", id_sector);
        cv.put("ID_Cuartel", id_cuartel);
        cv.put("Superficie", superficie);
        cv.put("Nombre", nombre);
        data.insertWithOnConflict("Cuartel", null, cv,SQLiteDatabase.CONFLICT_IGNORE);
        data.close();
        return "Cuartel Insertado!";
    }

    public boolean insertarServer(String id_fundo, String id_potrero, String id_sector, String id_cuartel, float superficie, String nombre) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "insert into Cuartel values ('" + id_cuartel + "','" + id_sector + "','" + id_potrero + "','" + id_fundo + "'," + superficie + ",'" + nombre + "')";
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

    public String actualizar(String id_cuartel, String id_sector, String id_potrero, String id_fundo, float superficie, String nombre) {
        SQLiteDatabase data = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Superficie", superficie);
        cv.put("Nombre", nombre);
        data.update("Cuartel", cv, "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "'", null);
        data.close();
        return "Cuartel Actualizado!";
    }

    public boolean actualizarServer(String id_fundo, String id_potrero, String id_sector, String id_cuartel, float superficie, String nombre) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "update Cuartel set Superficie='" + superficie + "', Nombre='" + nombre + "'  where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' and ID_Sector='" + id_sector + "' and ID_Cuartel='" + id_cuartel + "'";
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

    public String eliminar(String id_cuartel, String id_sector, String id_potrero, String id_fundo) {
        SQLiteDatabase data = helper.getWritableDatabase();
        data.delete("Cuartel", "ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "' AND ID_Cuartel='" + id_cuartel + "'", null);
        data.close();
        return "Atención! Cuartel Eliminado";
    }

    public boolean eliminarServer(String id_fundo, String id_potrero, String id_sector, String id_cuartel) {
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {
                return false;
            } else {
                //Consulta SQL
                String query = "delete from Cuartel where ID_Fundo='" + id_fundo + "' and ID_Potrero='" + id_potrero + "' and ID_Sector='" + id_sector + "' and ID_Cuartel='" + id_cuartel + "'";
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

    public ArrayList<String> getListaCuarteles(String id_fundo, String id_potrero, String id_sector) {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase data = helper.getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from Cuartel where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "'", null);
        while (cursor.moveToNext()) {
            lista.add(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3) + "," + cursor.getFloat(4) + "," + cursor.getString(5));
        }
        data.close();
        return lista;
    }

    public ArrayList<String> getListaCuartelesServer(String id_fundo, String id_potrero, String id_sector) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = helperSQLServer.CONN();
            if (con == null) {

            } else {
                //Consulta SQL
                String query = "select * from Cuartel where ID_Fundo='" + id_fundo + "' AND ID_Potrero='" + id_potrero + "' AND ID_Sector='" + id_sector + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString(0) + "," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getFloat(4) + "," + rs.getString(5));
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
                String query = "select * from Cuartel";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Cuartel") + "," + rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getFloat("Superficie") + "," + rs.getString("Nombre"));
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
                String query = "select * from SyncCuartel ORDER BY idSync";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lista.add(rs.getString("ID_Cuartel") + "," + rs.getString("ID_Sector") + "," + rs.getString("ID_Potrero") + "," + rs.getString("ID_Fundo") + "," + rs.getFloat("Superficie") + "," + rs.getString("Nombre") + "," + rs.getString("OperacionSQL"));
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
                String query = "delete from SyncCuartel";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
            }
        } catch (Exception ex) {
            System.out.println("server erro: " + ex.toString());
        }
    }
}
