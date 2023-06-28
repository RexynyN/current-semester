package com.usp.dsid.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Utils {
    private static final String OKBLUE = "\033[94m";
    private static final String OKGREEN = "\033[92m";
    private static final String WARNING = "\033[93m";
    private static final String FAIL = "\033[91m";
    private static final String ENDC = "\033[0m";

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
}
