//*******************************************************************
//*  Network Programming - Project Snakes & Ladders		            *
//*  Program Name: Interface                              			*
//*  The program defines the interface for Java RMI.                *
//*  2016.05.23                                                     *
//*******************************************************************
import java.rmi.Remote;
import java.util.*;
public interface SnakeLaddersInterface extends Remote
{
	public int Register( String acc, String pass) throws java.rmi.RemoteException;
	public int Login( String acc, String pass) throws java.rmi.RemoteException;
	public void Logout( int memberIndex) throws java.rmi.RemoteException;
	public LinkedList<String> GetRoomList() throws java.rmi.RemoteException;
	public int CreateRoom( int memberIndex, String roomName) throws java.rmi.RemoteException;
	public int EnterRoom( int memberIndex, String roomName) throws java.rmi.RemoteException;
	public int LeaveRoom( int memberIndex, int roomIndex) throws java.rmi.RemoteException;
	public LinkedList<Integer> GetMapObjImage( int roomIndex, int MapObjImageIndex) throws java.rmi.RemoteException;
	public int GetMapObjImageSize( int roomIndex) throws java.rmi.RemoteException;
	public LinkedList<Integer> GetPlayer( int roomIndex, int PlayerIndex) throws java.rmi.RemoteException;
	public int GetPlayerSize( int roomIndex) throws java.rmi.RemoteException;
	public Boolean MyTurn( int memberIndex, int roomIndex) throws java.rmi.RemoteException;
	public void ThrowDice( int memberIndex, int roomIndex) throws java.rmi.RemoteException;	
}
