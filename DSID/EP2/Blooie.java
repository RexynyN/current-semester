public class Blooie {
    public static int main(String[] args) {
        System.out.println("Hello World!");
        if(args.length >= 2){
            int a = Integer.parseInt(args[0]);
            int b = Integer.parseInt(args[1]);
            return a + b;
        }

        return 42;
    }
}