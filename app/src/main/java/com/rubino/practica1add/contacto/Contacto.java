package com.rubino.practica1add.contacto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contacto implements Serializable, Comparable<Contacto>{

    private long id;
    private String nombre;
    private List<String> lTelf;

    public Contacto() {
        this(0, "", new ArrayList<String>());
    }

    public Contacto(long id, String nombre, List<String> lTelf) {
        this.id = id;
        this.nombre = nombre;
        this.lTelf = lTelf;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getlTelf() {
        return lTelf;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setlTelf(List<String> lTelf) {
        this.lTelf = lTelf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacto contacto = (Contacto) o;

        if (id != contacto.id) return false;
        if (nombre != null ? !nombre.equals(contacto.nombre) : contacto.nombre != null)
            return false;
        return !(lTelf != null ? !lTelf.equals(contacto.lTelf) : contacto.lTelf != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (lTelf != null ? lTelf.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Contacto contacto) {
        int r = this.nombre.compareTo(contacto.nombre);
        if(r == 0){
            r = (int)(this.id - contacto.id);
        }
        return r;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", lTelf=" + lTelf +
                '}';
    }


    //---------------------------------------------------------------------------------------------------------------------//

    public void addlTelf(String telefono){
        this.lTelf.add(telefono);
    }

    public  void setTelefono (int location, String lTelf){
        this.lTelf.set(location, lTelf);
    }

    public String getTelefono(int location) {
        return lTelf.get(location);
    }

    public int size() {
        return lTelf.size();
    }

    public boolean isEmpty() {
        return lTelf.isEmpty();
    }




}