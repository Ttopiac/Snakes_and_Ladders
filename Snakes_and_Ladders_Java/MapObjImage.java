class MapObjImage{
	int start;
	int destination;
	int objKind;
	MapObjImage( int s, int d, int kind)
	{
		start = s;
		destination = d;
		objKind = kind;
	}
	public void setMapObjImage( int s, int d, int kind)
	{
		start = s;
		destination = d;
		objKind = kind;
	}
}