package com.esoft.ischool.util;

public class Test {

	public static void main(String[] args){
		String albumNote="https://www.youtube.com/embed/ptmoLe6_Oh8";
		System.out.println((albumNote==null||albumNote.equals(""))?null:albumNote.split("/")[albumNote.split("/").length-1]);
	}
}
