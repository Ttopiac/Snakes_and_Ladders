class Room{
	String roomName;
	PlayerList playerList;
	MapObjImageList mapObjList;
	int state;// 0:idle 1~4:playing -1:ending
	
	Room( String rN)
	{
		roomName = rN;
		playerList = new PlayerList();
		mapObjList = new MapObjImageList();
		mapObjList.RandAddMapObj(4,4);
		state = 0;
	}
	public void setRoom(String rN, PlayerList pL, MapObjImageList mol, int s)
	{
		roomName = rN;
		playerList = pL;
		mapObjList = mol;
		state = s;
	}
}