package org.example;

import org.example.temporary.API;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        API api = new API();
        try {
            api.call();
        } catch (IOException e) {
            System.out.println("Error ");
            System.out.println(e.getMessage());
        }
    }
}
