package com.jiujie.base.util;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter {
    private String[] types;
    public MyFileFilter(String... types){
        this.types = types;
    }

    @Override
    public boolean accept(File file) {
        for (String type:types){
            boolean isThisType = file.getName().toLowerCase().endsWith(type.toLowerCase());
            if(isThisType){
                return true;
            }
        }
        return false;
    }
}
