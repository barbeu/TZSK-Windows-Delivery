package com.example.tzadmin.tzsk_windows.AuthModule;

/**
 * Created by tzadmin on 14.04.17.
 */

public class Auth {
    /*TODO fix AuthData*/
    public static int id = -1;
    public static String login = "";
    public static String passwd = "";
    public static int autoLogin = -1;

    public static void setAuth (int idAuth, String loginAuth, String passwdAuth, int autoLoginAuth) {
        id = idAuth;
        login = loginAuth;
        passwd = passwdAuth;
        autoLogin = autoLoginAuth;
    }

    public static void resetAuth () {
        id = -1;
        login = "";
        passwd = "";
        autoLogin = -1;
    }
}
