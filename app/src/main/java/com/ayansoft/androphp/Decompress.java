package com.ayansoft.androphp;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress {
  private String _location;
  
  private String _zipFile;
  
  Context mContext;
  
  public Decompress(Context paramContext, String paramString1, String paramString2) {
    this._zipFile = paramString1;
    this._location = paramString2;
    this.mContext = paramContext;
    _dirChecker("");
  }
  
  private void _dirChecker(String paramString) {
   File file = new File(String.valueOf(this._location) + paramString);
    if (!file.isDirectory()) {
      file.mkdirs();
      try {
        FileUtils.chmod(file.getParentFile(), 493);
        return;
      } catch (Exception e) {
        e.printStackTrace();
        return;
      } 
    } 
  }
  
  public void unzip() {
      ZipInputStream zipInputStream = null;
    try {
      this.mContext.getAssets();
      try {
        ZipInputStream zipInputStream1 = new ZipInputStream(this.mContext.getAssets().open(this._zipFile));
        zipInputStream = zipInputStream1;
      } catch (IOException iOException) {}
      label28: while (true) {
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        if (zipEntry == null) {
          zipInputStream.close();
          FileUtils.chmod(new File(String.valueOf(this._location) + zipEntry.getName()), 493);
          return;
        } 
        Log.v("Decompress", "Unzipping " + zipEntry.getName());
        if (zipEntry.isDirectory()) {
          _dirChecker(zipEntry.getName());
          continue;
        } 
        FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(this._location) + zipEntry.getName());
        byte[] arrayOfByte = new byte[4096];
        while (true) {
          int i = zipInputStream.read(arrayOfByte);
          if (i <= 0) {
            zipInputStream.closeEntry();
            fileOutputStream.close();
            continue label28;
          } 
          fileOutputStream.write(arrayOfByte, 0, i);
        }
      }
    } catch (Exception e) {
      Log.e("Decompress", "unzip", e);
      return;
    } 
  }
}


/* Location:              C:\反编译\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\com\ayansoft\androphp\Decompress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */
