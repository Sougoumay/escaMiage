package m2.miage.interop.serviceauthentification.dto;

public class ModifierPassword {
    private long id;
    private long token;

    public ModifierPassword(long id, long token) {
        this.id = id;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }
}
