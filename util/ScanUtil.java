package util;

import java.util.Scanner;

public class ScanUtil {
	
	private static Scanner s = new Scanner(System.in);
	
	public static String nextLine(){ //문자열 입력받는것
		return s.nextLine();
	}
	
	public static int nextInt(){
		return Integer.parseInt(s.nextLine()); //숫자입력받는 것
	}

}
