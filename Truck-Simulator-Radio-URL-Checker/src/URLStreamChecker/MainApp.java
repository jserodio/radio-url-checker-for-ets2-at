package URLStreamChecker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

public class MainApp {

	public static void main(String[] args) {
		try {
			 // We open live_streams.sii where all our radios are.
            Scanner entrada = new Scanner(new FileReader("live_streams.sii"));
			String linea;

			System.out.println("Opening the live_streams.sii file.");
			
			while (entrada.hasNext()) {
				// Reading first line of the file
				linea = entrada.nextLine();				
				if(linea.trim().length() == 0){
					continue;
				}
				// Split the line in to an array f[] with different possitions
				// Before splitting
				// linea ⬅️   stream_data[]: "http://radio.truckers.fm/stream|truckers.fm|Bonus|ETS2MP|128|1"
				// After splitting
				// f[1] ⬅️   http://radio.truckers.fm/stream|truckers.fm|Bonus|ETS2MP|128|1
				String f[] = linea.split("\"");
				
				if(f.length>1){
					// Split the f[1] in to an array g[] with different possitions
					// Before splitting
					// f[1] ⬅️   http://radio.truckers.fm/stream|truckers.fm|Bonus|ETS2MP|128|1
					// After splitting
					// g[0] ⬅️   http://radio.truckers.fm/stream
					// stream ⬅️    g[0]
					String g[] = f[1].split("\\|");
					String stream = g[0];					
					
					int code = isURLbroken(stream);
					switch (code){
						case 404: 	System.out.println("Link down: ");
									System.out.println(stream);
									System.out.println("Error code " + code +'\n');
							break;
						case 0: 	System.out.println("Link down: ");
									System.out.println(stream);
									System.out.println("Error code " + code +'\n');
							break;
						case 1:	 	System.out.println("Link down: ");
									System.out.println(stream);
									System.out.println("Error code " + code +'\n');
							break;
						case -1: 	System.out.println("Warning: ");
									System.out.println(stream);
									System.out.println("Error code " + code +'\n');
							break;
						default:	break;								
					}
				}
			}
			System.out.println("Finished.");

			// Closing the file
			entrada.close();	
			
		} catch (FileNotFoundException e) {
			System.out.println("Can't find the file: live_streams.sii");
			System.out.println("Make sure the file and this app are in the same folder.");
			System.out.println("Email me ➡️ serodio.jose@protonmail.ch");
		} catch (Exception e) {
			System.out.println("Verify you didn't open the live_streams.sii with a notepad.");
			System.out.println("Email me ➡️ serodio.jose@protonmail.ch");
			e.printStackTrace();
		}
	}

	/**
	 * The following method has partly been taken (and adapted) from the source below: 
	 * http://computing.dcu.ie/~humphrys/Notes/Networks/java.html
	 * 
	 * This is an auxiliary method called by the processLinks method. It ascertains whether a link is 
	 * broken, based on it having a connection timeout of over 17 seconds or a HTTP response code of 404.
	 *
	 * @author Janice Brogan (https://github.com/janicebrogan/javaLinkChecker)
	 * @param  singleLink  the URL that is to be assessed
	 * @return  		   returns the response code resulting from having connected to the URL, or 1 or 2 
	 * 					   if the link times out. Returns 0 if another Exception was thrown
	 */
	private static int isURLbroken(String singleLink) throws Exception {
		HttpURLConnection c = null;
		try {
			// Convert singleLink to URL.
			URL url = new URL(singleLink);
			
			// Open the connection to url.
			c = (HttpURLConnection) url.openConnection();
			
			// Set the connection timeout: if unable to connect within 17 seconds, connection will time out.
			c.setConnectTimeout(5000);
			
			// Return the response code resulting from having connected to the URL.
			// It will return 200 and 401 respectively. Returns -1 if no code can be discerned from the response (i.e., the response is not valid HTTP).
			return c.getResponseCode();
		
		} catch (ConnectException e) {
			// Return 1 or 2 if the connection times out.
			return 1;
		} catch (SocketTimeoutException e) {
			return 2;
		} catch (Exception e) {
			// Return 0 if another Exception was thrown.
			return 0;
		}
		finally {
			// If the connection is open, disconnect.
			if (c != null) {
			c.disconnect();
			}
		}
	}
}
