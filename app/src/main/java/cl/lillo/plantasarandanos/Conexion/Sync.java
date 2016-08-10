package cl.lillo.plantasarandanos.Conexion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import cl.lillo.plantasarandanos.Gestion.GestionCuartel;
import cl.lillo.plantasarandanos.Gestion.GestionEstado;
import cl.lillo.plantasarandanos.Gestion.GestionFundo;
import cl.lillo.plantasarandanos.Gestion.GestionHilera;
import cl.lillo.plantasarandanos.Gestion.GestionMapeo;
import cl.lillo.plantasarandanos.Gestion.GestionPlanta;
import cl.lillo.plantasarandanos.Gestion.GestionPotrero;
import cl.lillo.plantasarandanos.Gestion.GestionSector;
import cl.lillo.plantasarandanos.Gestion.GestionUsuario;
import cl.lillo.plantasarandanos.Gestion.GestionVariedad;

/**
 * Created by Alexi on 04/07/2016.
 */
public class Sync {

    private GestionCuartel gestionCuartel;
    private GestionEstado gestionEstado;
    private GestionFundo gestionFundo;
    private GestionHilera gestionHilera;
    private GestionMapeo gestionMapeo;
    private GestionPlanta gestionPlanta;
    private GestionPotrero gestionPotrero;
    private GestionSector gestionSector;
    private GestionUsuario gestionUsuario;
    private GestionVariedad gestionVariedad;

    private Context contexto;
    private int lastMapeo;


    public String sync(Context context, boolean syncCompleta) {
        gestionUsuario = new GestionUsuario(context);
        gestionCuartel = new GestionCuartel(context);
        gestionEstado = new GestionEstado(context);
        gestionFundo = new GestionFundo(context);
        gestionHilera = new GestionHilera(context);
        gestionMapeo = new GestionMapeo(context);
        gestionPlanta = new GestionPlanta(context);
        gestionPotrero = new GestionPotrero(context);
        gestionSector = new GestionSector(context);
        gestionVariedad = new GestionVariedad(context);
        gestionMapeo = new GestionMapeo(context);


        lastMapeo = gestionMapeo.getLastMapeo();
        String mensaje = "";

        try {

            //Syncronizar todas las tablas
            if (syncCompleta) {
                //Select usuarios sqlServer insert sqlite
                ArrayList<String> listaUsuarios = gestionUsuario.getAllSyncServer();
                String idUsuario = "";
                String cadenaUsuario = listaUsuarios.toString().substring(1, gestionUsuario.getAllSyncServer().toString().length() - 1);
                if (!cadenaUsuario.isEmpty()) {
                    boolean correct = false;
                    String[] splitUsuario = cadenaUsuario.split(",");

                    try {
                        for (int i = 0; i < splitUsuario.length; i += 5) {
                            if (splitUsuario[i].startsWith(" ")) {
                                idUsuario = splitUsuario[i].substring(1);
                            } else {
                                idUsuario = splitUsuario[i];
                            }
                            if (splitUsuario[i + 4].equals("insert")) {
                                gestionUsuario.insertar(idUsuario, splitUsuario[i + 1], splitUsuario[i + 2], Integer.parseInt(splitUsuario[i + 3]));
                                correct = true;
                            }
                            if (splitUsuario[i + 4].equals("update")) {
                                gestionUsuario.actualizar(idUsuario, splitUsuario[i + 1], splitUsuario[i + 2], Integer.parseInt(splitUsuario[i + 3]));
                                correct = true;
                            }
                            if (splitUsuario[i + 4].equals("delete")) {
                                gestionUsuario.eliminar(idUsuario, splitUsuario[i + 1]);
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionUsuario.clearSyncServer();
                    }
                }

                //Select mapeo sqlServer insert sqlite
                ArrayList<String> listaMapeo = gestionMapeo.getAllSyncServer();
                String idMapeo = "";
                String cadenaMapeo = listaMapeo.toString().substring(1, gestionMapeo.getAllSyncServer().toString().length() - 1);
                if (!cadenaMapeo.isEmpty()) {
                    boolean correct = false;
                    String[] splitMapeo = cadenaMapeo.split(",");
                    try {
                        for (int i = 0; i < splitMapeo.length; i += 4) {
                            if (splitMapeo[i].startsWith(" ")) {
                                idMapeo = splitMapeo[i].substring(1);
                            } else {
                                idMapeo = splitMapeo[i];
                            }
                            if (splitMapeo[i + 3].equals("insert")) {
                                gestionMapeo.insertar(Integer.parseInt(idMapeo), splitMapeo[i + 1], splitMapeo[i + 2]);
                                correct = true;
                            }
                            if (splitMapeo[i + 3].equals("update")) {
                                gestionMapeo.actualizar(splitMapeo[i + 1], splitMapeo[i + 2], Integer.parseInt(idMapeo));
                                correct = true;
                            }
                            if (splitMapeo[i + 3].equals("delete")) {
                                gestionMapeo.eliminar(idMapeo);
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionMapeo.clearSyncServer();
                    }
                }

                //Select ESTADO sqlServer insert sqlite
                ArrayList<String> listaEstado = gestionEstado.getAllSyncServer();
                String idEstado = "";
                String cadenaEstado = listaEstado.toString().substring(1, gestionEstado.getAllSyncServer().toString().length() - 1);
                if (!cadenaEstado.isEmpty()) {
                    boolean correct = false;
                    String[] splitEstado = cadenaEstado.split(",");
                    try {
                        for (int i = 0; i < splitEstado.length; i += 2) {
                            if (splitEstado[i].startsWith(" ")) {
                                idEstado = splitEstado[i].substring(1);
                            } else {
                                idEstado = splitEstado[i];
                            }
                            if (splitEstado[i + 1].equals("insert")) {
                                gestionEstado.insertar(idEstado);
                                correct = true;
                            } else {
                                if (splitEstado[i + 1].equals("delete")) {
                                    gestionEstado.eliminar(idEstado);
                                    correct = true;
                                } else {
                                    gestionEstado.actualizar(idEstado, splitEstado[i + 1]);
                                    correct = true;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionEstado.clearSyncServer();
                    }
                }

                //Select variedad sqlServer insert sqlite
                ArrayList<String> listaVariedad = gestionVariedad.getAllSyncServer();
                String idVariedad = "";
                String cadenaVariedad = listaVariedad.toString().substring(1, gestionVariedad.getAllSyncServer().toString().length() - 1);
                if (!cadenaVariedad.isEmpty()) {
                    boolean correct = false;
                    String[] splitVariedad = cadenaVariedad.split(",");
                    try {
                        for (int i = 0; i < splitVariedad.length; i += 4) {
                            if (splitVariedad[i].startsWith(" ")) {
                                idVariedad = splitVariedad[i].substring(1);
                            } else {
                                idVariedad = splitVariedad[i];
                            }
                            if (splitVariedad[i + 3].equals("insert")) {
                                gestionVariedad.insertar(idVariedad, splitVariedad[i + 1], splitVariedad[i + 2]);
                                correct = true;
                            }
                            if (splitVariedad[i + 3].equals("update")) {
                                gestionVariedad.actualizar(idVariedad, splitVariedad[i + 1], splitVariedad[i + 2]);
                                correct = true;
                            }
                            if (splitVariedad[i + 3].equals("delete")) {
                                gestionVariedad.eliminar(idVariedad, splitVariedad[i + 2]);
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionVariedad.clearSyncServer();
                    }
                }

                //Select fundo sqlServer insert sqlite
                ArrayList<String> listaFundo = gestionFundo.getAllSyncServer();
                String idFundo = "";
                String cadenaFundo = listaFundo.toString().substring(1, gestionFundo.getAllSyncServer().toString().length() - 1);
                if (!cadenaFundo.isEmpty()) {
                    boolean correct = false;
                    String[] splitFundo = cadenaFundo.split(",");
                    try {
                        for (int i = 0; i < splitFundo.length; i += 3) {
                            if (splitFundo[i].startsWith(" ")) {
                                idFundo = splitFundo[i].substring(1);
                            } else {
                                idFundo = splitFundo[i];
                            }
                            if (splitFundo[i + 2].equals("insert")) {
                                gestionFundo.insertar(idFundo, splitFundo[i + 1]);
                                correct = true;
                            }
                            if (splitFundo[i + 2].equals("update")) {
                                gestionFundo.actualizar(idFundo, splitFundo[i + 1]);
                                correct = true;
                            }
                            if (splitFundo[i + 2].equals("delete")) {
                                gestionFundo.eliminar(idFundo);
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionFundo.clearSyncServer();
                    }
                }

                //Select Potrero sqlServer insert sqlite
                ArrayList<String> listaPotrero = gestionPotrero.getAllSyncServer();
                String idPotrero = "";
                String cadenaPotrero = listaPotrero.toString().substring(1, gestionPotrero.getAllSyncServer().toString().length() - 1);
                if (!cadenaPotrero.isEmpty()) {
                    boolean correct = false;
                    String[] splitPotrero = cadenaPotrero.split(",");
                    try {
                        for (int i = 0; i < splitPotrero.length; i += 4) {
                            if (splitPotrero[i].startsWith(" ")) {
                                idPotrero = splitPotrero[i].substring(1);
                            } else {
                                idPotrero = splitPotrero[i];
                            }
                            if (splitPotrero[i + 3].equals("insert")) {
                                gestionPotrero.insertar(idPotrero, splitPotrero[i + 1], splitPotrero[i + 2]);
                                correct = true;
                            }
                            if (splitPotrero[i + 3].equals("update")) {
                                gestionPotrero.actualizar(idPotrero, splitPotrero[i + 1], splitPotrero[i + 2]);
                                correct = true;
                            }
                            if (splitPotrero[i + 3].equals("delete")) {
                                gestionPotrero.eliminar(idPotrero, splitPotrero[i + 1]);
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionPotrero.clearSyncServer();
                    }
                }

                //Select Sector sqlServer insert sqlite
                ArrayList<String> listaSector = gestionSector.getAllSyncServer();
                String idSector = "";
                String cadenaSector = listaSector.toString().substring(1, gestionSector.getAllSyncServer().toString().length() - 1);
                if (!cadenaSector.isEmpty()) {
                    boolean correct = false;
                    String[] splitSector = cadenaSector.split(",");
                    try {
                        for (int i = 0; i < splitSector.length; i += 5) {
                            if (splitSector[i].startsWith(" ")) {
                                idSector = splitSector[i].substring(1);
                            } else {
                                idSector = splitSector[i];
                            }
                            if (splitSector[i + 4].equals("insert")) {
                                gestionSector.insertar(idSector, splitSector[i + 1], splitSector[i + 2], splitSector[i + 3]);
                                correct = true;
                            }
                            if (splitSector[i + 4].equals("update")) {
                                gestionSector.actualizar(idSector, splitSector[i + 1], splitSector[i + 2], splitSector[i + 3]);
                                correct = true;
                            }
                            if (splitSector[i + 4].equals("delete")) {
                                gestionSector.eliminar(idSector, splitSector[i + 1], splitSector[i + 2]);
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionSector.clearSyncServer();
                    }
                }

                //Select Cuartel sqlServer insert sqlite
                ArrayList<String> listaCuartel = gestionCuartel.getAllSyncServer();
                String idCuartel = "";
                String cadenaCuartel = listaCuartel.toString().substring(1, gestionCuartel.getAllSyncServer().toString().length() - 1);
                if (!cadenaCuartel.isEmpty()) {
                    boolean correct = false;
                    String[] splitCuartel = cadenaCuartel.split(",");
                    try {
                        for (int i = 0; i < splitCuartel.length; i += 7) {
                            if (splitCuartel[i].startsWith(" ")) {
                                idCuartel = splitCuartel[i].substring(1);
                            } else {
                                idCuartel = splitCuartel[i];
                            }
                            if (splitCuartel[i + 6].equals("insert")) {
                                gestionCuartel.insertar(idCuartel, splitCuartel[i + 1], splitCuartel[i + 2], splitCuartel[i + 3], Float.parseFloat(splitCuartel[i + 4]), splitCuartel[i + 5]);
                                correct = true;
                            }
                            if (splitCuartel[i + 6].equals("update")) {
                                gestionCuartel.actualizar(idCuartel, splitCuartel[i + 1], splitCuartel[i + 2], splitCuartel[i + 3], Float.parseFloat(splitCuartel[i + 4]), splitCuartel[i + 5]);
                                correct = true;
                            }
                            if (splitCuartel[i + 6].equals("delete")) {
                                gestionCuartel.eliminar(idCuartel, splitCuartel[i + 1], splitCuartel[i + 2], splitCuartel[i + 3]);
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionCuartel.clearSyncServer();
                    }
                }

                //Select Hilera sqlServer insert sqlite
                ArrayList<String> listaHilera = gestionHilera.getAllSyncServer();
                String idHilera = "";
                String cadenaHilera = listaHilera.toString().substring(1, gestionHilera.getAllSyncServer().toString().length() - 1);
                if (!cadenaHilera.isEmpty()) {
                    boolean correct = false;
                    String[] splitHilera = cadenaHilera.split(",");
                    try {
                        for (int i = 0; i < splitHilera.length; i += 8) {
                            if (splitHilera[i].startsWith(" ")) {
                                idHilera = splitHilera[i].substring(1);
                            } else {
                                idHilera = splitHilera[i];
                            }
                            if (splitHilera[i + 7].equals("insert")) {
                                gestionHilera.insertar(idHilera, splitHilera[i + 1], splitHilera[i + 2], splitHilera[i + 3], splitHilera[i + 4], splitHilera[i + 5], Integer.parseInt(splitHilera[i + 6]));
                                correct = true;
                            }
                            if (splitHilera[i + 7].equals("update")) {
                                gestionHilera.actualizar(idHilera, splitHilera[i + 1], splitHilera[i + 2], splitHilera[i + 3], splitHilera[i + 4], splitHilera[i + 5], Integer.parseInt(splitHilera[i + 6]));
                                correct = true;
                            }
                            if (splitHilera[i + 7].equals("delete")) {
                                gestionHilera.eliminar(idHilera, splitHilera[i + 1], splitHilera[i + 2], splitHilera[i + 3], splitHilera[i + 4], Integer.parseInt(splitHilera[i + 6]));
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionHilera.clearSyncServer();
                    }
                }

                //Select Planta sqlServer insert sqlite
                ArrayList<String> listaPlanta = gestionPlanta.getAllSyncServer();
                String idPlanta = "";
                String cadenaPlanta = listaPlanta.toString().substring(1, gestionPlanta.getAllSyncServer().toString().length() - 1);
                if (!cadenaPlanta.isEmpty()) {
                    boolean correct = false;
                    String[] splitPlanta = cadenaPlanta.split(",");
                    try {
                        for (int i = 0; i < splitPlanta.length; i += 11) {
                            if (splitPlanta[i].startsWith(" ")) {
                                idPlanta = splitPlanta[i].substring(1);
                            } else {
                                idPlanta = splitPlanta[i];
                            }
                            if (splitPlanta[i + 10].equals("insert")) {
                                gestionPlanta.insertar(idPlanta, splitPlanta[i + 1], splitPlanta[i + 2], splitPlanta[i + 3], splitPlanta[i + 4], splitPlanta[i + 5], splitPlanta[i + 6], splitPlanta[i + 7], splitPlanta[i + 8], Integer.parseInt(splitPlanta[i + 9]));
                                correct = true;
                            }
                            if (splitPlanta[i + 10].equals("update")) {
                                gestionPlanta.actualizar(idPlanta, splitPlanta[i + 1], splitPlanta[i + 2], splitPlanta[i + 3], splitPlanta[i + 4], splitPlanta[i + 5], splitPlanta[i + 6], splitPlanta[i + 7], splitPlanta[i + 8], Integer.parseInt(splitPlanta[i + 9]));
                                correct = true;
                            }
                            if (splitPlanta[i + 10].equals("delete")) {
                                gestionPlanta.eliminar(idPlanta, splitPlanta[i + 1], splitPlanta[i + 2], splitPlanta[i + 3], splitPlanta[i + 4], splitPlanta[i + 5], Integer.parseInt(splitPlanta[i + 9]));
                                correct = true;
                            }
                        }
                    } catch (Exception ex) {
                        correct = false;
                        System.out.println(ex.toString());
                    }
                    if (correct) {
                        gestionPlanta.clearSyncServer();
                    }
                }
            }

            //consultar Hileras y plantas actualizadas o eliminadas para realizar modificacione en el server
            //sincronizar hileras pendientes en sql server
            ArrayList<String> listaHilerasSync = gestionHilera.getSyncHilera();
            String idHileraSync = "";
            String cadenaHileraSync = listaHilerasSync.toString().substring(1, gestionHilera.getSyncHilera().toString().length() - 1);
            if (!cadenaHileraSync.isEmpty()) {
                boolean correct = false;
                String[] splitHileraSync = cadenaHileraSync.split(",");

                try {
                    for (int i = 0; i < splitHileraSync.length; i += 8) {
                        if (splitHileraSync[i].startsWith(" ")) {
                            idHileraSync = splitHileraSync[i].substring(1);
                        } else {
                            idHileraSync = splitHileraSync[i];
                        }
                        if (splitHileraSync[i + 7].equals("insert")) {
                            gestionHilera.insertarServer(idHileraSync, splitHileraSync[i + 1], splitHileraSync[i + 2], splitHileraSync[i + 3], splitHileraSync[i + 4], splitHileraSync[i + 5], Integer.parseInt(splitHileraSync[+6]));
                            correct = true;
                        }
                        if (splitHileraSync[i + 7].equals("update")) {
                            gestionHilera.actualizarServer(idHileraSync, splitHileraSync[i + 1], splitHileraSync[i + 2], splitHileraSync[i + 3], splitHileraSync[i + 4], splitHileraSync[i + 5], Integer.parseInt(splitHileraSync[+6]));
                            correct = true;
                        }
                        if (splitHileraSync[i + 7].equals("delete")) {
                            gestionHilera.eliminarServer(idHileraSync, splitHileraSync[i + 1], splitHileraSync[i + 2], splitHileraSync[i + 3], splitHileraSync[i + 4], Integer.parseInt(splitHileraSync[+6]));
                            correct = true;
                        }
                    }
                } catch (Exception ex) {
                    correct = false;
                    System.out.println(ex.toString());
                }

                if (correct) {
                    gestionHilera.limpiarSyncHilera();
                }
            }

            //sincronizar plantas pendientes en sql server
            ArrayList<String> listaPlantasSync = gestionPlanta.getSyncPlantas();
            String idPlantasSync = "";
            String cadenaplantasSync = listaPlantasSync.toString().substring(1, gestionPlanta.getSyncPlantas().toString().length() - 1);
            if (!cadenaplantasSync.isEmpty()) {
                boolean correct = false;
                String[] splitPlantasSync = cadenaplantasSync.split(",");

                try {
                    for (int i = 0; i < splitPlantasSync.length; i += 11) {
                        if (splitPlantasSync[i].startsWith(" ")) {
                            idPlantasSync = splitPlantasSync[i].substring(1);
                        } else {
                            idPlantasSync = splitPlantasSync[i];
                        }
                        if (splitPlantasSync[i + 10].equals("insert")) {
                            gestionPlanta.insertarServer(idPlantasSync, splitPlantasSync[i + 1], splitPlantasSync[i + 2], splitPlantasSync[i + 3], splitPlantasSync[i + 4], splitPlantasSync[i + 5], splitPlantasSync[i + 6], splitPlantasSync[i + 7], splitPlantasSync[i + 8], Integer.parseInt(splitPlantasSync[i + 9]));
                            correct = true;
                        }
                        if (splitPlantasSync[i + 10].equals("update")) {
                            gestionPlanta.actualizarServer(idPlantasSync, splitPlantasSync[i + 1], splitPlantasSync[i + 2], splitPlantasSync[i + 3], splitPlantasSync[i + 4], splitPlantasSync[i + 5], splitPlantasSync[i + 6], splitPlantasSync[i + 7], splitPlantasSync[i + 8], Integer.parseInt(splitPlantasSync[i + 9]));
                            correct = true;
                        }
                        if (splitPlantasSync[i + 10].equals("delete")) {
                            gestionPlanta.eliminarServer(idPlantasSync, splitPlantasSync[i + 1], splitPlantasSync[i + 2], splitPlantasSync[i + 3], splitPlantasSync[i + 4], splitPlantasSync[i + 5], splitPlantasSync[i + 6], splitPlantasSync[i + 7], splitPlantasSync[i + 8], Integer.parseInt(splitPlantasSync[i + 9]));
                            correct = true;
                        }
                    }

                } catch (Exception ex) {
                    correct = false;
                    System.out.println(ex.toString());
                }

                if (correct) {
                    gestionPlanta.limpiarSyncPlanta();
                }
            }


            mensaje = "Sincronización correcta";
        } catch (Exception ex) {
            mensaje = "Error! No se pudo sincronizar";
            System.out.println("exeption sync: " + ex.toString());
        }
        return mensaje;
    }

    public void eventoSync(View view, Activity activity, boolean syncCompleta) {
        //llamar servicio que sincroniza "bajo cuerda"
        contexto = view.getContext();
        if (conectado(contexto)) {
            ProgressDialog progress = new ProgressDialog(contexto);
            progress.setMessage("Sincronizando, por favor espere...");
            new Servicio(progress, activity, syncCompleta).execute();
        } else {
            Toast.makeText(contexto, "Atención! No hay conexión a Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public class Servicio extends AsyncTask<String, Void, String> {

        private String msj;
        private boolean syncCompleta;
        ProgressDialog progress;
        Activity act;

        public Servicio(ProgressDialog progress, Activity act, boolean syncCompleta) {
            this.progress = progress;
            this.act = act;
            this.syncCompleta = syncCompleta;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            msj = sync(contexto, syncCompleta);
            return msj;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss();
            Toast.makeText(contexto, result, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean conectado(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }
}
