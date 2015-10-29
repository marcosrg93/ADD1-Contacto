package com.rubino.practica1add.Xml.Adaptador;

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
import com.rubino.practica1add.Sincronizacion;
import com.rubino.practica1add.contacto.Contacto;
import com.rubino.practica1add.Ram.GestionContactos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 28/10/2015.
 */
public class AdaptadorXml extends ArrayAdapter<Contacto> {

    private GestionContactos gc;
    Sincronizacion sc;
    private Contacto c;
    private Context ctx;
    private int res;
    private LayoutInflater lInflator;
    private List<Contacto> valores;

    static class ViewHolder {
        public TextView tv1, tv2;
        public ImageView iv;
    }

    public AdaptadorXml(Context context, int resource, List<Contacto> objects) {
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
