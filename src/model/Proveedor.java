package model;

public class Proveedor {

    private int id;
    private String nombre;
    private String telefono;

    public Proveedor(int id, String nombre, String telefono) {
        this.id = id;
        setNombre(nombre);
        this.telefono = telefono;
    }

    public int getId() { return id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() { return nombre; }
}
