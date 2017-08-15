package com.pentb.asseditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by pentonbin on 17-8-14.
 */
public class AssReader {

    public AssReader() {
    }

    private static BufferedReader getReader(String fileName) {
        try {
            return new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void read(String fileName, EditCallback callback) {
        if (fileName == null || fileName.trim().length() == 0) {
            throw new IllegalArgumentException("set up the file path correctly");
        }
        if (callback == null) {
            System.out.println("callback is null, nothing to execute");
            return;
        }
        File file = new File(fileName);
        if (!isAssFile(file)) {
            System.out.println("the wrong file type!");
            return;
        }
        BufferedReader reader = getReader(fileName);
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                callback.edit(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeIO(reader);
        }
    }

    private static boolean isAssFile(File file) {
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (fileType.equalsIgnoreCase("ass")) {
                return true;
            }
        }
        return false;
    }

    public static interface EditCallback {
        public void edit(String line);
    }
}
