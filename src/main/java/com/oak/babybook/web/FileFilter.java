package com.oak.babybook.web;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implements java.io.FilenameFilter to enable filtering
 * of filenames. Has two custom variables to hold the
 * employee ID and the allowed image types. 
 */
public class FileFilter implements FilenameFilter{
    private String _employeeID = "";
    private ArrayList _imageTypes;
    
    /**
     * Checks the filename based on it starting with the employee ID
     * and ending with the correct image type extension.
     * 
     * @param file
     * @param name
     * @return boolean true if filename passes check, false otherwise
     */
    public boolean accept(File file, String name){
        boolean startsWith = false;
        boolean endsWith = false;
        
        startsWith = name.startsWith(getEmployeeID());
        
        Iterator iterator = getImageTypes().iterator();
        while (iterator.hasNext()){
            if (name.toLowerCase().endsWith((String) iterator.next())){
                endsWith = true;
                break;
            }
        }
        return startsWith && endsWith;
    }
    
    public String getEmployeeID(){
        return _employeeID;
    }
    
    public void setEmployeeID(String value){
        _employeeID = value;
    }
    
    public ArrayList getImageTypes(){
        return _imageTypes;
    }
    
    public void setImageTypes(ArrayList value){
        _imageTypes = value;
    }
}
