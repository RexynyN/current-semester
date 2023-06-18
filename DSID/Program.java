import java.util.Scanner;

class Program {
    public static void main(String [] args){
        
        System.out.println("Hello Stone World!");

        Scanner scan = new Scanner(System.in);

        int sexoCon = scan.nextInt();

        String nibba = Integer.toString(sexoCon);

        System.out.println(nibba); 

        scan.close();
    }
}