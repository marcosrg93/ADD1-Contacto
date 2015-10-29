package com.rubino.practica1add.Ram.Adaptador;

/**
 * Created by marco on 06/10/2015.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rubino.practica1add.R;
import com.rubino.practica1add.contacto.Contacto;
import com.rubino.practica1add.Ram.GestionContactos;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorContacto extends ArrayAdapter<Contacto>{

    private GestionContactos gc;
    private Contacto c;
    private Context ctx;
    private int res;
    private LayoutInflater lInflator;
    private List<Contacto> valores;

    static class ViewHolder {
        public TextView tv1, tv2;
        public ImageView iv;
    }

    public AdaptadorContacto(Context context, int resource, List<Contacto> objects) {
        super(context, resource, objects);
        this.ctx = context;//actividad
        this.res = resource;//layout del item
        this.valores = objects;//lista de valores
        this.lInflator = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1
        ViewHolder vh = new ViewHolder();
        Contacto valor = valores.get(position);
        if(convertView==null){
            convertView = lInflator.inflate(res, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tvNombrexml);
            vh.tv1 = tv;
            tv = (TextView) convertView.findViewById(R.id.tvTelfxml);
            vh.tv2 = tv;
            ImageView iv = (ImageView) convertView.findViewById(R.id.ivNumxml);
            vh.iv = iv;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.iv.setTag(position);
        vh.tv1.setText(valores.get(position).getNombre());

        int numContactos = valor.getlTelf().size();
            if(numContactos>0) {
            vh.tv2.setText(valor.getlTelf().get(0));
            if (numContactos == 1) {
                vh.iv.setImageResource(R.drawable.ic_no);
                muestraDet(vh.iv, position);
            } else {
                vh.iv.setImageResource(R.drawable.ic_yes);
                muestraDet(vh.iv, position);
            }
        }

        return convertView;
    }

    public void muestraDet(ImageView iv, final int pos) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detalles(pos);
            }
        });
    }

    public  void dgInsert(){
        AlertDialog.Builder alert= new AlertDialog.Builder(ctx);
        alert.setTitle(R.string.dial_Titulo);
        LayoutInflater inflater= LayoutInflater.from(ctx);
        final View vista = inflater.inflate(R.layout.dialogo_insert, null);
        alert.setView(vista);
        alert.setPositiveButton(R.string.dial_insert,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        long id = valores.size() - 1;
                        EditText etN, etTel,etTel2,etTel3;
                        etN = (EditText) vista.findViewById(R.id.etInsertN);
                        etTel = (EditText) vista.findViewById(R.id.etInsertT);
                        etTel2 = (EditText) vista.findViewById(R.id.etInsertT2);
                        etTel3 = (EditText) vista.findViewById(R.id.etInsertT3);

                        List<String> telf = new ArrayList<String>();
                        Contacto c;

                        if(etN.getText().toString().isEmpty() &&
                                etTel.getText().toString().isEmpty() &&
                                etTel2.getText().toString().isEmpty() &&
                                etTel3.getText().toString().isEmpty()){
                            Toast toast1 = Toast.makeText(ctx,
                                    "Introduce los datos", Toast.LENGTH_SHORT);
                            toast1.show();
                        }else if(etTel2.getText().toString().isEmpty() &&
                                etTel3.getText().toString().isEmpty()){

                           c = new Contacto(id, etN.getText().toString(), telf);
                           c.addlTelf(etTel.getText().toString());
                            valores.add(c);
                        }else if(etTel3.getText().toString().isEmpty()){

                           c = new Contacto(id, etN.getText().toString(), telf);
                            c.addlTelf(etTel.getText().toString());
                            c.addlTelf(etTel2.getText().toString());
                            valores.add(c);
                        }else {

                           c = new Contacto(id, etN.getText().toString(), telf);
                            c.addlTelf(etTel.getText().toString());
                            c.addlTelf(etTel2.getText().toString());
                            c.addlTelf(etTel3.getText().toString());
                            valores.add(c);
                        }

                        notifyDataSetChanged();

                    }
                });
        alert.setNegativeButton(R.string.dial_cancel, null);
        alert.show();
    }

    public  void dgEdit(final int posicion){
        AlertDialog.Builder alert= new AlertDialog.Builder(ctx);
        alert.setTitle(R.string.dial_Titulo_ed);
        Contacto c = new Contacto();
        LayoutInflater inflater= LayoutInflater.from(ctx);
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
                        EditText etN, etTel,etTel2,etTel3;
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
                        notifyDataSetChanged();
                        Log.v(" EDITO datos", "" + c.toString());
                    }
                });
        alert.setNegativeButton(R.string.dial_cancel, null);
        alert.show();
        notifyDataSetChanged();
    }


    public  void detalles(final int posicion){
        AlertDialog.Builder alert= new AlertDialog.Builder(ctx);
        alert.setTitle(R.string.dial_det_Titulo);
        LayoutInflater inflater= LayoutInflater.from(ctx);
        final View vista = inflater.inflate(R.layout.dialogo_detalles, null);
        final TextView tvNom,tvTel,tvTel2,tvTel3,tv4,tv5;
        Contacto valor = valores.get(posicion);

        tvNom = (TextView) vista.findViewById(R.id.tvDNom);
        tvTel = (TextView) vista.findViewById(R.id.tvDTel);
        tvTel2 = (TextView) vista.findViewById(R.id.tvDTel2);
        tvTel3 = (TextView) vista.findViewById(R.id.tvDTel3);
        tv4 = (TextView) vista.findViewById(R.id.textView4);
        tv5 = (TextView) vista.findViewById(R.id.textView5);

        tvNom.setText(valores.get(posicion).getNombre());
        int numContactos = valor.getlTelf().size();
        if(numContactos>0) {
            if (numContactos == 1) {
                tvTel.setText(valor.getlTelf().get(0));
                tv4.setVisibility(View.INVISIBLE);
                tv5.setVisibility(View.INVISIBLE);
            } else  if (numContactos == 2){
                tvTel.setText(valor.getlTelf().get(0));
                tvTel2.setText(valor.getlTelf().get(1));
                tv5.setVisibility(View.INVISIBLE);
            }else  if (numContactos == 3){
                tvTel.setText(valor.getlTelf().get(0));
                tvTel2.setText(valor.getlTelf().get(1));
                tvTel3.setText(valor.getlTelf().get(2));
            }
        }
        alert.setView(vista);
        alert.setNegativeButton(R.string.dial_det_back, null);
        alert.show();
    }

    public boolean borrar(int position) {
        try {
            valores.remove(position);
            this.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

}


