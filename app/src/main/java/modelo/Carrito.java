package modelo;

public class Carrito {

    private String pid, nombre, Stock, descuento, precio;

    public Carrito() {
    }

    public Carrito(String pid, String nombre, String stock, String descuento, String precio) {
        this.pid = pid;
        this.nombre = nombre;
        Stock = stock;
        this.descuento = descuento;
        this.precio = precio;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}

