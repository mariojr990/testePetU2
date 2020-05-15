package com.pet2u.pet2u.Helper;

import android.util.Base64;

public class Criptografia {

    public  static String codificar(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)",""); //remove caracteres de espaço no inicio e no fim
    }

    public static String decodificar(String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
