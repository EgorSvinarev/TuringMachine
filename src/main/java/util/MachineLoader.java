package util;

import java.io.*;
import java.util.*;
import java.io.BufferedReader;

import turing.StateSymbolPair;
import turing.TransitionRule;
import turing.TuringMachine;
import turing.tape.Tape;

public class MachineLoader
{
    
	public static void saveAlgo(String file, String[] lines) throws IOException{
    	FileWriter fileWriter = new FileWriter(setFileFormat(file));
    	PrintWriter printWriter = new PrintWriter(fileWriter);
    	
    	for (String line: lines) {
    		printWriter.print(line + "\n");
    	}
    	
    	printWriter.close();
    }
    
    public static String[] loadAlgo(String file) throws IOException {
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
    
    public static String setFileFormat(String fileName) {
    	int dotIndex = fileName.indexOf(".");
    	int formatIndex = fileName.indexOf(".mt");
    	
    	if (dotIndex == -1) {
    		dotIndex = fileName.length();
    	}
    	
    	if (formatIndex != -1) {
    		return fileName;
    	}
    	
    	return fileName.substring(0, dotIndex) + ".mt";
    }
}
