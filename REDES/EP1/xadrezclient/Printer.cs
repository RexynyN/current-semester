namespace XadrezClient
{
    class Printer
    {
        public static void Error(string message)
        {
            ConsoleColor aux = Console.ForegroundColor;
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(message);
            Console.ForegroundColor = aux;
        }
    }
}