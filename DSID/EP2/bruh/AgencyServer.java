package bruh;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AgencyServer {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Porta em que a agência deve rodar: ");
        int port = scan.nextInt();

        SandboxRMI sand = new SandboxRMI(port);
    }

}
