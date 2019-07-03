package com.rendez.api.util;


import java.util.Random;

public class RandomNameUtil {
    static String[]  DIC = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    public static String generateName(String prefix,int length){
        if(length <=0 ){
            return null;
        }

        Random ran = new Random();
        int cursur = ran.nextInt(DIC.length);
        StringBuffer nameBuffer = new StringBuffer();
        nameBuffer.append(prefix);
        for(int i=0;i<length;i++){
            cursur = ran.nextInt(DIC.length);
            nameBuffer.append(DIC[cursur]);
        }

        return nameBuffer.toString();

    }


}
