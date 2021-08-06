package com.teamteach.commons.utils;

public class AnonymizeService {

    private static String rotate(String data, int direction) {
        char dataArray[] = new char[data.length()];
        dataArray[0] = data.charAt(0);
        for(int i = 1 ; i < data.length() ; i++){
            dataArray[i] = (char)((int)data.charAt(i) + direction);
        }
        return String.valueOf(dataArray);
    }

    public static String anonymizeData(String data) {
        int numberOfRotations = ((int)(data.charAt(0)) % 5);
        while(numberOfRotations >= 0){
            data = rotate(data,1);
            numberOfRotations--;
        }
        char dataArray[] = new char[data.length()];
        dataArray[0] = (char)((int)data.charAt(0) + 1);
        for(int i = 1 ; i < data.length() ; i++){
            dataArray[i] = data.charAt(i);
        }
        return String.valueOf(dataArray);
    }

    public static String deAnonymizeData(String data) {
        char dataArray[] = new char[data.length()];
        dataArray[0] = (char)((int)data.charAt(0) - 1);
        for(int i = 1 ; i < data.length() ; i++){
            dataArray[i] = data.charAt(i);
        }
        data = String.valueOf(dataArray);
        int numberOfRotations = ((int)(data.charAt(0)) % 5);
        while(numberOfRotations >= 0){
            data = rotate(data,-1);
            numberOfRotations--;
        }
        return data;
    }
}