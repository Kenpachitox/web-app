package org.example.app.util;


import java.util.ArrayList;

public class UsernameChecker {
    //TODO:
    // replace List to repository
    // drop field from utilClass
    private static ArrayList<String> usernameDictionary;
    private static ArrayList<String> passwordDictionary;


    private UsernameChecker(){}


    public static boolean checkValidUsername(String username){
        return !usernameDictionary.contains(username);
    }

    public static boolean checkValidPassword(String password){
        return !passwordDictionary.contains(password);
    }
}
