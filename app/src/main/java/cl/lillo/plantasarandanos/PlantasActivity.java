package cl.lillo.plantasarandanos;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cl.lillo.plantasarandanos.Conexion.Sync;
import cl.lillo.plantasarandanos.Gestion.GestionEstado;
import cl.lillo.plantasarandanos.Gestion.GestionMapeo;
import cl.lillo.plantasarandanos.Gestion.GestionPlanta;

public class PlantasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GestionPlanta gestionPlanta;
    private GestionMapeo gestionMapeo;
    private GestionEstado gestionEstado;
    private Sync syncro;
    private ListView listPlant;
    private String fundo = "";
    private String potrero = "";
    private String sector = "";
    private String cuartel = "";
    private String hilera = "";
    private String titulo = "";
    private int lastMapeo = 0;

    private String usuario, contraseña;

    private ImageButton btsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantas);


        listPlant = (ListView) findViewById(R.id.listPlant);
        gestionPlanta = new GestionPlanta(this);
        gestionMapeo = new GestionMapeo(this);
        gestionEstado = new GestionEstado(this);
        syncro = new Sync();
        Bundle bundle = this.getIntent().getExtras();
        fundo = bundle.getString("fundo");
        potrero = bundle.getString("potrero");
        sector = bundle.getString("sector");
        cuartel = bundle.getString("cuartel");
        hilera = bundle.getString("hilera");
        titulo = bundle.getString("titulo");
        usuario = bundle.getString("usuario");
        contraseña = bundle.getString("contraseña");
        lastMapeo = gestionMapeo.getLastMapeo();

        btsync = (ImageButton) findViewById(R.id.btSync);

        toolbar = (Toolbar) findViewById(R.id.toolbarPlantas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //regresar...
                eventoVolver(view);
            }
        });
        toolbar.setTitle(titulo);

        Globals global = Globals.getInstance();
        if (global.getData()) {
            syncro.eventoSync(toolbar.getRootView(), this, false);
            global.setData(false);
        }

        cargarListPlant(fundo, potrero, sector, cuartel, hilera, lastMapeo);

        listPlant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //actualizarPlanta(id_planta, estado, observaciones);
                String[] split1 = listPlant.getItemAtPosition(position).toString().split(":");
                String[] split2 = split1[1].split(" ");
                String[] split3 = split1[2].split(" ");
                String id_planta = split2[1];
                String estado = split3[1];
                String observaciones = gestionPlanta.getObservacion(fundo, potrero, sector, cuartel, hilera, id_planta, lastMapeo);

                actualizarPlanta(id_planta, estado, observaciones);
            }
        });

        listPlant.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String[] split1 = listPlant.getItemAtPosition(position).toString().split(":");
                String[] split2 = split1[1].split(" ");
                String id_planta = split2[1];
                eliminarPlanta(id_planta);
                return true;
            }
        });

        btsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncro.eventoSync(v, PlantasActivity.this, false);
                //cargarListPlant(fundo, potrero, sector, cuartel, hilera, lastMapeo);
            }
        });
    }

    public void porejecutar() {
        cargarListPlant(fundo, potrero, sector, cuartel, hilera, lastMapeo);
    }

    public void cargarListPlant(String f, String p, String s, String c, String h, int m) {
        if (!fundo.equals("") && !potrero.equals("") && !sector.equals("") && !cuartel.equals("") && !hilera.equals("")) {
            try {
                ArrayList<String> list = new ArrayList<>();
                String cadena = gestionPlanta.getListaPlantas(f, p, s, c, h, m).toString().substring(1, gestionPlanta.getListaPlantas(f, p, s, c, h, m).toString().length() - 1);
                String[] split = cadena.split(",");
                for (int i = 0; i < split.length; i += 10) {
                    if (split[i + 8].isEmpty() || split[i + 8].equals("") || split[i + 8].equals(" ")) {
                        list.add("ID: " + split[i].replace(" ", "") + "            Estado: " + split[i + 6] + "            Observaciones: NO");
                    } else {
                        list.add("ID: " + split[i].replace(" ", "") + "            Estado: " + split[i + 6] + "            Observaciones: SI");
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                listPlant.setAdapter(adapter);
            } catch (Exception ex) {
                Toast.makeText(this, "No existen plantas en esta hilera, por favor agregue plantas.", Toast.LENGTH_LONG).show();
                ArrayList<String> list = new ArrayList<>();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                listPlant.setAdapter(adapter);
                System.out.println(ex.toString());
            }
        }
    }

    public InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                //if (!Character.isLetterOrDigit(source.charAt(i))) {
                if (Character.toString(source.charAt(i)).equals(",")) {
                    return "";
                }
            }
            return null;
        }
    };

    public InputFilter filter2 = new InputFilter.LengthFilter(249);

    public void agregarPlanta(View view) {
        if (fundo.equals("0") || potrero.equals("0") || sector.equals("0") || cuartel.equals("0")) {
            Toast.makeText(this, "", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder alertAgregarPlanta = new AlertDialog.Builder(view.getContext());
            alertAgregarPlanta.setTitle("Agregar Planta");
            alertAgregarPlanta.setMessage("Seleccione estado, agrege observaciones y seleccione fecha de plantación la nueva planta:");
            LinearLayout layout = new LinearLayout(view.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout layout2 = new LinearLayout(view.getContext());
            final TextView texto = new TextView(view.getContext());
            texto.setText("Estado: ");
            texto.setTextSize(20);
            texto.setPadding(16, 16, 16, 16);
            final Spinner estado = new Spinner(view.getContext());
            estado.setPadding(16, 16, 16, 16);
            final EditText observaciones = new EditText(view.getContext());
            observaciones.setPadding(16, 16, 16, 16);
            observaciones.setHint("Observaciones: (opcionales)");
            observaciones.setFilters(new InputFilter[]{filter, filter2});
            final DatePicker fecha = new DatePicker(view.getContext());
            fecha.setPadding(16, 16, 16, 16);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gestionEstado.getListaEstados());
            estado.setAdapter(adapter);
            layout2.addView(texto);
            layout2.addView(estado);
            layout.addView(layout2);
            layout.addView(observaciones);
            layout.addView(fecha);

            alertAgregarPlanta.setView(layout, 16, 16, 16, 16);
            alertAgregarPlanta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //AGREGAR planta SIGUIENTE A LA planta ANTERIOR
                    try {
                        int day = fecha.getDayOfMonth();
                        int month = fecha.getMonth() + 1;
                        int year = fecha.getYear();
                        String date = year + "-" + month + "-" + day;

                        int planta = gestionPlanta.getMaxPlanta(fundo, potrero, sector, cuartel, hilera, lastMapeo) + 1;
                        String id_planta;
                        if (planta < 10 && planta >= 1) {
                            id_planta = "PL000" + planta;
                        } else {
                            if (planta < 100 && planta >= 10) {
                                id_planta = "PL00" + planta;
                            } else {
                                if (planta < 1000 && planta >= 100) {
                                    id_planta = "PL0" + planta;
                                } else {
                                    id_planta = "PL" + planta;
                                }
                            }
                        }

                        gestionPlanta.insertar(id_planta, hilera, cuartel, sector, potrero, fundo, estado.getSelectedItem().toString(), date, observaciones.getText().toString().toUpperCase(), lastMapeo);
                        gestionPlanta.insertarSyncPlanta(id_planta, hilera, cuartel, sector, potrero, fundo, estado.getSelectedItem().toString(), date, observaciones.getText().toString().toUpperCase(), lastMapeo, "insert");
                        Toast.makeText(getApplicationContext(), "Planta agregada", Toast.LENGTH_SHORT).show();
                        cargarListPlant(fundo, potrero, sector, cuartel, hilera, lastMapeo);
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "ERROR! No se pudo agregar planta.", Toast.LENGTH_SHORT).show();
                        System.out.println(ex.toString());
                    }
                }
            });

            alertAgregarPlanta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertAgregarPlanta.create();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            alertDialog.show();
        }
    }

    public void actualizarPlanta(final String id_planta, String oldEstado, String oldObservaciones) {
        final Context context = this;
        AlertDialog.Builder alertActualizarPlanta = new AlertDialog.Builder(this);
        alertActualizarPlanta.setTitle("Acuatilzar Planta");
        alertActualizarPlanta.setMessage("Seleccione estado y agrege observaciones para la planta seleccionada:");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout layout2 = new LinearLayout(this);
        final TextView texto = new TextView(this);
        texto.setText("Estado: ");
        texto.setTextSize(20);
        texto.setPadding(16, 16, 16, 16);
        final Spinner estado = new Spinner(this);
        estado.setPadding(16, 16, 16, 16);
        final EditText observaciones = new EditText(this);
        observaciones.setFilters(new InputFilter[]{filter, filter2});
        observaciones.setPadding(16, 16, 16, 16);
        observaciones.setHint("Observaciones: (opcionales)");
        observaciones.setText(oldObservaciones);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gestionEstado.getListaEstados());
        estado.setAdapter(adapter);
        estado.setSelection(adapter.getPosition(oldEstado));

        layout2.addView(texto);
        layout2.addView(estado);
        layout.addView(layout2);
        layout.addView(observaciones);
        alertActualizarPlanta.setView(layout, 16, 16, 16, 16);
        alertActualizarPlanta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //actualizar planta
                try {
                    gestionPlanta.actualizar(id_planta, hilera, cuartel, sector, potrero, fundo, estado.getSelectedItem().toString(), "", observaciones.getText().toString().toUpperCase(), lastMapeo);
                    gestionPlanta.insertarSyncPlanta(id_planta, hilera, cuartel, sector, potrero, fundo, estado.getSelectedItem().toString(), "", observaciones.getText().toString().toUpperCase(), lastMapeo, "update");
                    Toast.makeText(context, "Datos de planta actualizados.", Toast.LENGTH_SHORT).show();
                    cargarListPlant(fundo, potrero, sector, cuartel, hilera, lastMapeo);
                } catch (Exception ex) {
                    Toast.makeText(context, "ERROR! No se pudo actualizar planta.", Toast.LENGTH_SHORT).show();
                    System.out.println(ex.toString());
                }
            }
        });

        alertActualizarPlanta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertActualizarPlanta.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();
    }

    public void eliminarPlanta(final String id_planta) {
        final Context context = this;
        AlertDialog.Builder alertEliminarPlanta = new AlertDialog.Builder(this);
        alertEliminarPlanta.setTitle("Eliminar Planta");
        alertEliminarPlanta.setMessage("¿Está seguro de que desea eliminar la planta: " + id_planta + "?");
        alertEliminarPlanta.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //eliminar planta
                try {
                    int maxplanta = gestionPlanta.getMaxPlanta(fundo, potrero, sector, cuartel, hilera, lastMapeo);
                    if (Integer.parseInt(id_planta.replace("PL", ""), 10) == maxplanta) {
                        gestionPlanta.eliminar(id_planta, hilera, cuartel, sector, potrero, fundo, lastMapeo);
                        gestionPlanta.insertarSyncPlanta(id_planta, hilera, cuartel, sector, potrero, fundo, "", "", "", lastMapeo, "delete");
                        Toast.makeText(context, "Planta eliminada.", Toast.LENGTH_SHORT).show();
                        cargarListPlant(fundo, potrero, sector, cuartel, hilera, lastMapeo);
                    } else {
                        Toast.makeText(context, "ATENCIÓN! Solo se puede eliminar la última planta", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "ERROR! No se pudo eliminar planta.", Toast.LENGTH_SHORT).show();
                    System.out.println(ex.toString());
                }
            }
        });

        alertEliminarPlanta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertEliminarPlanta.show();
    }

    public void eventoVolver(View view) {
        Globals global = Globals.getInstance();
        global.setData(true);
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Globals global = Globals.getInstance();
        global.setData(true);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Globals global = Globals.getInstance();
        if (global.getData()) {
            syncro.eventoSync(toolbar.getRootView(), this, false);
            global.setData(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Globals global = Globals.getInstance();
        if (global.getData()) {
            syncro.eventoSync(toolbar.getRootView(), this, false);
            global.setData(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Globals global = Globals.getInstance();
        global.setData(true);
    }

}
