package com.uws.jupiter.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;

import com.uws.jupiter.nameserver.LookupServer;

public class Utils {
    // Cores do texto
    private static final String OKBLUE = "\033[94m";
    private static final String OKGREEN = "\033[92m";
    private static final String WARNING = "\033[93m";
    private static final String FAIL = "\033[91m";
    private static final String ENDC = "\033[0m";

    // NameServer
    private static String nsName = "nameserver";
    private static int nsPort = 5454;

    public static void failPrint(Object str){
        System.out.println(FAIL + str.toString() + ENDC);
    }

    public static void okPrint(Object str){
        System.out.println(OKGREEN + str.toString() + ENDC);
    }

    public static void infoPrint(Object str){
        System.out.println(WARNING + str.toString() + ENDC);
    }

    public static void helpPrint(String cmd, String help){
        System.out.println(OKBLUE + cmd + ENDC + WARNING + " => " + help + ENDC);
    }

    public static String readFile(String fileName) {
        try {
            Path path = Paths.get(fileName);
            Scanner scanner = new Scanner(path);
            String fileContent = "";
            
            while (scanner.hasNextLine()) {
                fileContent += scanner.nextLine();
            }
            scanner.close();

            return fileContent;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getRandomID(){
        Random r = new Random();
        int lower = 100000;
        int upper = 999999999;

        return r.nextInt(lower, upper);
    }

    public static LookupServer connectNameServer(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", nsPort);
            LookupServer lookup = (LookupServer) registry.lookup(nsName);
            return lookup;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    
    }
}
