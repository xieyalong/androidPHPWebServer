package com.ayansoft.androphp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    public static int chmod(File paramFile, int paramInt) throws Exception {
        return ((Integer)Class.forName("android.os.FileUtils")
                .getMethod("setPermissions", new Class[] { String.class, int.class, int.class, int.class })
                .invoke(null, new Object[] { paramFile.getAbsolutePath(), Integer.valueOf(paramInt), Integer.valueOf(-1), Integer.valueOf(-1) }))
                .intValue();
    }

    public static File copyFromStream(String paramString, InputStream paramInputStream) {
        if (paramString == null || paramString.length() == 0) {
            Log.e("", "No script name specified.");
            return null;
        }
        File file = new File(paramString);
        if (!makeDirectories(file.getParentFile(), 493))
            return null;
        try {
            IoUtils.copy(paramInputStream, new FileOutputStream(file));
            return file;
        } catch (Exception e) {
            Log.e("", "", e);
            return null;
        }
    }
//----------------------------------------------------
public static boolean externalStorageMounted() {
    String str = Environment.getExternalStorageState();
    return !(!"mounted".equals(str) && !"mounted_ro".equals(str));
}

    public static File getExternalDownload() {
        try {
            Class clazz = Class.forName("android.os.Environment");
            return (File)clazz.getDeclaredMethod("getExternalStoragePublicDirectory", new Class[] { String.class }).invoke(null, new Object[] { clazz.getDeclaredField("DIRECTORY_DOWNLOADS").get(null).toString() });
        } catch (Exception exception) {
            return new File(Environment.getExternalStorageDirectory(), "Download");
        }
    }
    public static boolean makeDirectories(File paramFile, int paramInt) {
        File file;
        for (file = paramFile;; file = file.getParentFile()) {
            if (file.getParentFile() == null || file.exists()) {
                if (!paramFile.exists()) {
                    Log.v("", "Creating directory: " + paramFile.getName());
                    if (!paramFile.mkdirs()) {
                        Log.e("", "Failed to create directory.");
                        return false;
                    }
                }
                break;
            }
        }
        try {
            recursiveChmod(file, paramInt);
            return true;
        } catch (Exception e) {
            Log.e("", "", e);
            return false;
        }
    }
    public static String readFromAssetsFile(Context paramContext, String paramString) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramContext.getAssets().open(paramString)));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String str = bufferedReader.readLine();
            if (str == null) {
                bufferedReader.close();
                return stringBuilder.toString();
            }
            stringBuilder.append(str);
        }
    }

    public static String readToString(File paramFile) throws IOException {
        if (paramFile == null || !paramFile.exists())
            return null;
        FileReader fileReader = new FileReader(paramFile);
        StringBuilder stringBuilder = new StringBuilder();
        char[] arrayOfChar = new char[4096];
        while (true) {
            int i = fileReader.read(arrayOfChar);
            if (i <= -1) {
                fileReader.close();
                return stringBuilder.toString();
            }
            stringBuilder.append(String.valueOf(arrayOfChar, 0, i));
        }
    }

    public static boolean recursiveChmod(File paramFile, int paramInt) throws Exception {
        boolean bool;
        if (chmod(paramFile, paramInt) == 0) {
            bool = true;
        } else {
            bool = false;
        }
        File[] arrayOfFile = paramFile.listFiles();
        int i = arrayOfFile.length;
        for (byte b = 0;; b++) {
            boolean bool1;
            if (b >= i)
                return bool;
            File file = arrayOfFile[b];
            if (file.isDirectory())
                bool = recursiveChmod(file, paramInt);
            if (chmod(file, paramInt) == 0) {
                bool1 = true;
            } else {
                bool1 = false;
            }
            bool &= bool1;
        }
    }
    public static boolean rename(File paramFile, String paramString) {
        return paramFile.renameTo(new File(paramFile.getParent(), paramString));
    }
//    public static boolean delete(File paramFile) { // Byte code:
//
//    }
}


/* Location:              C:\反编译\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\com\ayansoft\androphp\FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */
