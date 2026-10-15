//guarda el historial de movimientos (entradas y salidas de stock)
//pd: igual que ProductoRepository, por ahora los datos viven solo mientras el programa está ejecutándose.
package repository;

import java.util.ArrayList;
import java.util.List;
import model.Movimiento;
import model.Producto;

public class MovimientoRepository {

    private List<Movimiento> movimientos = new ArrayList<>();
    private int proximoId = 1;

    public void registrar(Movimiento movimiento) {
        movimiento.setId(proximoId++);
        movimientos.add(movimiento);
    }

    public List<Movimiento> listarPorProducto(Producto producto) {
        List<Movimiento> resultado = new ArrayList<>();
        for (Movimiento m : movimientos) {
            if (m.getProducto() == producto) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}
