package observer;

import model.Stock;

/** Componente que quiere enterarse cuando cambia un Stock. */
public interface Observer {
    void actualizar(Stock stock);
}
