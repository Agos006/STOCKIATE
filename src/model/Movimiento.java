package model;

import java.time.LocalDateTime;

public class Movimiento {

    private int id;
    private final Producto producto;
    private final TipoMovimiento tipo;
    private final int cantidad;
    private final LocalDateTime fecha;

    public Movimiento(int id, Producto producto, TipoMovimiento tipo, int cantidad) {
        this(id, producto, tipo, cantidad, LocalDateTime.now());
    }

    public Movimiento(int id, Producto producto, TipoMovimiento tipo,
                      int cantidad, LocalDateTime fecha) {

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
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}