package cl.lillo.plantasarandanos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cl.lillo.plantasarandanos.Conexion.Sync;
import cl.lillo.plantasarandanos.Gestion.GestionCuartel;
import cl.lillo.plantasarandanos.Gestion.GestionFundo;
import cl.lillo.plantasarandanos.Gestion.GestionHilera;
import cl.lillo.plantasarandanos.Gestion.GestionMapeo;
import cl.lillo.plantasarandanos.Gestion.GestionPotrero;
import cl.lillo.plantasarandanos.Gestion.GestionSector;
import cl.lillo.plantasarandanos.Gestion.GestionVariedad;

public class SelectActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private ImageButton btEliminar;
    private GestionFundo gestionFundo;
    private GestionPotrero gestionPotrero;
    private GestionSector gestionSector;
    private GestionCuartel gestionCuartel;
    private GestionHilera gestionHilera;
    private GestionVariedad gestionVariedad;
    private GestionMapeo gestionMapeo;
    private Spinner ddFundo, ddPotrero, ddSector, ddCuartel, ddHilera;
    private String fundo = "0";
    private String potrero = "0";
    private String sector = "0";
    private String cuartel = "0";
    private String hilera = "0";
    private String nombreFundo, nombrePotrero, nombreSector, nombreCuartel, nombreHilera;
    private String usuario, contraseña;
    private Sync syncro;
    private int lastMapeo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Bundle bundle = this.getIntent().getExtras();
        //usuario = bundle.getString("usuario");
        //contraseña = bundle.getString("contraseña");

        syncro = new Sync();

        context = this;
        gestionFundo = new GestionFundo(this);
        gestionPotrero = new GestionPotrero(this);
        gestionSector = new GestionSector(this);
        gestionCuartel = new GestionCuartel(this);
        gestionHilera = new GestionHilera(this);
        gestionVariedad = new GestionVariedad(this);
        gestionMapeo = new GestionMapeo(this);

        ddFundo = (Spinner) findViewById(R.id.ddFundo);
        ddPotrero = (Spinner) findViewById(R.id.ddPotrero);
        ddSector = (Spinner) findViewById(R.id.ddSector);
        ddCuartel = (Spinner) findViewById(R.id.ddCuartel);
        ddHilera = (Spinner) findViewById(R.id.ddHilera);
        btEliminar = (ImageButton) findViewById(R.id.btDeleteHilera);
        lastMapeo = gestionMapeo.getLastMapeo();

        toolbar = (Toolbar) findViewById(R.id.toolbarSelect);
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

        cargarDDfundos();

        ddFundo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!ddFundo.getItemAtPosition(position).toString().equals("Seleccionar...")) {
                    String[] split1 = ddFundo.getItemAtPosition(position).toString().split(":");
                    String[] split2 = split1[1].split(" ");
                    fundo = split2[1];
                    nombreFundo = split1[2] + ">";
                    cargarDDpotreros(fundo);
                } else {
                    nombreFundo = "";
                    fundo = "0";
                    cargarDDpotreros("0");
                    cargarDDsector("0", "0");
                    cargarDDcuartel("0", "0", "0");
                    cargarDDhilera("0", "0", "0", "0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        ddPotrero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!ddPotrero.getItemAtPosition(position).toString().equals("Seleccionar...")) {
                    String[] split1 = ddPotrero.getItemAtPosition(position).toString().split(":");
                    String[] split2 = split1[1].split(" ");
                    potrero = split2[1];
                    nombrePotrero = split1[2] + ">";
                    cargarDDsector(fundo, potrero);
                } else {
                    nombrePotrero = "";
                    potrero = "0";
                    cargarDDsector("0", "0");
                    cargarDDcuartel("0", "0", "0");
                    cargarDDhilera("0", "0", "0", "0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        ddSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!ddSector.getItemAtPosition(position).toString().equals("Seleccionar...")) {
                    String[] split1 = ddSector.getItemAtPosition(position).toString().split(":");
                    String[] split2 = split1[1].split(" ");
                    sector = split2[1];
                    nombreSector = split1[2] + ">";
                    cargarDDcuartel(fundo, potrero, sector);
                } else {
                    nombreSector = "";
                    sector = "0";
                    cargarDDcuartel("0", "0", "0");
                    cargarDDhilera("0", "0", "0", "0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        ddCuartel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!ddCuartel.getItemAtPosition(position).toString().equals("Seleccionar...")) {
                    String[] split1 = ddCuartel.getItemAtPosition(position).toString().split(":");
                    String[] split2 = split1[1].split(" ");
                    cuartel = split2[1];
                    nombreCuartel = split1[2] + ">";
                    cargarDDhilera(fundo, potrero, sector, cuartel);
                } else {
                    nombreCuartel = "";
                    cuartel = "0";
                    cargarDDhilera("0", "0", "0", "0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ddHilera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!ddHilera.getItemAtPosition(position).toString().equals("Seleccionar...")) {
                    btEliminar.setVisibility(View.VISIBLE);
                    String[] split1 = ddHilera.getItemAtPosition(position).toString().split(":");
                    String[] split2 = split1[1].split(" ");
                    hilera = split2[1];
                    nombreHilera = " " + split2[1] + ">";
                } else {
                    btEliminar.setVisibility(View.INVISIBLE);
                    nombreHilera = "";
                    hilera = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public void cargarDDfundos() {
        Globals global = Globals.getInstance();
        if (global.getData()) {
            syncro.eventoSync(toolbar.getRootView(), this, false);
            global.setData(false);
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(0, "Seleccionar...");
        String cadena = gestionFundo.getListaFundos().toString().substring(1, gestionFundo.getListaFundos().toString().length() - 1);
        if (!cadena.isEmpty()) {
            String[] split = cadena.split(",");
            for (int i = 0; i < split.length; i += 2) {
                list.add("ID: " + split[i].replace(" ", "") + "            Nombre: " + split[i + 1]);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            ddFundo.setAdapter(adapter);
        }
    }

    public void cargarDDpotreros(String f) {
        if (!f.equals("0")) {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            String cadena = gestionPotrero.getListaPotreros(f).toString().substring(1, gestionPotrero.getListaPotreros(f).toString().length() - 1);
            if (!cadena.isEmpty()) {
                String[] split = cadena.split(",");
                for (int i = 0; i < split.length; i += 3) {
                    list.add("ID: " + split[i].replace(" ", "") + "            Nombre: " + split[i + 2]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                ddPotrero.setAdapter(adapter);
            }
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            ddPotrero.setAdapter(adapter);
        }
    }

    public void cargarDDsector(String f, String p) {
        if (!f.equals("0") && !p.equals("0")) {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            String cadena = gestionSector.getListaSectores(f, p).toString().substring(1, gestionSector.getListaSectores(f, p).toString().length() - 1);
            if (!cadena.isEmpty()) {
                String[] split = cadena.split(",");
                for (int i = 0; i < split.length; i += 4) {
                    list.add("ID: " + split[i].replace(" ", "") + "            Nombre: " + split[i + 3]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                ddSector.setAdapter(adapter);
            }
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            ddSector.setAdapter(adapter);
        }
    }

    public void cargarDDcuartel(String f, String p, String s) {
        if (!f.equals("0") && !p.equals("0") && !s.equals("0")) {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            String cadena = gestionCuartel.getListaCuarteles(f, p, s).toString().substring(1, gestionCuartel.getListaCuarteles(f, p, s).toString().length() - 1);
            if (!cadena.isEmpty()) {
                String[] split = cadena.split(",");
                for (int i = 0; i < split.length; i += 6) {
                    list.add("ID: " + split[i].replace(" ", "") + "            Nombre: " + split[i + 5]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                ddCuartel.setAdapter(adapter);
            }
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            ddCuartel.setAdapter(adapter);
        }
    }

    public void cargarDDhilera(String f, String p, String s, String c) {
        if (!f.equals("0") && !p.equals("0") && !s.equals("0") && !c.equals("0")) {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            String cadena = gestionHilera.getListaHileras(f, p, s, c, lastMapeo).toString().substring(1, gestionHilera.getListaHileras(f, p, s, c, lastMapeo).toString().length() - 1);
            if (!cadena.isEmpty()) {
                String[] split = cadena.split(",");
                for (int i = 0; i < split.length; i += 7) {
                    list.add("ID: " + split[i].replace(" ", "") + "            Variedad: " + split[i + 5]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                ddHilera.setAdapter(adapter);
            }
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(0, "Seleccionar...");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            ddHilera.setAdapter(adapter);
        }
    }

    public void agregarHilera(final View view) {
        if (fundo.equals("0") || potrero.equals("0") || sector.equals("0") || cuartel.equals("0")) {
            Toast.makeText(this, "Por favor, selecione todas las opciones para agregar una hilera.", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder alertAgregarHilera = new AlertDialog.Builder(view.getContext());
            alertAgregarHilera.setTitle("Agregar Hilera");
            alertAgregarHilera.setMessage("Verifique código y seleccione variedad para la nueva hilera:");
            LinearLayout layout = new LinearLayout(view.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText idHilera = new EditText(view.getContext());
            InputFilter[] lengthFilter = new InputFilter[1];
            lengthFilter[0] = new InputFilter.LengthFilter(5); //Filter to 5 characters
            idHilera.setFilters(lengthFilter);
            final Spinner variedad = new Spinner(view.getContext());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gestionVariedad.getListaVariedades("32"));
            variedad.setAdapter(adapter);
            layout.addView(idHilera);
            layout.addView(variedad);
            alertAgregarHilera.setView(layout, 16, 16, 16, 16);

            int hilera;
            String id_hilera = "";
            try{
                 hilera = gestionHilera.getMaxHilera(fundo, potrero, sector, cuartel, lastMapeo) + 1;
                if (hilera < 10 && hilera >= 1) {
                    id_hilera = "H000" + hilera;
                } else {
                    if (hilera < 100 && hilera >= 10) {
                        id_hilera = "H00" + hilera;
                    } else {
                        if (hilera < 1000 && hilera >= 100) {
                            id_hilera = "H0" + hilera;
                        } else {
                            id_hilera = "H" + hilera;                      }
                    }
                }
                idHilera.setText(id_hilera);
            }catch (Exception ex){}

            alertAgregarHilera.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //AGREGAR HILERA SIGUIENTE A LA HILERA ANTERIOR
                    try {
                        gestionHilera.insertar(idHilera.getText().toString().toUpperCase().replace(" ",""), cuartel, sector, potrero, fundo, variedad.getSelectedItem().toString(), lastMapeo);
                        gestionHilera.insertarSyncHilera(idHilera.getText().toString().toUpperCase().replace(" ",""), cuartel, sector, potrero, fundo, variedad.getSelectedItem().toString(), lastMapeo, "insert");
                        Toast.makeText(view.getContext(), "Hilera agregada.", Toast.LENGTH_SHORT).show();
                        cargarDDhilera(fundo, potrero, sector, cuartel);
                    } catch (Exception ex) {
                        Toast.makeText(view.getContext(), "ERROR! No se pudo agregar hilera.", Toast.LENGTH_SHORT).show();
                        System.out.println(ex.toString());
                    }
                }
            });

            alertAgregarHilera.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertAgregarHilera.show();
        }
    }

    public void eliminarHilera(View view) {
        final Context context = this;
        String[] split1 = ddHilera.getSelectedItem().toString().split(":");
        String[] split2 = split1[1].split(" ");
        final String id_hilera = split2[1];
        AlertDialog.Builder alertEliminarHilera = new AlertDialog.Builder(this);
        alertEliminarHilera.setTitle("Eliminar Hilera");
        alertEliminarHilera.setMessage("¿Está seguro de que desea eliminar la hilera: " + id_hilera + "?   Si selecciona 'ELIMINAR', se eliminarán TODAS las plantas asociadas a esta hilera.");
        alertEliminarHilera.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //eliminar hilera
                try {
                    int maxhilera = gestionHilera.getMaxHilera(fundo, potrero, sector, cuartel, lastMapeo);
                    if (Integer.parseInt(id_hilera.replace("H", ""), 10) == maxhilera) {
                        gestionHilera.eliminar(id_hilera, cuartel, sector, potrero, fundo, lastMapeo);
                        gestionHilera.insertarSyncHilera(id_hilera, cuartel, sector, potrero, fundo, "", lastMapeo, "delete");
                        Toast.makeText(context, "Hilera eliminada.", Toast.LENGTH_SHORT).show();
                        cargarDDhilera(fundo, potrero, sector, cuartel);
                    } else {
                        Toast.makeText(context, "ATENCIÓN! Solo se puede eliminar la última hilera", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "ERROR! No se pudo eliminar planta.", Toast.LENGTH_SHORT).show();
                    System.out.println(ex.toString());
                }
            }
        });

        alertEliminarHilera.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertEliminarHilera.show();
    }

    public void verPlantas(View view) {
        if (fundo.equals("0") || potrero.equals("0") || sector.equals("0") || cuartel.equals("0") || hilera.equals("0")) {
            Toast.makeText(this, "Por favor, selecione todas las opciones.", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, PlantasActivity.class);
            i.putExtra("fundo", fundo);
            i.putExtra("potrero", potrero);
            i.putExtra("sector", sector);
            i.putExtra("cuartel", cuartel);
            i.putExtra("hilera", hilera);
            i.putExtra("titulo", nombreFundo + nombrePotrero + nombreSector + nombreCuartel + nombreHilera);
            i.putExtra("usuario", usuario);
            i.putExtra("contraseña", contraseña);
            startActivity(i);
            Globals global = Globals.getInstance();
            global.setData(true);
        }
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
