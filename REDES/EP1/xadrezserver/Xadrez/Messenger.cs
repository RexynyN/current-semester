namespace XadrezServer.Xadrez
{
    class Messenger
    {
        public List<string> Chat = new List<string>();
        public List<string> PretaBuffer = new List<string>();
        public List<string> BrancaBuffer = new List<string>();

        public string[] GetMessages(string cor)
        {
            if (cor == "Branca")
            {
                string[] mensagens = PretaBuffer.ToArray();

                foreach (var item in PretaBuffer)
                    Chat.Add(item);

                PretaBuffer.Clear();

                return mensagens;
            }
            else
            {
                string[] mensagens = BrancaBuffer.ToArray();

                foreach (var item in BrancaBuffer)
                    Chat.Add(item);

                BrancaBuffer.Clear();

                return mensagens;
            }
        }

        public void SetMessage(string cor, string message)
        {
            if (String.IsNullOrEmpty(message))
                return;
                
            if (cor == "Branca")
                BrancaBuffer.Add(message);
            else
                PretaBuffer.Add(message);
        }

        public string [] GetChat()
        {
            return Chat.ToArray();
        }
    }
}