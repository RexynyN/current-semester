package com.usp.dsid.agency;

import java.rmi.RemoteException;
import java.util.Scanner;

import com.usp.dsid.common.Agency;

public class AgencyServer {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Nome do servidor: ");
        String name = scan.nextLine();
        System.out.print("Porta do servidor: ");
        int port = Integer.parseInt(scan.nextLine());
        
        Server mainServer = new Server(name, port);
        Thread serverThread = new Thread(mainServer);

        try {
            Agency mainAgency = new Agency();
            Thread agencyThread = new Thread(mainAgency);
            
            agencyThread.start();
            serverThread.start();
        
        } catch (RemoteException e) {
            System.out.println("Erro ao criar servidor:");
            e.printStackTrace();
        }

        scan.close();

    }
}
