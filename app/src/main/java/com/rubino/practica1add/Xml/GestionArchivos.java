package com.rubino.practica1add.Xml;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import com.rubino.practica1add.contacto.Contacto;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by marco on 21/10/2015.
 */
public class GestionArchivos extends AppCompatActivity{

    private List <Contacto> con;



    public void escribir2(List<Contacto> lContactos) throws IOException {

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
            Log.v("NUMEROS", numContactos + "");

            if(numContactos>0) {
                docxml.startTag(null, "telefono");
                if (numContactos == 1) {
                    docxml.startTag(null, "telefono");
                    docxml.text(s.getlTelf().get(0));
                    docxml.endTag(null, "telefono");
                } else  if (numContactos == 2){
                    docxml.startTag(null, "telefono");
                    docxml.text(s.getlTelf().get(0));
                    docxml.endTag(null, "telefono");

                    docxml.startTag(null, "telefono");
                    docxml.text(s.getlTelf().get(1));
                    docxml.endTag(null, "telefono");
                }else  if (numContactos == 3){
                    docxml.startTag(null, "telefono");
                    docxml.text(s.getlTelf().get(0));
                    docxml.endTag(null, "telefono");

                    docxml.startTag(null, "telefono");
                    docxml.text(s.getlTelf().get(1));
                    docxml.endTag(null, "telefono");

                    docxml.startTag(null, "telefono");
                    docxml.text(s.getlTelf().get(2));
                    docxml.endTag(null, "telefono");
                }
                docxml.endTag(null, "telefono");
            }

            docxml.endTag(null, "contacto");
        }
        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }


    public void escribir(List<Contacto> x) throws IOException {
        Random r = new Random();
        FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"cont_backup.xml"));
        XmlSerializer docxml = Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        ArrayList<String> l= new ArrayList<>();
        docxml.startTag(null, "contactos");
        for(int i = 0; i<x.size();i++){
            docxml.startTag(null, "contacto");
            docxml.startTag(null, "nombre");
            docxml.attribute(null, "id", String.valueOf(x.get(i).getId()));
            docxml.text(x.get(i).getNombre().toString());
            docxml.endTag(null, "nombre");
            for(int j=0; j<x.get(i).getlTelf().size(); j++) {
                docxml.startTag(null, "telefono");
                docxml.text(x.get(i).getTelefono(j).toString());
                docxml.endTag(null, "telefono");
            }
            docxml.endTag(null, "contacto");
        }
        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }

    public List<Contacto> leer() throws IOException, XmlPullParserException {
        Contacto c= null;
        int id=0;
        List <String> telf= new ArrayList<>();
        String nom="";
        XmlPullParser lectorxml = Xml.newPullParser();
        lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null), "cont_backup.xml")), "utf-8");
        int evento = lectorxml.getEventType();
        while (evento != XmlPullParser.END_DOCUMENT){
            if(evento == XmlPullParser.START_TAG) {
                String etiqueta = lectorxml.getName();
                if (etiqueta.compareTo("nombre") == 0) {
                    String atrib = lectorxml.getAttributeValue(null, "id");
                    String texto = lectorxml.nextText();
                    id = Integer.parseInt(lectorxml.getAttributeValue(null, "id"));
                    nom = lectorxml.nextText();

                } else if (etiqueta.compareTo("telefono") == 0) {
                    String texto = lectorxml.nextText();
                    telf.add(texto);

                }
                if (evento == XmlPullParser.END_TAG) {
                    c = new Contacto(id, nom, telf);
                }
            }
            con.add(c);
            evento = lectorxml.next();
        }
        return con;
    }


    public List<Contacto> pasarContactos(){
        List<Contacto> lista=new ArrayList<Contacto>();
        try {
            XmlPullParser lectorxml = Xml.newPullParser();
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null), "cont_backup.xml")), "utf-8");
            int evento = lectorxml.getEventType();
            Contacto c=new Contacto();
            while(evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG) {
                    String etiqueta = lectorxml.getName();
                    if (etiqueta.compareTo("nombre") == 0) {
                        c.setNombre(lectorxml.nextText());
                    }
                    if (etiqueta.compareTo("telefono") == 0) {
                        c.setTelefono(lectorxml.getAttributeCount() ,lectorxml.nextText());
                    }
                }
                if (evento == XmlPullParser.END_TAG) {
                    String etiqueta = lectorxml.getName();
                    if (etiqueta.compareTo("") == 0) {
                        lista.add(c);
                        c=new Contacto();
                    }
                }
                evento = lectorxml.next();
            }
        }catch(Exception e){}
        return lista;
    }

}
