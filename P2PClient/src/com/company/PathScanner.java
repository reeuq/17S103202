package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mialiu on 9/17/15
 */
public class PathScanner {
    private String _path = null;
    private List<String> fileList = new ArrayList<String>();

    public PathScanner() {
        SetPath("");
    }

    public PathScanner(String path) {
        SetPath(path);
    }

    public boolean IsFolderPathRight(String filePath) {
        File f0 = new File(filePath);
        if (f0.exists()) {
            return f0.isDirectory() && f0.canRead();
        }
        return false;
    }

    public void SetPath(String path) {
        _path = path;

        if (IsFolderPathRight(path)) {

            try {
                File[] f1 = new File(path).listFiles();
                for (int i = 0; i < f1.length; i++) {
                    if (!f1[i].isDirectory()) {
                        //fileList.add(f1[i].getAbsolutePath());
                        fileList.add(f1[i].getName());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void PrintAllFiles() {
        if (0 == fileList.size()) {
            System.out.println("No Files found!");
        } else {
            for (int i = 0; i < fileList.size(); i++) {
                System.out.println(fileList.get(i));
            }
            System.out.println("Total: " + fileList.size() + " file(s).");
        }
    }

    public List<String> ListAllFiles() {
        return fileList;
    }
}
