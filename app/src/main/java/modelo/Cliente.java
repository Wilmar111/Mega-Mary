package modelo;

public class Cliente {
    private String Uid, Cedula , Nombres, Apellidos, Nombre_Negocio, Direccion, Telefono, Correo, Password;

    public Cliente() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }



    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getNombre_Negocio() {
        return Nombre_Negocio;
    }

    public void setNombre_Negocio(String nombre_Negocio) {
        Nombre_Negocio = nombre_Negocio;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "Cedula='" + Cedula + '\'' +
                ", Nombres='" + Nombres + '\'' +
                ", Apellidos='" + Apellidos + '\'' +
                ", Nombre_Negocio='" + Nombre_Negocio + '\'' +
                ", Direccion='" + Direccion + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", Correo='" + Correo + '\'' +
                '}';
    }
}
