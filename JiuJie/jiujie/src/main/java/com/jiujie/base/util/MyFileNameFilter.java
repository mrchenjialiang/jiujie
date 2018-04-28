package com.jiujie.base.util;

import java.io.File;
import java.io.FilenameFilter;

public class MyFileNameFilter implements FilenameFilter {
    private String[] types;
    public MyFileNameFilter(String... types){
        this.types = types;
    }
    public boolean accept(File dir, String name){
        for (String type:types){
            boolean isThisType = name.toLowerCase().endsWith(type.toLowerCase());
            if(isThisType){
                return true;
            }
        }
        return false;
    }
}
