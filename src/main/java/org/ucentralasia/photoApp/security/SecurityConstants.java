package org.ucentralasia.photoApp.security;

import org.springframework.core.env.Environment;
import org.ucentralasia.photoApp.SpringAppContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 DAYS
    public static final String TOKEN_PREFIX = "Ghost ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    // min length 64 characters
    public static final String TOKEN_SECRET = "skuqwzidtxae62l8rnbj0my5xmy5ec75l337uinixnyal9idxjzxo8fjvuejoash";

    public static String getTokenSecret() {
        Environment environment = (Environment) SpringAppContext.getBean("environment");
        return environment.getProperty("secretToken");
    }
}