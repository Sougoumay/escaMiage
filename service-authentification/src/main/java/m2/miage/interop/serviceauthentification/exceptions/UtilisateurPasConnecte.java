package m2.miage.interop.serviceauthentification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UtilisateurPasConnecte extends RuntimeException {
}
