package com.pet2u.pet2u.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateCustom {
    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String dataString = simpleDateFormat.format(data);
        return  dataString;
    }
}
