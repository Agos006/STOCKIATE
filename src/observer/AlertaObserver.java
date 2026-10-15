package observer;

import model.Stock;

/** Reacciona a los cambios de stock y avisa si está en el mínimo o por debajo. */
public class AlertaObserver implements Observer {

    @Override
    public void actualizar(Stock stock) {
        if (stock.estaBajoMinimo()) {
            System.out.println("ALERTA: stock bajo de "
                + stock.getProducto().getNombre()
                + " (quedan " + stock.getCantidadActual()
                + ", mínimo " + stock.getStockMinimo() + ")");
        }
    }
}
