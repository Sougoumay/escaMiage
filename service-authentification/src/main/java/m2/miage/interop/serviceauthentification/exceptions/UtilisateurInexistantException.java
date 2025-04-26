package m2.miage.interop.serviceauthentification.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class UtilisateurInexistantException extends RuntimeException {
}
