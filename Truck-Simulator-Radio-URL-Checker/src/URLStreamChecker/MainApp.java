package URLStreamChecker;

import java.util.Iterator;
import java.util.Scanner;

public class MainApp {
	
	public static void main(String[] args) throws Exception {
		try {
			// args[0] will be the path of the file i.e. "live_streams.sii"
			String path = args[0];

			System.out.println("Checking for broken links...");
			System.out.println("This could take a moment!");
			System.out.println("Please wait until it finishes.");
			
			// singleton pattern (see File.java for further information)
			Iterator<String> it = File.getInstance().load(path);
			// this string will be storing each radio URL
			String stream;
			
			while(it.hasNext()){
				// iterating trought each line of radio previously loaded from the file	
				stream = it.next();
				int code = File.getInstance().isURLbroken(stream);
				
				switch (code){
					case 404: 	System.out.println("Link down: ");
								System.out.println(stream);
								System.out.println("Code " + code +'\n');
						break;
					case 0: 	System.out.println("Link down: ");
								System.out.println(stream);
								System.out.println("Code " + code +'\n');
						break;
					case 1:	 	System.out.println("Link down: ");
								System.out.println(stream);
								System.out.println("Code " + code +'\n');
						break;
					case -1: 	System.out.println("Warning: ");
								System.out.println(stream);
								System.out.println("Code " + code +'\n');
						break;
					default:	break;								
				} // switch end
			} // while end
			
			System.out.println("Finished.");
			waitForUserInput();
		
		} catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Can't find the file: live_streams.sii");
			System.out.println("Make sure the file and this app are in the same folder.");
			System.out.println("Email me ➡️ serodio.jose@protonmail.ch");
			waitForUserInput();
			//e.printStackTrace();
		} catch(NullPointerException e1){
			System.out.println("");
			waitForUserInput();
			//e1.printStackTrace();
		}

	} // main
	
	private static void waitForUserInput(){
		System.out.println("Press any key to exit...");
		Scanner keyboard = new Scanner(System.in);
		keyboard.nextLine();
		keyboard.close();
	}
	
} // class
