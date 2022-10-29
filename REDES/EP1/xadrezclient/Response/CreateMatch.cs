namespace XadrezClient.Response
{
    class CreateMatch : BaseResponse
    {
        public string MatchId{ get; set; }
        public string Player { get; set; }
    }
}