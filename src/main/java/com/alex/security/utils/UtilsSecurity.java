package com.alex.security.utils;

public class UtilsSecurity {

    public  final static String[] PUBLIC_ROUTES = {
            "/api/v1/security/auth/login",
            "/api/v1/security/auth/login",
            "/api/v1/security/auth/refresh",
            "/api/v1/security/auth/activate/**",

            "api/v1/security/auth/pw/recovery",
            "api/v1/security/auth/pw/recovery/**",
            "/actuator/**",

            "/api/v1/security/security/users/register",

            "/api/v1/auth/pw/forgot",
            "/api/v1/auth/pw/recovery/**",
            "/api/core/get",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };



    public static  final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";


    public static final String SERVER = "http://localhost:9095/";

    public static  final String ENDPOINT_RECOVERY = SERVER + "api/v1/security/auth/pw/recovery/";
    public static  final String ENDPOINT_ACTIVATE =  SERVER + "api/v1/security/auth/activate/";
}
