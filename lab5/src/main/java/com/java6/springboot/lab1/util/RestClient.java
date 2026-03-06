package com.java6.springboot.lab1.util;

import java.io.IOException;

public class RestClient {

    public static void main(String[] args) {

        getAll();
         getByKey();
         post();
         put();
         delete();
    }
    static String host = "https://java6-6f9c1-default-rtdb.asia-southeast1.firebasedatabase.app";
    private static void getAll() {
        var url = host + "/students.json";
        try{
            var connection = HttpClient.openConnection("GET",url);
            var response = HttpClient.readData(connection);
            System.out.println(new String(response));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
    private static void getByKey() {
        var url = host + "/students/-On0qfEfJY3mO0i_alFn.json";
        try{
            var connection = HttpClient.openConnection("GET",url);
            var response = HttpClient.readData(connection);
            System.out.println(new String(response));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    private static void post() {
        var url = host + "/students.json";
        var data = "{\"name\":\"<NAME>\",\"age\":20}".getBytes();
        try{
            var connection = HttpClient.openConnection("POST",url);
            var response = HttpClient.writeData(connection,data);
            System.out.println(new String(response));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    private static void put() {
        var url = host + "/students/-On0qfEfJY3mO0i_alFn.json";
        var data = "{\"name\":\"<NAME>\",\"age\":20}".getBytes();
        try{
            var connection = HttpClient.openConnection("PUT",url);
            var response = HttpClient.writeData(connection,data);
            System.out.println(new String(response));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    private static void delete() {
        var url = host + "/students/-On0qfEfJY3mO0i_alFn.json";
        try{
            var connection = HttpClient.openConnection("DELETE",url);
            var response = HttpClient.readData(connection);
            System.out.println(new String(response));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
