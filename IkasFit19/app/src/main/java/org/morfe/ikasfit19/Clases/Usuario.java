package org.morfe.ikasfit19.Clases;

public class Usuario {

    private String fecha;
    private int pasosTotales;


    public Usuario() {
    }

    public Usuario(String fecha, int pasosTotales) {
        this.fecha = fecha;
        this.pasosTotales = pasosTotales;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getPasosTotales() {
        return pasosTotales;
    }

    public void setPasosTotales(int pasosTotales) {
        this.pasosTotales = pasosTotales;
    }
}
