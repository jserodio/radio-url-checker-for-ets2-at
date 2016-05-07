package URLStreamChecker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class File {
	// Singleton pattern https://en.wikipedia.org/wiki/Singleton_pattern
	private static final File INSTANCE = new File();
	
	private File () {}
	
	public static File getInstance() {
		return INSTANCE;
	}
	
	// This is the ArrayList where each radio url is going to be stored
	private static final ArrayList<String> STREAM = new ArrayList<String>();

	/**
	 * The following method will load the live_streams.sii file to search for
	 * the radio URLs. It will return an iterator containing each radio link.
	 *
	 * @param  path		the location your live_streams.sii is.
	 * @return			String iterator containing each radio link. 
	 * 					
	 */
	public ArrayList<String> load (String path) {
		try {
		
			// We open live_streams.sii where all our radios are.
		    Scanner input = null;
			input = new Scanner(new FileReader(path));
			String line;
		
			System.out.println("\nOpening the live_streams.sii file.");
			
			while (input.hasNext()) {
				// Reading first line of the file
				line = input.nextLine();				
				if(line.trim().length() == 0){
					continue;
				}
				// Split the line in to an array f[] with different possitions
				// Before splitting
				// linea ⬅️   stream_data[]: "http://radio.truckers.fm/stream|truckers.fm|Bonus|ETS2MP|128|1"
				// After splitting
				// f[1] ⬅️   http://radio.truckers.fm/stream|truckers.fm|Bonus|ETS2MP|128|1
				String f[] = line.split("\"");
				
				if(f.length>1){
					// Split the f[1] in to an array g[] with different possitions
					// Before splitting
					// f[1] ⬅️   http://radio.truckers.fm/stream|truckers.fm|Bonus|ETS2MP|128|1
					// After splitting
					// g[0] ⬅️   http://radio.truckers.fm/stream
					// stream ⬅️    g[0]
					String g[] = f[1].split("\\|");
					STREAM.add(g[0]);	
				}
			}
			input.close();
			
			System.out.println("The file has been loaded." + '\n');
		
			return STREAM;
			
		} catch (FileNotFoundException e) {
			System.out.println('\n'+ "Can't find the file: live_streams.sii");
			System.out.println("The path provided was: " + path);
			System.out.println("Make sure the file and this app are in the same folder.");
			System.out.println("Email me ➡️ serodio.jose@protonmail.ch");
			return null;
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
	public int isURLbroken(String singleLink) throws Exception {
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