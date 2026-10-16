//recibe los datos tal como llegan de la interfaz (texto), los convierte y llama a los servicios
//La vista no habla directo con los servicios: pasa siempre por acá.
package controller;

import java.util.List;
import model.Movimiento;
import model.Producto;
import service.ProductoService;
import service.StockService;

public class ProductoController {

    private final ProductoService productoService;
    private final StockService stockService;

    public ProductoController(ProductoService productoService, StockService stockService) {
        this.productoService = productoService;
        this.stockService = stockService;
    }

    public Producto registrarProducto(String nombre, String precio, String categoria,
            String proveedor, String cantidad, String stockMinimo) {
        return productoService.registrarProducto(
                nombre,
                aDecimal(precio, "precio"),
                categoria,
                proveedor,
                aEntero(cantidad, "cantidad"),
                aEntero(stockMinimo, "stock mínimo"));
    }

    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    public Movimiento registrarMovimiento(String nombreProducto, String tipo, String cantidad) {
        int unidades = aEntero(cantidad, "cantidad");

        if ("ENTRADA".equals(tipo)) {
            return stockService.registrarEntrada(nombreProducto, unidades);
        }
        if ("SALIDA".equals(tipo)) {
            return stockService.registrarSalida(nombreProducto, unidades);
        }
        throw new IllegalArgumentException("Elegí si es una entrada o una salida.");
    }

    public List<Movimiento> obtenerHistorial(String nombreProducto) {
        return stockService.obtenerHistorial(nombreProducto);
    }

    private int aEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero.");
        }
    }

    private double aDecimal(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número válido.");
        }
    }
}
