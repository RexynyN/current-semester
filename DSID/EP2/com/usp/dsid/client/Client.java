package com.usp.dsid.client;

import java.util.Scanner;

public class Client {
    public static void main(String [] args) {
        ActionConsole console = new ActionConsole();
        Scanner scan = new Scanner(System.in);
        String command = ""; 
        String sideEffect = "";

        while(sideEffect != "quit"){
            System.out.print(">> ");
            command = scan.nextLine();
            sideEffect = console.runCommand(command);
        }
    }
}
