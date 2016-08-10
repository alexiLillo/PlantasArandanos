package cl.lillo.plantasarandanos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cl.lillo.plantasarandanos.Conexion.ConexionHelperSQLite;
import cl.lillo.plantasarandanos.Conexion.Sync;
import cl.lillo.plantasarandanos.Gestion.GestionUsuario;

public class MainActivity extends AppCompatActivity {

    private GestionUsuario gestionUsuario;
    private Sync syncro;
    private EditText usuario, contraseña;
    private Context context;

    //private ConexionHelperSQLite helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestionUsuario = new GestionUsuario(this);
        syncro = new Sync();

        context = this;

        usuario = (EditText) findViewById(R.id.txtUsuario);
        contraseña = (EditText) findViewById(R.id.txtContraseña);

        //ATENCION! ESTAS LINEAS ELIMINAN LOS REGISTROS DE PLANTAS E HILERAS LOCALES
        //helper = new ConexionHelperSQLite(context);
        //SQLiteDatabase data = helper.getWritableDatabase();
        //data.delete("Hilera", null, null);
        //data.delete("Planta", "Estado=''", null);
        //data.close();

    }

    public void login(View view) {
        if (log() == 1) {
            //Toast.makeText(this, "Login OK ADMIN: ", Toast.LENGTH_SHORT).show();
            Globals global = Globals.getInstance();
            global.setData(true);
            Intent i = new Intent(this, SelectActivity.class);
            i.putExtra("usuario", usuario.getText().toString());
            i.putExtra("contraseña", contraseña.getText().toString());
            startActivity(i);
        } else {
            if (log() == 2) {
                //Toast.makeText(this, "Login OK NORMAL", Toast.LENGTH_SHORT).show();
                Globals global = Globals.getInstance();
                global.setData(true);
                Intent i = new Intent(this, SelectActivity.class);
                i.putExtra("usuario", usuario.getText().toString());
                i.putExtra("contraseña", contraseña.getText().toString());
                startActivity(i);
            } else {
                if (log() == 666) {
                    Toast.makeText(this, "Complete todos los campo!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Datos de inicio incorrectos!", Toast.LENGTH_SHORT).show();
                    usuario.setText("");
                    contraseña.setText("");
                }
            }
        }
    }

    public int log() {
        int log = 0;
        if (!usuario.getText().toString().isEmpty() || !contraseña.getText().toString().isEmpty()) {
            String cadena = gestionUsuario.getListaUsuarios().toString().substring(1, gestionUsuario.getListaUsuarios().toString().length() - 1).replace(" ", "");
            String[] split = cadena.split(",");
            for (int i = 0; i < split.length; i += 4) {
                if (split[i].equals(usuario.getText().toString()) && split[i + 1].equals(contraseña.getText().toString())) {
                    if (split[i + 2].equals("admin")) {
                        log = 1;
                    } else {
                        if (split[i + 2].equals("normal")) {
                            log = 2;
                        }
                    }
                }
            }
        } else {
            log = 666;
        }
        return log;
    }

    public void syncroCompleta(final View view) {

        AlertDialog.Builder alertSyncCompleta = new AlertDialog.Builder(this);
        alertSyncCompleta.setTitle("Sincronización completa");
        alertSyncCompleta.setMessage("¿Desea sincronizar todos los datos del sistema? Esta operación puede tardar varios minutos dependiendo de la cantidad de datos a sincronizar y la velocidad de su conexión a Internet.");
        alertSyncCompleta.setPositiveButton("Sincronizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //sync
                sync(view);
            }
        });

        alertSyncCompleta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertSyncCompleta.show();

    }

    public void sync(View view) {
        syncro.eventoSync(view, this, true);
    }

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    @Override
    public void onBackPressed() {
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Vuelva a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

    public void toWebApp(View view) {
        if (conectado(this)) {
            //Toast.makeText(this, "No disponible momentáneamente", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, WebActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Atención! No hay conexión a Internet", Toast.LENGTH_SHORT).show();
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
