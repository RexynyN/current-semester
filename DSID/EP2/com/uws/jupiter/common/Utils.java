package com.uws.jupiter.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.uws.jupiter.nameserver.LookupServer;

// Classe de utilidades diversas usadas no projeto
public class Utils {
    // Cores do texto
    private static final String OKBLUE = "\033[94m";
    private static final String OKGREEN = "\033[92m";
    private static final String WARNING = "\033[93m";
    private static final String FAIL = "\033[91m";
    private static final String ENDC = "\033[0m";

    // NameServer
    private static String nsName = "NameServer";
    private static int nsPort = 5454;

    // ======================================= TEXTOS COLORIDOS =======================================  
    public static void failPrint(Object str){
        System.out.println(FAIL + str.toString() + ENDC);
    }

    public static void bluePrint(Object str){
        System.out.println(OKBLUE + str.toString() + ENDC);
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

    // Lê um arquivo o retorna como uma string
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
            return "";
        }
    }

    // Lê um arquivo e o retorna como uma lista de strings (uma string para cada linha)
    public static String [] readFileAllLines(String fileName) {
        try {
            Path path = Paths.get(fileName);
            Scanner scanner = new Scanner(path);
            List<String> lines = new ArrayList<>();
            
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }

            scanner.close();
            return lines.toArray(new String[0]);
        } catch (Exception e) {
            return new String[] { "" };
        }
    }

    // Retorna um número pseudo-aleatório para formar o ID de um agente
    public static int getRandomID(){
        Random r = new Random();
        int upper = 999999999;

        return r.nextInt(upper);
    }

    // Utilidade para se conectar ao servidor de nomes
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
