//va a encargarse de guardar y buscar los productos
//pd:por ahora los datos existen mientras el programa está ejecutándose. Al cerrarlo, se pierden. Más adelante podemos incorporar almacenamiento permanente.
package repository;

import java.util.ArrayList;
import java.util.List;
import model.Producto;

public class ProductoRepository {

    private List<Producto> productos = new ArrayList<>();
    private int proximoId = 1;

    public void registrar(Producto producto) {
        producto.setId(proximoId++);
        productos.add(producto);
    }

    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre.trim())) {
                return p;
            }
        }
        return null;
    }
}
