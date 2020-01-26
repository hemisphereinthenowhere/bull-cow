package com.example.bullcow.security;

public final class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/auth/login";

    // Signing key for HS512 algorithm
    // http://www.allkeysgenerator.com/ used to generate key
    public static final String JWT_SECRET = "9z$C&F)J@NcRfUjXnZr4u7x!A%D*G-KaPdSgVkYp3s5v8y/B?E(H+MbQeThWmZq4";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final long TOKEN_EXPIRATION_TIME = 864_000_000;
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
