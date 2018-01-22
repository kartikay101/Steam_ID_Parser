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
		
		
		if(isValid(UserID_Raw)){
		UserID_64 = parseID(UserID_Raw);
		return UserID_64;
		}
		else{	
			return "-1";
		}
	}
	
	private static boolean isValid(String UserID_Raw){
		
		Pattern valid = Pattern.compile("(?:https?:\\/\\/)?steamcommunity\\.com\\/(?:profiles|id)\\/\\w+");
		Matcher match = valid.matcher(UserID_Raw);
		
		return match.matches();
	}
	private static String parseID(String UID_Raw){
	
		
		Pattern steamID = Pattern.compile("\\d{17}");
		Matcher UID = steamID.matcher(UID_Raw);
		
		
		String extracted_ID="";
		if(UID.find()){
		    extracted_ID=UID.group(0);
		}
		else{
		
// // wrote this code, don't feel like deleting it
//			Pattern custURL = Pattern.compile("\\w{1,}$");
//			Matcher URL = custURL.matcher(UID_Raw);
//			
//			if(URL.find()){
//				custom_URL=URL.group();
//			}
			
			extracted_ID= getID_64(UID_Raw);
		}
		return extracted_ID;
		
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
			
			
			//System.out.print(responseCode+sendReq.getHeaderField("Location"));
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
			System.out.println("The entered steam ID is invalid ");
			System.exit(0);
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

