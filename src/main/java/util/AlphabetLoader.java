package util;

import java.io.*;
import java.util.*;

public class AlphabetLoader {

	public static String[] loadAphabet(String file) throws IOException {
		ArrayList<String> linesList = new ArrayList<String>();
    	BufferedReader br = new BufferedReader(new FileReader(file));
	    for(String line; (line = br.readLine()) != null; ) {
	    	linesList.add(line);
	    }
	    
	    String[] lines = new String[linesList.size()];
	    
	    for (int i = 0; i < lines.length; i++) {
	    	lines[i] = linesList.get(i);
	    }
	    
	    
	    return lines;
	}
	
}
