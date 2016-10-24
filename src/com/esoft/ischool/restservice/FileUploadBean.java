package com.esoft.ischool.restservice;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.util.File;


@Component("fileUploadBean")
@Scope("session")
public class FileUploadBean{
    
    private ArrayList<File> files = new ArrayList<File>();
    private int uploadsAvailable = 1;
    private boolean autoUpload = true;
    private boolean useFlash = true;
    public int getSize() {
        if (getFiles().size()>0){
            return getFiles().size();
        }else 
        {
            return 0;
        }
    }

    public FileUploadBean() {
    }
    
    public void overrideFiles(File file){
    	files.clear();
    	files.add(file);
    }

    public void paint(OutputStream stream, Object object) throws IOException {
        stream.write(getFiles().get((Integer)object).getData());
    }
    public void listener(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        File file = new File();
        file.setLength(item.getData().length);
        file.setName(item.getFileName());
        file.setData(item.getData());
        if(files!=null&&files.size()<=0){
        	files.add(file);
    	}else{
    		files.clear();
    		files.add(file);    		
    	}
        //uploadsAvailable--;
    }  
      
    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(1);
        return null;
    }
    
    public String saveData() {
        files.clear();
        
        for(File file:files){
        	
        	//DocClient doc = new DocClient();
        	
        	//doc.setClient(client);
        	
        }
        
        setUploadsAvailable(1);
        return null;
    }
    
    public long getTimeStamp(){
        return System.currentTimeMillis();
    }
    
    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) { 
        this.files = files;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

}