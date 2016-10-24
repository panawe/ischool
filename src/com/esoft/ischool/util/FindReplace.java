package com.esoft.ischool.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FindReplace {
	private List<File> files = new ArrayList<File>();
	
	 public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public void walk( String path ) {

	        File root = new File( path );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	                walk( f.getAbsolutePath() );
	                //System.out.println( "Dir:" + f.getAbsoluteFile() );
	            }
	            else {
	            	if(f.getName().endsWith("java") ||f.getName().endsWith("js")){

		                System.out.println( "File:" + f.getAbsoluteFile() );
		                if(!f.getName().endsWith("FindReplace.java"))
		                	files.add(f);
	            	}
	            }
	        }
	    }

	 public  void replaceFileString(String oldStr, String newStr, File f) throws IOException {
		 Path path = Paths.get(f.getAbsolutePath());
		 Charset charset = StandardCharsets.UTF_8;
		 String content = new String(Files.readAllBytes(path), charset);
		 content = content.replaceAll(oldStr, newStr);
		 Files.write(path, content.getBytes(charset));
		}
	    public static void main(String[] args) {
	    	FindReplace fw = new FindReplace();
	        fw.walk("C:\\My Projects\\ischool" );
	        for(File f:fw.getFiles()){
	        	try {
	        		fw.replaceFileString("com.ukad.", "com.esoft.ischool.", f);
					//fw.replaceFileString("localhost:8080/ukadtogo", "www.arelbou.com", f);
					//fw.replaceFileString("www.arelbou.com", "localhost:8080/ukadtogo", f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
}
