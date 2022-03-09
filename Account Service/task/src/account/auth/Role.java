package account.auth;

public enum Role {
    ADMINISTRATOR,
    ACCOUNTANT,
    USER,
    AUDITOR;

    public String withPrefix() {
        return "ROLE_" + name();
    }
}
