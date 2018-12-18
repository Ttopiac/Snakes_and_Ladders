import java.util.*;

class RoomList extends LinkedList<Room> {
	
	int AddRoom( int memberIndex, String rN)
	{
		Room newRoom = new Room( rN);
		Room room=null;
		if( this.size() == 0){
			this.add( newRoom);
			newRoom.playerList.AddPlayer( memberIndex, 1);
		}
		else{
			for(int i=0; i<this.size(); i++){
				room = this.get(i);
				if( room.roomName.compareTo( newRoom.roomName) == 0){
					return -2;  //same roomName and return -2
				}
			}
			this.add( newRoom);
			newRoom.playerList.AddPlayer( memberIndex, 1);
		}
		
		return this.size()-1; //Add Room Successfully and return Room Index
	}
	int InRoom( int memberIndex, String rN){
		for(int roomIndex=0; roomIndex<this.size(); roomIndex++){
			Room room = this.get( roomIndex);
			if( room.roomName.compareTo( rN) == 0 && room.playerList.size()<4){
				room.playerList.AddPlayer( memberIndex, 1);
				return roomIndex;
			}
		}
		return -2; //wrong account and return -2
	}
}