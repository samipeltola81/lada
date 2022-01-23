package fi.turskacreations.ladaapi.security;


public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String LOGIN_URL = "/login";
    public static final String SWAGGER_URL = "/v2/api-docs";
    public static final String ACTUATOR_URL = "/actuator/**";

    public static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/configuration/ui",
            "/configuration/security"
    };

}
