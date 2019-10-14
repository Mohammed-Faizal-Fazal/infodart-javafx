package com.infodart.ui;

import java.io.IOException;

public class Test {

	public static void main(String[] args) {

	    try {
			Process proc = Runtime.getRuntime().exec("cmd /c osk.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

}
