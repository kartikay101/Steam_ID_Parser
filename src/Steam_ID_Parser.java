import java.util.Scanner;
import java.util.regex.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Steam_ID_Parser {

	static String UserID_Raw;
	static String UserID_64;
	final static String User_Agent= "Mozilla/5.0";
	public static String getID(){
		
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter Steam ID\n");
		UserID_Raw=sc.nextLine();
		sc.close();
		int length=UserID_Raw.length();
		
		// if the user copies the url directly , removing the last '/' after the profile
		if(UserID_Raw.charAt(length-1)=='/'){
			UserID_Raw=UserID_Raw.substring(0,length-1);
		}
		String result=isValid(UserID_Raw);
		if(result.equals("Mismatched ID")){
			return "Invalid Input";
		}
		else{
			return getID_64(result); 
		}
		
	}
	
	private static String isValid(String UserID_Raw){
		
		if(UserID_Raw.matches("(?:https?:\\/\\/)?(www.)?steamcommunity\\.com\\/(?:profiles|id)\\/\\w+")){
			UserID_64=UserID_Raw;
			return UserID_64;
		}
		else if(UserID_Raw.matches("\\d{17}")){
				return "http://steamcommunity.com/profiles/"+UserID_Raw;
		}
		else if(UserID_Raw.matches("\\w+")){
				UserID_64 ="http://www.steamcommunity.com/id/"+UserID_Raw;
				return UserID_64;
		}
		else{
				return "Mismatched ID";
		}
	}
	private static String getID_64(String UserID_Raw){
		
		String steamID_64="gg";
		
		try{
			// xml file can be requested through an http protocol only
			String Addr;
			
			if(UserID_Raw.contains("http")){
				Addr=(UserID_Raw+"/?xml=1").replace("https", "http");
			}
			else{
				Addr="http://"+UserID_Raw+"/?xml=1";
			}
			URL steamURL = new URL(Addr);
			
			System.out.println("Checking this "+Addr);
			
			HttpURLConnection sendReq = (HttpURLConnection) steamURL.openConnection();

			sendReq.setRequestMethod("GET");

	
			sendReq.setRequestProperty("User-Agent", User_Agent);

			int responseCode = sendReq.getResponseCode();
			
			
			if(responseCode == HttpURLConnection.HTTP_OK){
				Scanner sc=new Scanner(sendReq.getInputStream());
				steamID_64=sc.nextLine()+"";
				steamID_64=sc.nextLine()+"";
				sc.close();
				steamID_64=steamID_64.trim().substring(11,28);
			}
			      
		}
		catch (java.util.NoSuchElementException e){
			// If the entered id is not valid, the Scanner class throws this exception
			Pattern steamID = Pattern.compile("profiles\\/\\d{17}");
			Matcher UID = steamID.matcher(UserID_Raw);
			if(UID.find()){
				int len=UserID_Raw.length();
				return getID_64("http://steamcommunity.com/id/"+UserID_Raw.substring(len-18,len));
				
			}
			return "Invalid Input";
		}
		catch (Exception e){
			System.out.println("Exception : "+e);
			System.exit(0);	
		}
		return steamID_64;
	}
	public static void main(String args[]){
		
		//Steam_ID_Receiver s = new Steam_ID_Receiver();
		//testing using main function
		String received=getID();
		System.out.println(received);
		
	}
	
	
}

