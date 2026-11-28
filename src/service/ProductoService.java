package service;

import java.util.List;
import model.Categoria;
import model.Producto;
import model.Proveedor;
import observer.AlertaObserver;
import repository.ProductoRepository;

public class ProductoService {

    private final ProductoRepository repositorio;
    // un solo observer de alertas, compartido por todos los productos
    private final AlertaObserver alertas = new AlertaObserver();

    public ProductoService(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Producto registrarProducto(String nombre, double precio,
                                      String nombreCategoria, String nombreProveedor,
                                      int cantidadInicial, int stockMinimo) {

        if (repositorio.buscarPorNombre(nombre == null ? "" : nombre) != null) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }

        Categoria categoria = new Categoria(0, nombreCategoria);
        Proveedor proveedor = new Proveedor(0, nombreProveedor, null);

        Producto producto = new Producto(0, nombre, precio,
                categoria, proveedor, cantidadInicial, stockMinimo);

        producto.getStock().agregarObserver(alertas);
        repositorio.registrar(producto);
        return producto;
    }

    public List<Producto> listarProductos() {
        return repositorio.listarTodos();
    }

    public Producto obtenerPorNombre(String nombre) {
        Producto producto = repositorio.buscarPorNombre(nombre == null ? "" : nombre);
        if (producto == null) {
            throw new IllegalArgumentException("No existe un producto con ese nombre.");
        }
        return producto;
    }

    // Actualiza el stock del producto en la base de datos.
    public void actualizarStock(Producto producto) {
        repositorio.actualizarStock(
                producto.getId(),
                producto.getCantidad()
        );
    }
}