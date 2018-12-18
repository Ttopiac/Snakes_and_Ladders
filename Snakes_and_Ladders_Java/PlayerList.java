import java.util.*;

class PlayerList extends LinkedList<Player> {
	int AddPlayer(int mI, int loc)
	{
		Player newPlayer = new Player( mI, loc);
		Player player;
		if( this.size() == 0){
			this.add( newPlayer);
		}
		else{
			for(int i=0; i<this.size(); i++){
				player = this.get(i);
				if( player.memberIndex==newPlayer.memberIndex){
					return -2;
				}
			}
			this.add( newPlayer);
		}
		System.out.println("Player: " + mI + "\n");
		return this.size()-1;
	}
	int RemovePlayer( int mI){
		Player player;
		if( this.size() == 0){
			return -1;
		}
		else{
			for(int i=0; i<this.size(); i++){
				player = this.get(i);
				if( player.memberIndex==mI){
					this.remove(i);
					return i;
				}
			}
		}
		return -1;
	}
}
