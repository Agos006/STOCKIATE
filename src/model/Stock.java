package model;

import java.util.ArrayList;
import java.util.List;
import observer.Observer;
import observer.Subject;

/** Stock de un producto. Es el Subject del patrón Observer. */
public class Stock implements Subject {

    private final Producto producto;
    private int cantidadActual;
    private int stockMinimo;
    private final List<Observer> observers = new ArrayList<>();

    public Stock(Producto producto, int cantidadActual, int stockMinimo) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (cantidadActual < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        }
        this.producto = producto;
        this.cantidadActual = cantidadActual;
        this.stockMinimo = stockMinimo;
    }

    public void registrarEntrada(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de entrada debe ser mayor a cero.");
        }
        cantidadActual += cantidad;
        notificarObservers();
    }

    public void registrarSalida(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de salida debe ser mayor a cero.");
        }
        if (cantidad > cantidadActual) {
            throw new IllegalArgumentException("Stock insuficiente: hay " + cantidadActual + ".");
        }
        cantidadActual -= cantidad;
        notificarObservers();
    }

    public boolean estaBajoMinimo() {
        return cantidadActual <= stockMinimo;
    }

    public Producto getProducto() { return producto; }

    public int getCantidadActual() { return cantidadActual; }

    public int getStockMinimo() { return stockMinimo; }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        }
        this.stockMinimo = stockMinimo;
        notificarObservers();
    }

    @Override
    public void agregarObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void quitarObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers() {
        for (Observer o : new ArrayList<>(observers)) {
            o.actualizar(this);
        }
    }
}
