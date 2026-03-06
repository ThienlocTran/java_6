package com.java6.springboot.lab1.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    /**
     * Mở kết nối
     *
     * @param method là web method (GET, POST, PUT hay DELETE)
     * @param url địa chỉ URL của REST API
     */
    public static HttpURLConnection openConnection(String method, String url)
            throws IOException {
        var conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestMethod(method);
        return conn;
    }
    /**
     * Đọc dữ liệu phản hồi từ server và đóng kết nối
     */
    public static byte[] readData(HttpURLConnection connection)
            throws IOException {
        if(connection.getResponseCode() == 200){
            var out = new ByteArrayOutputStream();
            var is = connection.getInputStream();
            var block = new byte[ 4 * 1024 ];
            while(true){
                int n = is.read(block);
                if (n <= 0) break;
                out.write(block, 0, n);
            }

            connection.disconnect();
            return out.toByteArray();
        }

        connection.disconnect();
        return new byte[0];
    }
    /**
     * Gửi dữ liệu lên server và đọc dữ liệu phản hồi từ server
     */
    public static byte[] writeData(HttpURLConnection connection, byte[] data)
            throws IOException {
        connection.setDoOutput(true);
        connection.getOutputStream().write(data);
        return readData(connection);
    }
}
