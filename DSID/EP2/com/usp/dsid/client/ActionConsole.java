package com.usp.dsid.client;

import java.util.Arrays;
import java.util.Scanner;

import com.usp.dsid.agency.Agency;

public class ActionConsole {
    private static Agency agency;
    private static Scanner scan = new Scanner(System.in);
    

    public ActionConsole (){
        agency = null;
    }

    public String runCommand (String command){

        return switchFunction(command);
    }

    private String switchFunction(String command){
        String [] cmdSlice = command.split(" ");
        String verb = cmdSlice[0];
        String [] args;
        if (cmdSlice.length > 1)
            args = Arrays.copyOfRange(cmdSlice, 1, cmdSlice.length); 
        else 
            args = null; 

        switch(verb){
            case "bind": {
                
            }

        }

        return "";
    }
}
