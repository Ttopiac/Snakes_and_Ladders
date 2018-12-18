//*******************************************************************
//*  Network Programming - Project Snakes & Ladders		            *
//*  Program Name: RMIImpl                              			*
//*  The program implements the services defended in the interface, *
//*    Interface.java, for Java RMI.                      			*
//*  2016.05.23                                                     *
//*******************************************************************
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class SnakeLaddersRMIImpl extends UnicastRemoteObject implements SnakeLaddersInterface{
	RoomList roomList = new RoomList();
	MemberList memberList = new MemberList();
	public static Object 	memberListLock = new Object();
	public static Object 	roomListLock = new Object();
	// This implementation must have a public constructor.
	// The constructor throws a RemoteException.
	public SnakeLaddersRMIImpl() throws java.rmi.RemoteException
	{
		super(); 	// Use constructor of parent class
	}
		// Implementation of the service defended in the interface
	
	public int Register( String acc, String pass) throws java.rmi.RemoteException{	
		int registerState;
		synchronized( memberListLock)
		{
			registerState = memberList.AddMember( acc, pass);
		}
		return registerState;
	}
	
	public int Login( String acc, String pass) throws java.rmi.RemoteException{
		int authentState;
		synchronized( memberListLock)
		{
			authentState = memberList.Authentication( acc, pass);
		}
		return authentState;
	}
	public void Logout( int memberIndex) throws java.rmi.RemoteException{
		synchronized( memberListLock)
		{
			memberList.get( memberIndex).onLineState = false;
		}
	}
	public LinkedList<String> GetRoomList() throws java.rmi.RemoteException{
		synchronized( roomListLock)
		{
			LinkedList<String> rL = new LinkedList<String>();
			for(int i=0; i<roomList.size(); i++){
				rL.add(roomList.get(i).roomName);
			}
			return rL;
		}
	}
	public int CreateRoom( int memberIndex, String roomName) throws java.rmi.RemoteException{
		int createRoomState;
		synchronized( roomListLock)
		{
			createRoomState = roomList.AddRoom( memberIndex, roomName);
			if( createRoomState >= 0){
				memberList.get( memberIndex).inRoomState = true;
			}
		}
		return createRoomState;
	}
	public int EnterRoom( int memberIndex, String roomName) throws java.rmi.RemoteException{
		int enterRoomState;
		synchronized( roomListLock)
		{
			enterRoomState = roomList.InRoom( memberIndex, roomName);
			if( enterRoomState >= 0){
				memberList.get( memberIndex).inRoomState = true;
				if( roomList.get(enterRoomState).playerList.size() == 4 && roomList.get(enterRoomState).state==0){
					 roomList.get(enterRoomState).state = 1;
				}
				else if( roomList.get(enterRoomState).playerList.size() == 1 && roomList.get(enterRoomState).state==-1){
					 roomList.get(enterRoomState).state = 0;
				}
			}
		}
		return enterRoomState;
	}
	public int LeaveRoom( int memberIndex, int roomIndex) throws java.rmi.RemoteException{
		synchronized( roomListLock)
		{
			if( roomList.get(roomIndex).state<1){
				if( roomList.get( roomIndex).playerList.RemovePlayer( memberIndex)>=0 ){
					memberList.get( memberIndex).inRoomState = false;		
					return 1;
				}
				return -1;
			}
		}
		return 0;
	}
	public LinkedList<Integer> GetMapObjImage( int roomIndex, int MapObjImageIndex) throws java.rmi.RemoteException{
		synchronized( roomListLock){
			Room room = roomList.get( roomIndex);
			LinkedList<Integer> moil = new LinkedList<Integer>();
			moil.add(room.mapObjList.get(MapObjImageIndex).start);
			moil.add(room.mapObjList.get(MapObjImageIndex).destination);
			moil.add(room.mapObjList.get(MapObjImageIndex).objKind);
			return moil;	
		}		
	}
	public int GetMapObjImageSize( int roomIndex) throws java.rmi.RemoteException{
		synchronized( roomListLock){
			Room room = roomList.get( roomIndex);
			return room.mapObjList.size();	
		}
	}
	public LinkedList<Integer> GetPlayer( int roomIndex, int PlayerIndex) throws java.rmi.RemoteException{
		synchronized( roomListLock){
			Room room = roomList.get( roomIndex);
			LinkedList<Integer> pl = new LinkedList<Integer>();
			pl.add(room.playerList.get(PlayerIndex).memberIndex);
			pl.add(room.playerList.get(PlayerIndex).location);
			return pl;
		}		
	}
	public int GetPlayerSize( int roomIndex) throws java.rmi.RemoteException{
		synchronized( roomListLock){
			Room room = roomList.get( roomIndex);
			return room.playerList.size();
		}		
	}
	public Boolean MyTurn( int memberIndex, int roomIndex) throws java.rmi.RemoteException{
		synchronized( roomListLock){
			Room room = roomList.get( roomIndex);
			System.out.println("memberIndex: " + memberIndex);
			System.out.println("roomIndex: " + roomIndex);
			System.out.println("roomState: " + room.state);
			if( room.state > 0){
				System.out.println("roomState.memberIndex: " + room.playerList.get(room.state-1).memberIndex);
				if( room.playerList.get(room.state-1).memberIndex == memberIndex){
					return true;
				}else{
					return false;
				}	
			}else{
				return false;
			}
		}
	}
	public void ThrowDice( int memberIndex, int roomIndex) throws java.rmi.RemoteException{
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100)%6+1;
		Room room = roomList.get( roomIndex);
		int newloc = randomInt+room.playerList.get(room.state-1).location;

		if( newloc >= 100){
			room.playerList.get(room.state-1).location = 100;
			room.state = -1;
		}else{
			MapObjImageList moil = room.mapObjList;
			for( int i=0; i< moil.size(); i++){
				if( newloc == moil.get(i).start && moil.get(i).objKind == 0){
					newloc = moil.get(i).destination;
					break;
				}else if( newloc == moil.get(i).destination && moil.get(i).objKind == 1){
					newloc = moil.get(i).start;
					break;
				}
			}
			room.playerList.get(room.state-1).location = newloc;
			room.state = (room.state+1-1)%room.playerList.size()+1;
		}
	}
}