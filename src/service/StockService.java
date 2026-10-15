//lógica de negocio del stock: entradas, salidas e historial de movimientos
//Cuando el stock cambia, Stock (el Subject) avisa solo a sus observers;
//este servicio no genera alertas.
package service;

import java.util.List;
import model.Movimiento;
import model.Producto;
import model.TipoMovimiento;
import repository.MovimientoRepository;

public class StockService {

    private final ProductoService productoService;
    private final MovimientoRepository historial;

    public StockService(ProductoService productoService, MovimientoRepository historial) {
        this.productoService = productoService;
        this.historial = historial;
    }

    public Movimiento registrarEntrada(String nombreProducto, int cantidad) {
        return registrarMovimiento(nombreProducto, TipoMovimiento.ENTRADA, cantidad);
    }

    public Movimiento registrarSalida(String nombreProducto, int cantidad) {
        return registrarMovimiento(nombreProducto, TipoMovimiento.SALIDA, cantidad);
    }

    public List<Movimiento> obtenerHistorial(String nombreProducto) {
        Producto producto = productoService.obtenerPorNombre(nombreProducto);
        return historial.listarPorProducto(producto);
    }

    private Movimiento registrarMovimiento(String nombreProducto,
            TipoMovimiento tipo, int cantidad) {

        Producto producto = productoService.obtenerPorNombre(nombreProducto);

        // primero se cambia el stock; si falla (por ejemplo, stock insuficiente),
        // no se guarda el movimiento
        if (tipo == TipoMovimiento.ENTRADA) {
            producto.getStock().registrarEntrada(cantidad);
        } else {
            producto.getStock().registrarSalida(cantidad);
        }

        Movimiento movimiento = new Movimiento(0, producto, tipo, cantidad);
        historial.registrar(movimiento);
        return movimiento;
    }
}
