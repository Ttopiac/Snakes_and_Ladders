import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.*;


public class SnakeLaddersClient{ 

	public static String state;
	public static MainFrame frame;
	public static MapObjImageList mapObjList = new MapObjImageList();
	public static PlayerList playerList = new PlayerList();
	public static Room room;
	public static RoomList roomList;
	public static SnakeLaddersInterface sli = null;
	public static int memberIndex;
	public static int roomIndex;
	public static LinkedList<String> roomNameList = new LinkedList<String>();
    public static void main(String[] args) { 
		try
	    {
	    	sli = (SnakeLaddersInterface) Naming.lookup("rmi://127.0.0.1/SnakeLadders");
	    	System.out.println("RMI server connected");
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Server lookup exception: " + e.getMessage());
	    }
		
		state = "logging";
		SnakeLaddersClient client = new SnakeLaddersClient();
		frame = new MainFrame(client);
		frame.changePage(state);
		
		while(true){
			try
			{
				int waitingTime = 500;
				System.out.println("Sleep " + waitingTime + " milliseconds!!");
				Thread.sleep(waitingTime);
			}
			catch(InterruptedException e)
			{
				System.out.println("Interrupt!!");
			}
			switch(state){
				case "playing":
					UpdateMapObj();		
					break;
				case "room":
					UpdateRoomNameList();
					break;
			}

			
		}
    } 
	public void action( String cmd, LinkedList<String> messages){
		switch( cmd){
			case "Login":	
				try{
					memberIndex = sli.Login( messages.get(0), messages.get(1));
					if( memberIndex >= 0){
						state = "room";
						frame.changePage(state);	
					}else{
						
					}
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "Reg":
				try{
					memberIndex = sli.Register( messages.get(0), messages.get(1));
					if( memberIndex >= 0){
						state = "room";
						frame.changePage(state);	
					}else{
						
					}
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}	
				break;
			case "LogOut":
				try{
					sli.Logout( memberIndex);
					memberIndex = -1;
					state = "logging";
					frame.changePage(state);
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "In":
				try{
					roomIndex = sli.EnterRoom( memberIndex, messages.get(0));
					System.out.println("RoomIndex: " + roomIndex);
					if( roomIndex >= 0){
						state = "playing";
						frame.changePage(state);	
						UpdateMapObj();
					}else{
						
					}
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "Create":
				try{
					roomIndex = sli.CreateRoom( memberIndex, messages.get(0));
					System.out.println("RoomIndex: " + roomIndex);
					if( roomIndex >= 0){
						state = "playing";
						frame.changePage(state);	
						UpdateMapObj();
					}else{
						
					}
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "Dice":
				try{
					if( sli.MyTurn( memberIndex, roomIndex) == true){
						sli.ThrowDice( memberIndex, roomIndex);
					}
					UpdateMapObj();	
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "Leave":
				try{
					if(sli.LeaveRoom( memberIndex, roomIndex) == 1){
						mapObjList.clear();
						playerList.clear();
						roomIndex = -1;
						state = "room";
						frame.changePage(state);	
					}
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case "close":
				try{
					if( roomIndex>=0){
						if(sli.LeaveRoom( memberIndex, roomIndex) == 1){
							mapObjList.clear();
							playerList.clear();
							roomIndex = -1;	
						}
					}
					if( memberIndex>=0){
						sli.Logout( memberIndex);
						memberIndex = -1;
					}
				}
				catch(Exception e)
				{
					System.out.println("SnakeLaddersServer exception: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			default:
				System.out.println( cmd);
				break;
		}
	}
	public static void UpdateRoomNameList(){
		try{
			roomNameList.clear();
			roomNameList = sli.GetRoomList();
			frame.roomChoicePanel.UpdateRoomMessBoard();
		}
		catch(Exception e)
		{
			System.out.println("SnakeLaddersServer exception: " + e.getMessage());
			e.printStackTrace();
		}

	}
	public static void UpdateMapObj(){
		try{
			int moilSize = sli.GetMapObjImageSize(roomIndex);
			mapObjList.clear();
			for( int i=0; i<moilSize; i++){
				LinkedList<Integer> moil = sli.GetMapObjImage( roomIndex, i);
				mapObjList.AddObjImage( moil.get(0), moil.get(1), moil.get(2));
			}
			int plSize = sli.GetPlayerSize(roomIndex);
			playerList.clear();
			for( int i=0; i<plSize; i++){
				LinkedList<Integer> pl = sli.GetPlayer( roomIndex, i);
				playerList.AddPlayer( pl.get(0), pl.get(1));
			}
			System.out.println("RoomIndex: " + roomIndex);
			frame.playingPanel.snakeLadderBoard.repaint();
		}
		catch(Exception e)
		{
			System.out.println("SnakeLaddersServer exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}