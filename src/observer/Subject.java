package observer;

/** Objeto observable: avisa a sus observadores cuando cambia su estado. */
public interface Subject {
    void agregarObserver(Observer observer);
    void quitarObserver(Observer observer);
    void notificarObservers();
}
