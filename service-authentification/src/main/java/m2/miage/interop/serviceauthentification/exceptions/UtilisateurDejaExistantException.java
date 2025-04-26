package m2.miage.interop.serviceauthentification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UtilisateurDejaExistantException extends Exception {
}
