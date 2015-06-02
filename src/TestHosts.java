import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;


public class TestHosts {
	DNSDB db;
	@Before
	public void setup() {
		db = new DNSDB();
		db.load(new File("host-list.txt"));
	}
	public void testHostToIP(String text) {
		Integer ip = db.findIP(text);
		if (ip == null) {
			fail("Unable to find an IP for text: " + text);
		}
	}
	public void testIpToHost(int ip) {
		String host = db.findHostName(ip);
		if (host == null) {
			fail("Unable to find an IP for text: " + ip + " (" + IPToString(ip) + ")");
		}
	}

	@Test
	public void testHostToIP() {
		try{
			BufferedReader data = new BufferedReader(new FileReader("host-list.txt"));
			for (int i = 0; i < 6; ++i) {
				String line = data.readLine();
				if (line==null) {break;}
				String[] pair = line.split("\t");
				String host = pair[0];
				testHostToIP(pair[0]);
			}
			
		} catch(IOException e){System.out.println("Fail: " + e);}
}
	@Test
	public void testIPToHost() {
		try{
			BufferedReader data = new BufferedReader(new FileReader("host-list.txt"));
			for (int i = 0; i < 2; ++i) {
				String line = data.readLine();
				if (line==null) {break;}
				String[] pair = line.split("\t");
				String host = pair[0];
				testIpToHost(stringToIP(pair[1]));
			}
			
		} catch(IOException e){System.out.println("Fail: " + e);}
}
	// Utilities

	public static Integer stringToIP(String text){
		String[] bytes = text.trim().split("\\.");

		if(bytes.length != 4)
			return null;

		try {
			int ip = 0;
			for(int i=0; i<4; i++){
				int b = Integer.parseInt(bytes[i].trim());
				ip |= b << (24 - 8*i);
			}

			return ip;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String IPToString(int ip){
		StringBuilder sb = new StringBuilder();
		for(int i=3; i>=0; i--){
			//sb.append((ip & (0xFF << (i*8))) >> (i*8));
			sb.append((ip >> (i*8)) & 0xFF);
			if(i > 0)
				sb.append('.');
		}
		return sb.toString();
	}
}
