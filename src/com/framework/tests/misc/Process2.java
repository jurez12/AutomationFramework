package com.framework.tests.misc;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Process2 {
	public static void main(String args[]) {
		System.out.println("Done");
	    try {
	      ProcessBuilder proc =
	        new ProcessBuilder("ls", " -al");
	      proc.start();
	    } catch (Exception e) {
	      System.out.println("Error executing notepad.");
	    } 
	    
	    

   }

}
