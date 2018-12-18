import java.util.*;

class MapObjImageList extends LinkedList<MapObjImage> {
	void AddObjImage(int s, int d, int kind)
	{
		if( s<d-30){
			MapObjImage newMapObj = new MapObjImage( s, d, kind);	
			if( this.size()==0){
				this.add(newMapObj);
			}
			else{
				for(int i=0; i<this.size(); i++){
					MapObjImage mapObj = this.get(i);
					if( mapObj.start == newMapObj.start || mapObj.destination == newMapObj.destination || mapObj.start == newMapObj.destination || mapObj.destination == newMapObj.start){
						break;
					}
					else if( newMapObj.start==100 || newMapObj.destination==100 || newMapObj.start==1 || newMapObj.destination==1) {
						break;
					}
					else if( i==this.size()-1){
						this.add(newMapObj);
					}
				}	
			}
		}
	}
	void RandAddMapObj( int numOfLadders, int numOfSnakes){
		Random randomGenerator = new Random();
		while( this.size()<numOfLadders){
			int randomS = randomGenerator.nextInt(100)%100+1;	
			int randomD = randomGenerator.nextInt(100)%100+1;	
			this.AddObjImage( randomS, randomD, 0);
		}
		while( this.size()<numOfSnakes+numOfLadders){
			int randomS = randomGenerator.nextInt(100)%100+1;	
			int randomD = randomGenerator.nextInt(100)%100+1;	
			this.AddObjImage( randomS, randomD, 1);
		}
	}
}
