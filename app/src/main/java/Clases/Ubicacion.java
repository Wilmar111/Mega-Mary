package Clases;

public class Ubicacion {
    private Double latitud;
    private Double longitud;
    public Ubicacion() {
        // Constructor vacío requerido por Firebase
    }

    public Ubicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }
}
