namespace XadrezClient.Response
{
    class CreateMatch : BaseResponse
    {
        public string Status { get; set; }
        public string MatchId{ get; set; }
        public string Player { get; set; }
    }
}