package com.accolite.mini_au;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

interface TelephoneInterface
{
	boolean call(String num);
	int pressButton(char num);
}
class Customer
{
	String id;//denotes the phone number of this customer;
	CallHistory history;
	String previousNumber;
	Customer(CallHistory history,String id,String previousNumber)
	{
		this.history=history;
		this.previousNumber=previousNumber;
		this.id=id;
	}
	String makeCall()// method to make a phone call
	{
		System.out.println("enter a number to call");
		Scanner s=new Scanner(System.in);
		String num=s.next();
		Telephone t=new Telephone();
		t.phoneNumber=num;
		if(num.equals("R")) //if the user wants to Redial, then he/she presses 'R'
		{
			System.out.println("redialling");
			if(previousNumber=="") {System.out.println("can't perform this operation");return null;}
			Telephone t1=new Telephone();
			t1.phoneNumber=previousNumber;
			
			if(t1.call(previousNumber))
			{
				history.store(id,t1);
				return previousNumber;
			}
		}
		
		while(!t.validatePhoneNumber()) // To validate the dialled number
		{
			System.out.println("enter a valid number to call");
			num=s.next();
			t.phoneNumber=num;
		}
		
		if(id.equals(num)) // if the user dials his/her own number
		{
			System.out.println("Dude! You can't call your own number");
			return null;
		}
		t.call(num);
		history.store(id,t);
		Telephone t1=new Telephone();
		t1.incoming=0;
		t1.phoneNumber=id;
		t1.time=new Date();
		previousNumber=num;
		history.store(num, t1);// storing the call and the time of call
		return num;
	}
}
//Class to store the call history of all the customers
class CallHistory
{
	HashMap<String, ArrayList<Telephone>> hm;// structure to store the customer and his/her call history
	CallHistory()
	{
		hm=new HashMap<String, ArrayList<Telephone>>();
	}
	void store(String id,Telephone call) // method to store the call
	{
		if(hm.containsKey(id))
		{
			ArrayList<Telephone> tel=hm.get(id);
			tel.add(call);
			hm.remove(id);
			hm.put(id, tel);
		}
		else
		{
			ArrayList<Telephone> tel=new ArrayList<Telephone>();
			tel.add(call);
			hm.put(id, tel);
		}
	}
	void displayCallHistoryOfCustomer(String id) // method to display call history of a particular customer
	{
		try{
			ArrayList<Telephone> history=hm.get(id);
			Iterator<Telephone> i=history.iterator();
			while(i.hasNext())
			{
				Telephone t=i.next();
				t.display();
			}
		}catch(NullPointerException e)
		{
			System.out.println("no call history available");
		}
	}
	void displayEntireHistory() // method to display entire call history
	{
		Set<String> keyset=hm.keySet();
		Iterator<String> i=keyset.iterator();
		while(i.hasNext())
		{
			String id=i.next();
			System.out.println("phone number: "+id);
			System.out.println("-------------call history----------");
			displayCallHistoryOfCustomer(id);
		}
	}
}
// Class that implements Telephone interface and provides us with the functionality of a telephone
class Telephone implements TelephoneInterface{
	String phoneNumber;
	Date time;
	int incoming;
	Character arr[]={'1','2','3','4','5','6','7','8','9','0','*','#'};
	boolean validatePhoneNumber() // method to validate the dialled number
	{
		
		if(phoneNumber.length()==8 || phoneNumber.length()==10) // checks if the length of the number is 8 (land line) or 10(mobile number)
		{
			for(int i=0;i<phoneNumber.length();i++)
			{
				if(pressButton(phoneNumber.charAt(i))==1);
				else return false;
			}
			return true;
		}
		return false;
	}
	public boolean call(String num) // function to call a number
	{
			System.out.println("calling number: "+phoneNumber);
			time=new Date();// stores the time of the call
			incoming=1;//if incoming=1, then it is an out going call else it's an incoming call
			return true;
	}
	public int pressButton(char num) // method to press a button
	{
		ArrayList<Character> listNums=new ArrayList<Character>(Arrays.asList(arr));
		if(listNums.contains(num))
		{
			return 1;
		}
		return 0;
	}
	void display()// method to display the phone number, the time of the call, whether it's incoming or outgoing
	{
		System.out.print(""+phoneNumber);
		System.out.print("\t"+time);
		if(incoming==1) System.out.println("\tout going");
		else System.out.println("\tin coming");
	}
}
// Class that uses the above classes and provides us with the menu to perform all the operations
public class ImplementTelephone
{
	public static void main(String args[])
	{
		int choice;
		CallHistory ch=new CallHistory();
		
		HashMap<String,String> previousNumberStorage=new HashMap<String,String>();
		while(true){
			System.out.println("press 1-make a call\n      2-total call history\n      3-call history of a customer\n      4-exit\n");
			Scanner s1=new Scanner(System.in);
			choice=s1.nextInt();
			if(choice==1)
			{
				Telephone t1=new Telephone();
				System.out.println("enter your phone number");
				Scanner s=new Scanner(System.in);
				String num=s.nextLine();
				t1.phoneNumber=num;
				while(!t1.validatePhoneNumber())
				{
					System.out.println("enter a valid phone number");
					num=s.nextLine();
					t1.phoneNumber=num;
				}
				String pnum="";
				pnum=(String)previousNumberStorage.get(num);
				Customer c=new Customer(ch,num,pnum);
				String num1=c.makeCall();
				if(previousNumberStorage.containsKey(num))
				{
					previousNumberStorage.remove(num);
					previousNumberStorage.put(num, num1);
				}
				else previousNumberStorage.put(num, num1);
				
			}
			else if(choice==4) break;
			else if(choice==3)
			{
				System.out.println("enter customer's phone number");
				Scanner s=new Scanner(System.in);
				String id=s.nextLine();
				ch.displayCallHistoryOfCustomer(id);
				
			}
			else if(choice==2)
			{
				ch.displayEntireHistory();
			}
			else ;
			
		}
		
	}
}
