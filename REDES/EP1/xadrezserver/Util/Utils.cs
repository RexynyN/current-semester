namespace XadrezServer.Util
{
    static class Utils
    {
        public static string CreateId(){
            Random rand = new Random();

            string mesh = "ABCDEFGHIJKLMOPQRSTUVWXYZ0123456789";
            string ID = "";
            for (int i = 0; i < 6; i++)
            {
                ID += mesh[rand.Next(mesh.Length)];
            }
            return ID;
        }
    }
}