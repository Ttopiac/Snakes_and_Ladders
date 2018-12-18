//*******************************************************************
//*  Network Programming - Unit 5 Remote Method Invocation          *
//*  Program Name: SnakeLaddersServer								*
//*  The program is the RMI server. It binds the RMIImpl  			*
//*    with name server.                                            *
//*  2016.05.23                                                     *
//*******************************************************************
import java.rmi.*;
import java.rmi.server.*;

public class SnakeLaddersServer
{
	// Bind Server and Registry
	public static void main(String args[])
	{
		//System.setSecurityManager(new RMISecurityManager());
		try
		{
			SnakeLaddersRMIImpl name = new SnakeLaddersRMIImpl();
			System.out.println("Registering ...");
			Naming.rebind("SnakeLadders", name);	// arithmetic is the name of the service
			System.out.println("Register success");
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}