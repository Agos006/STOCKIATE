package model;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private Categoria categoria;
    private Proveedor proveedor;
    private final Stock stock;

    public Producto(int id, String nombre, double precio, Categoria categoria,
                    Proveedor proveedor, int cantidadInicial, int stockMinimo) {
        this.id = id;
        setNombre(nombre);
        setPrecio(precio);
        setCategoria(categoria);
        setProveedor(proveedor);
        this.stock = new Stock(this, cantidadInicial, stockMinimo);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public double getPrecio() { return precio; }

    public void setPrecio(double precio) {
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo ni inválido.");
        }
        this.precio = precio;
    }

    public Categoria getCategoria() { return categoria; }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Proveedor getProveedor() { return proveedor; }

    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public Stock getStock() { return stock; }

    /** Atajo para mostrar la cantidad actual sin exponer toda la lógica de Stock. */
    public int getCantidad() { return stock.getCantidadActual(); }
}

