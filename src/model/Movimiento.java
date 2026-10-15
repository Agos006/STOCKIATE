package model;

import java.time.LocalDateTime;

/** Registro de una entrada o salida de stock de un producto. */
public class Movimiento {

    private int id;
    private final Producto producto;
    private final TipoMovimiento tipo;
    private final int cantidad;
    private final LocalDateTime fecha;

    public Movimiento(int id, Producto producto, TipoMovimiento tipo, int cantidad) {
        if (producto == null || tipo == null) {
            throw new IllegalArgumentException("Producto y tipo son obligatorios.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.id = id;
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Producto getProducto() { return producto; }

    public TipoMovimiento getTipo() { return tipo; }

    public int getCantidad() { return cantidad; }

    public LocalDateTime getFecha() { return fecha; }
}
