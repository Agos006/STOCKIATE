//va a encargarse de guardar y buscar los productos
//pd:por ahora los datos existen mientras el programa está ejecutándose. Al cerrarlo, se pierden. Más adelante podemos incorporar almacenamiento permanente.
package repository;

import java.util.ArrayList;
import java.util.List;
import model.Producto;

public class ProductoRepository {

    private List<Producto> productos = new ArrayList<>();

    public void registrar(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }
}