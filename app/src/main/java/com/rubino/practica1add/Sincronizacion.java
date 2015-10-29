package com.rubino.practica1add;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.rubino.practica1add.Xml.Adaptador.AdaptadorXml;
import com.rubino.practica1add.Xml.GestionArchivos;
import com.rubino.practica1add.contacto.Contacto;
import com.rubino.practica1add.Ram.GestionContactos;
import com.rubino.practica1add.contacto.OrdenaNombresAsc;
import com.rubino.practica1add.filtros.OnScrollUpDownListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Sincronizacion extends AppCompatActivity {

    private List<Contacto> lContactos;
    private TextView tvTexto, tvFecha;
    private RadioButton rb1,rb2;
    private FloatingActionButton fab;
    private SharedPreferences pc;
    private Boolean sinc;
    private AdaptadorXml ac;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sinc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.mn_ordenaMayor) {
            ordenaNombresAsc();
            ac.notifyDataSetChanged();
            return true;
        }
        if (id == R.id.sin_sincro) {
            sincronizar();
        }

        if(id == R.id.sin_ajustes){
            dgPrefe();
        }

        return super.onOptionsItemSelected(item);
    }

    //Creamos el menu contextual que nos dará las opciones de editar y borrar de cada Contacto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);

    }
    //Damos funcionalidad al los elementos del menu contextual
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        long id = item.getItemId();
        AdapterView.AdapterContextMenuInfo vistaInfo =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = vistaInfo.position;



        if(id==R.id.mn_editar){

            try {
                escribir( dgEdit(posicion,lContactos));
                Toast.makeText(this,"Elemento Editado",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else if(id==R.id.mn_borrar){
            ac.borrar(posicion);
            try {
                escribir(lContactos);
                Toast.makeText(this,"Elemento Borrado",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }


    //Inicializamos todos los componentes que llamaremos en el onCreate
    public  void init() throws IOException, XmlPullParserException {

        //Floating Action Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    escribir(dgInsert(lContactos));
                    Log.v("New C",lContactos.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Comprobamos la posicion para que se muestre o no
        OnScrollUpDownListener.Action scrollAction = new OnScrollUpDownListener.Action() {
            @Override
            public void up() {
                fab.hide();
            }

            @Override
            public void down() {
                fab.show();
            }
        };



        tvTexto = (TextView)findViewById(R.id.tvTexto);
        tvFecha = (TextView)findViewById(R.id.tvFecha);
        lContactos = GestionContactos.getLista(this);
        if(pc != null){
            if(pc.getBoolean("Sincronizacion",false)){
                Toast.makeText(this,"Sincronización Manual",Toast.LENGTH_LONG).show();
            }else {
               sincronizar();
                Toast.makeText(this,"Sincronización Realizada",Toast.LENGTH_LONG).show();
            }
        }

        listView = (ListView) findViewById(R.id.lvXml);


            lContactos = new ArrayList<>();
            lContactos = pasarContactos();
            Log.v("LEO","ARRAYLIST");
            ac = new AdaptadorXml(this, R.layout.elementos_lvxml, lContactos);
            listView.setAdapter(ac);
            listView.setTag(lContactos);




        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        registerForContextMenu(listView);

    }


    public void sincronizar(){
        try {
            lContactos = new ArrayList<>();
            lContactos = GestionContactos.getLista(this);
            Log.v("Sincro_Array",""+ GestionContactos.getLista(this));
            escribir(lContactos);
            Toast.makeText(this,"Sincronización completa",Toast.LENGTH_LONG).show();
            Log.v("ESCRIBIR", "Escribo");
        } catch (IOException e) {
            Toast.makeText(this,"Sincronización Fallida!!!",Toast.LENGTH_LONG).show();
            Log.v("ESCRIBIR", "No Escribo");
        }
    }
    //---------------------------------------------------------------------------------------------------------------------//
    public void ordenaNombresAsc(){
        Collections.sort(lContactos, new OrdenaNombresAsc());
    }

    public  List<Contacto> dgInsert(final List<Contacto> valores){
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle(R.string.dial_Titulo);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_insert, null);
        alert.setView(vista);
        alert.setPositiveButton(R.string.dial_insert,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        long id = valores.size() - 1;
                        EditText etN, etTel, etTel2, etTel3;
                        etN = (EditText) vista.findViewById(R.id.etInsertN);
                        etTel = (EditText) vista.findViewById(R.id.etInsertT);
                        etTel2 = (EditText) vista.findViewById(R.id.etInsertT2);
                        etTel3 = (EditText) vista.findViewById(R.id.etInsertT3);

                        List<String> telf = new ArrayList<String>();
                        Contacto c;

                        if (etN.getText().toString().isEmpty() &&
                                etTel.getText().toString().isEmpty() &&
                                etTel2.getText().toString().isEmpty() &&
                                etTel3.getText().toString().isEmpty()) {
                        } else if (etTel2.getText().toString().isEmpty() &&
                                etTel3.getText().toString().isEmpty()) {

                            c = new Contacto(id, etN.getText().toString(), telf);
                            c.addlTelf(etTel.getText().toString());
                            valores.add(c);
                        } else if (etTel3.getText().toString().isEmpty()) {

                            c = new Contacto(id, etN.getText().toString(), telf);
                            c.addlTelf(etTel.getText().toString());
                            c.addlTelf(etTel2.getText().toString());
                            valores.add(c);
                        } else {

                            c = new Contacto(id, etN.getText().toString(), telf);
                            c.addlTelf(etTel.getText().toString());
                            c.addlTelf(etTel2.getText().toString());
                            c.addlTelf(etTel3.getText().toString());
                            valores.add(c);
                        }

                        try {
                            escribir(lContactos);
                        } catch (IOException e) {
                            Log.v("FAILL", "Hay un fallo aqui");
                        }
                        ac.notifyDataSetChanged();

                    }
                });
        alert.setNegativeButton(R.string.dial_cancel, null);
        alert.show();
        return valores;
    }

    public  List<Contacto> dgEdit(final int posicion,final List<Contacto> valores){
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle(R.string.dial_Titulo_ed);
        Contacto c = new Contacto();
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_edit, null);

        Contacto valor = valores.get(posicion);
        EditText etN, etTel,etTel2,etTel3;
        etN = (EditText) vista.findViewById(R.id.editN);
        etTel = (EditText) vista.findViewById(R.id.editTelf);
        etTel2 = (EditText) vista.findViewById(R.id.editTelf2);
        etTel3 = (EditText) vista.findViewById(R.id.editTelf3);

        etN.setText(valores.get(posicion).getNombre());

        int numContactos = valor.getlTelf().size();
        if(numContactos>0) {
            if (numContactos == 1) {
                etTel.setText(valor.getlTelf().get(0));
                etTel2.setVisibility(View.INVISIBLE);
                etTel3.setVisibility(View.INVISIBLE);
            } else  if (numContactos == 2){
                etTel.setText(valor.getlTelf().get(0));
                etTel2.setText(valor.getlTelf().get(1));
                etTel3.setVisibility(View.INVISIBLE);
            }else  if (numContactos == 3){
                etTel.setText(valor.getlTelf().get(0));
                etTel2.setText(valor.getlTelf().get(1));
                etTel3.setText(valor.getlTelf().get(2));
            }
        }

        alert.setView(vista);
        alert.setPositiveButton(R.string.dial_insert,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText etN, etTel, etTel2, etTel3;
                        etN = (EditText) vista.findViewById(R.id.editN);
                        etTel = (EditText) vista.findViewById(R.id.editTelf);
                        etTel2 = (EditText) vista.findViewById(R.id.editTelf2);
                        etTel3 = (EditText) vista.findViewById(R.id.editTelf3);

                        valores.remove(posicion);
                        List<String> telf = new ArrayList<String>();
                        telf.add(etTel.getText().toString());
                        telf.add(etTel2.getText().toString());
                        telf.add(etTel3.getText().toString());
                        Contacto c = new Contacto(posicion, etN.getText().toString(), telf);
                        valores.add(c);
                        try {
                            escribir(lContactos);
                        } catch (IOException e) {
                            Log.v("FAILL", "Hay un fallo aqui");
                        }
                        ac.notifyDataSetChanged();
                        Log.v(" EDITO datos", "" + c.toString());
                    }
                });
        alert.setNegativeButton(R.string.dial_cancel, null);
        alert.show();
        ac.notifyDataSetChanged();
        return  valores;

    }
    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++PREFERENCIAS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    private  SharedPreferences setPreferencia(){
            pc = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            //preferencias.xml
        return pc;
    }


    public  void dgPrefe(){
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle(R.string.dial_pref);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_pref, null);

        /*final Calendar c = Calendar.getInstance();
        final int anio = c.get(Calendar.YEAR); //obtenemos el año
        final int mes = c.get(Calendar.MONTH)+1; //obtenemos el mes
        final int dia = c.get(Calendar.DAY_OF_MONTH);//obtenemos el día
        final int hora = c.get(Calendar.HOUR_OF_DAY); //obtenemos la hora*/

        TextView tvFecha = (TextView)vista.findViewById(R.id.tvFecha);
        final RadioButton rb1,rb2;
       rb1= (RadioButton)vista.findViewById(R.id.radioButton);
        rb2= (RadioButton)vista.findViewById(R.id.radioButton2);
        Date lastModified = new Date(Calendar.DATE);

        File file = new File(getExternalFilesDir(null),"cont_backup.xml");

        String valor = "";
        valor = pc.getString("Fecha","");
        if(valor.equals("")){

            String fecha;
            Boolean sin;

        if (file.exists()) {
            lastModified = new Date(file.lastModified());
        }



            pc = setPreferencia();


                fecha = pc.getString("Fecha",lastModified + "");

                tvFecha.setText(fecha);



            Log.v("VALORES","INICIALES");
               sin = pc.getBoolean("Sincronizacion", false);
            if (sin == false) {
                rb2.setChecked(true);
            } else {
                rb1.setChecked(true);
            }
        }

        alert.setView(vista);
        final Date finalLastModified = lastModified;
        alert.setPositiveButton(R.string.dial_insert,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        sinc = false;

                        if (rb1.isChecked()) {
                            sinc = true;
                            Log.v("RB TRUE", "Dentro");
                        } else if (rb2.isChecked()) {
                            sinc = false;
                            Log.v("RB False", "Dentro");
                        }
                        pc = setPreferencia();

                        SharedPreferences.Editor ed = pc.edit();
                        ed.putBoolean("Sincronizacion", sinc);

                        ed.putString("Fecha ", finalLastModified +"");
                        ed.commit();
                        Log.v("Guardo Preferencias", "");
                    }
                });
        alert.setNegativeButton(R.string.dial_cancel, null);
        alert.show();
    }


    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++XML++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void escribir(List<Contacto> lContactos) throws IOException {

        FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"cont_backup.xml"));
        XmlSerializer docxml = Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        docxml.startTag(null, "contactos");

        for(Contacto s: lContactos){
            docxml.startTag(null, "contacto");
            docxml.startTag(null, "nombre");
            docxml.attribute(null, "id", s.getId()+"");
            docxml.text(s.getNombre());
            docxml.endTag(null, "nombre");

            int numContactos = s.size();
            Log.v("NUMEROS del Contacto", numContactos + "");

            if(numContactos>0) {
                docxml.startTag(null, "telefonos");
                docxml.attribute(null, "id", s.getId() + "");
                if (numContactos == 1) {
                    docxml.startTag(null, "telefono");
                    docxml.attribute(null, "idN", s.getId() + "");
                    docxml.text(s.getlTelf().get(0));
                    docxml.endTag(null, "telefono");
                } else  if (numContactos == 2){
                    docxml.startTag(null, "telefono");
                    docxml.attribute(null, "idN", s.getId() + "");
                    docxml.text(s.getlTelf().get(0));
                    docxml.endTag(null, "telefono");

                    docxml.startTag(null, "telefono");
                    docxml.attribute(null, "idN", s.getId() + "");
                    docxml.text(s.getlTelf().get(1));
                    docxml.endTag(null, "telefono");
                }else  if (numContactos == 3){
                    docxml.startTag(null, "telefono");
                    docxml.attribute(null, "idN", s.getId() + "");
                    docxml.text(s.getlTelf().get(0));
                    docxml.endTag(null, "telefono");

                    docxml.startTag(null, "telefono");
                    docxml.attribute(null, "idN", s.getId() + "");
                    docxml.text(s.getlTelf().get(1));
                    docxml.endTag(null, "telefono");

                    docxml.startTag(null, "telefono");
                    docxml.attribute(null, "idN", s.getId() + "");
                    docxml.text(s.getlTelf().get(2));
                    docxml.endTag(null, "telefono");
                }
                docxml.endTag(null, "telefonos");
            }

            docxml.endTag(null, "contacto");
        }
        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }



    public List<Contacto> pasarContactos(){
        List<Contacto> lista= new ArrayList<>();
        try {

            XmlPullParser lectorxml = Xml.newPullParser();
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null), "cont_backup.xml")), "utf-8");
            int evento = lectorxml.getEventType();
            Contacto c=new Contacto();
            long atrib = 0,atrib2 = 0;
            String nom = "";
            ArrayList <String> telf= new ArrayList<>();


            while(evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG) {
                    String etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("contacto")==0){
                        telf=new ArrayList<>();
                        c=null;
                        atrib=0;
                        nom="";
                    }
                    if (etiqueta.compareTo("nombre") == 0) {
                        atrib = Integer.parseInt(lectorxml.getAttributeValue(null, "id"));
                        nom = lectorxml.nextText();
                        Log.v("M LEER ID ",atrib+" M LEER NOMBRE "+nom);
                    }
                    if(etiqueta.compareTo("telefonos") == 0){

                    }
                    if (etiqueta.compareTo("telefono") == 0) {
                        String texto = lectorxml.nextText();
                        telf.add(texto);
                        Log.v("TLF", telf.toString());

                    }


                }else if(evento == XmlPullParser.END_TAG){
                    String etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("contacto")==0){
                        c=new Contacto(atrib, nom, telf);
                        lista.add(c);
                        Log.v("INSERTO", c.toString());

                    }

                }

                evento = lectorxml.next();
            }

        }catch(Exception e){}
        Log.v("Contacto", lista.toString());
        return lista;
    }

}
