package m2.miage.interop.servicejeu.exception;

public class PartieNotFoundException extends Exception {

    public PartieNotFoundException(long id) {
        super("Partie avec l'id : " + id + " n'existe pas");
    }
}
