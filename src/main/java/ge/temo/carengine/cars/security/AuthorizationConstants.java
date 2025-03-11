package ge.temo.carengine.cars.security;

public class AuthorizationConstants {

    public static final String ADMIN = "hasAuthority('ROLE_ADMIN')";
    public static final String USER_OR_ADMIN = "hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')";
}
