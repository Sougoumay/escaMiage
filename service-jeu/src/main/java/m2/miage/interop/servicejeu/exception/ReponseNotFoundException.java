package m2.miage.interop.servicejeu.exception;

public class ReponseNotFoundException extends Exception {

    public ReponseNotFoundException(long id) {
        super("L'enigme avec l'id : " + id + " n'existe pas");
    }
}
