package com.rubino.practica1add.Xml;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.rubino.practica1add.R;
import com.rubino.practica1add.Xml.Adaptador.AdaptadorXml;
import com.rubino.practica1add.contacto.Contacto;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 21/10/2015.
 */
public class GestionArchivos extends AppCompatActivity{

    private AdaptadorXml ac;

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



    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++Dialogo++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

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
                            escribir(valores);
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
                            escribir(valores);
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
}
