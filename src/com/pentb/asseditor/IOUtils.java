package com.pentb.asseditor;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by pentonbin on 17-8-14.
 */
public class IOUtils {

    public static void closeIO(Closeable obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
