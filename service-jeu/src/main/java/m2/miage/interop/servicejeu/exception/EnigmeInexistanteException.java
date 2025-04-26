package m2.miage.interop.servicejeu.exception;

public class EnigmeInexistanteException extends Exception {
    public EnigmeInexistanteException() {
        super("L'énigme demandée n'existe pas !");
    }

    public EnigmeInexistanteException(String message) {
        super(message);
    }
}
