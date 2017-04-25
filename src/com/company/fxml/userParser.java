package com.company.fxml;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;

public class userParser {
    public static LinkedList<User> parse(File file) throws IOException {
        try(InputStream stream = new FileInputStream(file)){
            return parse(stream);
        }
    }

    public static LinkedList<User> parse(URL url) throws IOException{
        try(InputStream stream = url.openStream()) {
            return parse(stream);
        }
    }

    public static LinkedList<User> parse(InputStream stream) throws IOException{
        try(InputStreamReader reader = new InputStreamReader(stream)){
            return parse(reader);
        }
    }
    public static LinkedList<User> parse(InputStreamReader reader) throws IOException{
        LinkedList<User> result = new LinkedList<>();

        try(BufferedReader tmp = new BufferedReader(reader)){
            while(true){
                String line = tmp.readLine();

                if(line==null) break;

                User user = parseUser(line);

                if(user==null) continue;

                result.add(user);
            }
        }
        return result;
    }

    private static User parseUser(String line){
        String[] parts = line.split(";");
        if(parts.length == 5){
            for(String el : parts){
                if(el.isEmpty())
                    return null;
            }

            try{
                User user = new User();
                user.setName(parts[0]);
                user.setPass(parts[1]);
                user.setAdress(parts[2]);
                user.setAge(parts[3]);
                user.setSex(parts[4]);

                return user;
            } catch (NumberFormatException e){
                return null;
            }
        }

        return null;
    }
}
