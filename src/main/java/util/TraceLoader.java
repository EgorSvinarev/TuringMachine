package util;

import java.io.*;

public class TraceLoader {

	public static void saveTrace(String file, String[] lines) throws IOException{
		FileWriter fileWriter = new FileWriter(setFileFormat(file));
    	PrintWriter printWriter = new PrintWriter(fileWriter);
    	
    	for (String line: lines) {
    		printWriter.print(line + "\n");
    	}
    	
    	printWriter.close();
    	
	}
	
	public static String setFileFormat(String fileName) {
    	int dotIndex = fileName.indexOf(".");
    	int formatIndex = fileName.indexOf(".track");
    	
    	if (dotIndex == -1) {
    		dotIndex = fileName.length();
    	}
    	
    	if (formatIndex != -1) {
    		return fileName;
    	}
    	
    	return fileName.substring(0, dotIndex) + ".track";
    }
	
}
