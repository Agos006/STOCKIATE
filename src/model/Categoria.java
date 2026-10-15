package model;

public class Categoria {

    private int id;
    private String nombre;

    public Categoria(int id, String nombre) {
        this.id = id;
        setNombre(nombre);
    }

    public int getId() { return id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    @Override
    public String toString() { return nombre; }
}
