class Member{
	String account;
	String password;
	int score;
	Boolean onLineState;
	Boolean inRoomState;
	Member( String ac, String ps)
	{
		account = ac;
		password = ps;
		score = 0;
		onLineState = true;
		inRoomState = false;
	}
	public void setMember(String ac, String ps, int sc, Boolean ols, Boolean irs)
	{
		account = ac;
		password = ps;
		score = sc;
		onLineState = ols;
		inRoomState = irs;
	}
}