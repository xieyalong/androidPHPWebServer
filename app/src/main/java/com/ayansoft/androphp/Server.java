package com.ayansoft.androphp;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
  String dataPath = null;

  boolean isRoot = false;

  Context mContext;

  String port = "8080";

  String sdCardPath = null;

  String serverPath = null;

  String version = "1.2.0";

  String wwwroot = "/mnt/sdcard/www";

  public Server(Context paramContext, String paramString1, String paramString2, boolean paramBoolean) {
    this.mContext = paramContext;
    this.port = paramString1;
    this.wwwroot = paramString2;
    this.isRoot = paramBoolean;
    this.sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    this.serverPath = "/data/data/com.ayansoft.androphp";
  }

  public static void runCommand(String paramString) {
    try {
      OutputStream outputStream = Runtime.getRuntime().exec("sh").getOutputStream();
      Log.d("", "runCommand() cmd=" + paramString);
      writeLine(outputStream, paramString);
      outputStream.flush();
      return;
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  public static void runSuCommand(String paramString) {
    try {
      OutputStream outputStream = Runtime.getRuntime().exec("su -c sh").getOutputStream();
      Log.d("", "runSuCommand() cmd=" + paramString);
      writeLine(outputStream, paramString);
      outputStream.flush();
      return;
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  private static void writeLine(OutputStream paramOutputStream, String paramString) throws IOException { paramOutputStream.write((String.valueOf(paramString) + "\n").getBytes()); }

  public void Start() { reloadServer(); }

  public boolean checkPort(int paramInt) {
      try {
        Socket socket = new Socket("127.0.0.1", paramInt);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println("test");
        socket.close();
        return true;
      } catch (Exception e) {
        System.out.println(">]server check port="+ e.getMessage());
        return false;
      }
  }

  public void checkVersion() {
    boolean bool = false;
    File file = new File(String.valueOf(this.serverPath) + "/version");
    String str = readTextFile(String.valueOf(this.serverPath) + "/version");
    if (!file.exists()) {
      bool = true;
    } else {
      if (str == null)
        bool = true;
      if (!str.trim().equals(this.version))
        bool = true;
    }
    if (bool) {
      deleteFolder(String.valueOf(this.serverPath) + "/phpmyadmin");
      deleteFolder(String.valueOf(this.serverPath) + "/php");
      deleteFolder(String.valueOf(this.serverPath) + "/mysql");
      deleteFolder(String.valueOf(this.serverPath) + "/lighttpd");
      deleteFolder(String.valueOf(this.serverPath) + "/lib");
      deleteFolder(String.valueOf(this.serverPath) + "/binary");
      extractZipFile();
      copyFileFromAssets("conf/version", String.valueOf(this.serverPath) + "/version");
    }
  }

  public boolean copyFile(File paramFile1, File paramFile2) {
    try {
      FileInputStream fileInputStream = new FileInputStream(paramFile1);
      FileOutputStream fileOutputStream = new FileOutputStream(paramFile2);
      byte[] arrayOfByte = new byte[1024];
      while (true) {
        int i = fileInputStream.read(arrayOfByte);
        if (i <= 0) {
          fileInputStream.close();
          fileOutputStream.close();
          return true;
        }
        fileOutputStream.write(arrayOfByte, 0, i);
      }
    } catch (Exception e) {
      return false;
    }
  }

  public boolean copyFileFromAssets(String paramString1, String paramString2) {
    AssetManager assetManager = this.mContext.getAssets();
    try {
      InputStream inputStream = assetManager.open(paramString1);
      FileOutputStream fileOutputStream = new FileOutputStream(paramString2);
      try {
        byte[] arrayOfByte = new byte[1024];
        while (true) {
          int i = inputStream.read(arrayOfByte);
          if (i == -1) {
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
          }
          fileOutputStream.write(arrayOfByte, 0, i);
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      } catch (Exception e) {

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean deleteFolder(String paramString) {
    File file = new File(paramString);
    Log.d("DeleteRecursive", "DELETEPREVIOUS TOP" + file.getPath());
    if (file.isDirectory()) {
      String[] arrayOfString = file.list();
      for (byte b = 0;; b++) {
        if (b >= arrayOfString.length) {
          file.delete();
          return true;
        }
        File file1 = new File(file, arrayOfString[b]);
        if (file1.isDirectory()) {
          Log.d("DeleteRecursive", "Recursive Call" + file1.getPath());
          deleteFolder(file1.getPath());
        } else {
          Log.d("DeleteRecursive", "Delete File" + file1.getPath());
          if (!file1.delete())
            break;
        }
      }
    } else {
      return true;
    }
    Log.d("DeleteRecursive", "DELETE FAIL");
    return false;
  }

  public void execCommand(String paramString) {
    Runtime runtime = Runtime.getRuntime();
    try {
      runtime.exec(paramString);
      return;
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
  }

  public void extractZipFile() { (new Decompress(this.mContext, "files.zip", String.valueOf(this.serverPath) + "/")).unzip(); }

  public String readTextFile(String paramString) {
    File file = new File(paramString);
    StringBuilder stringBuilder = new StringBuilder();
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      while (true) {
        String str = bufferedReader.readLine();
        if (str == null)
          return stringBuilder.toString();
        stringBuilder.append(str);
        stringBuilder.append('\n');
      }
    } catch (IOException e) {
      return null;
    }
  }

  public String readTextFileFromAssets(String paramString) {
    try {
      InputStream inputStream = this.mContext.getAssets().open(paramString);
      byte[] arrayOfByte = new byte[inputStream.available()];
      inputStream.read(arrayOfByte);
      inputStream.close();
      return new String(arrayOfByte);
    } catch (IOException e) {
      return null;
    }
  }

  public void reloadServer() {
    checkVersion();
    stopServer();
    setupConfigs();
    setPermissions();
    String str2;
    String str3;
    String str1 = String.valueOf(String.valueOf((str3 = String.valueOf(String.valueOf((str2 = (str1 = "-D -f " + this.serverPath + "/lighttpd/lighttpd.conf").valueOf((str3 = "--defaults-file=" + this.serverPath + "/mysql/my.cnf").valueOf((str2 = "-c " + this.serverPath + "/php/php.ini").valueOf(this.serverPath) + "/php/php -a -b 127.0.0.1:9009")) + " " + str2).valueOf(this.serverPath) + "/mysql/mysqld")) + " " + str3).valueOf(this.serverPath) + "/lighttpd/lighttpd")) + " " + str1;
    execCommand(str2);
    execCommand(str1);
    execCommand(str3);
    if (this.isRoot) {
      runSuCommand(str2);
      runSuCommand(str1);
      runSuCommand(str3);
      return;
    }
    runCommand(str2);
    runCommand(str1);
    runCommand(str3);
  }

  public boolean saveTextFile(String paramString1, String paramString2) {
    try {
      FileWriter fileWriter = new FileWriter(paramString1);
      fileWriter.write(paramString2);
      fileWriter.flush();
      fileWriter.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void setPermissions() {
    File exception;
    try {
      File file = new File(String.valueOf(this.serverPath) + "/lighttpd/lighttpd");
      exception = file;
      try {
        FileUtils.chmod(file, 511);
        exception = file;
        exception = new File(String.valueOf(this.serverPath) + "/lighttpd/killall");
        FileUtils.chmod(exception, 511);
        file = new File(String.valueOf(this.serverPath) + "/mysql/mysqld");
        exception = file;
        FileUtils.chmod(file, 511);
        exception = file;
        exception = new File(String.valueOf(this.serverPath) + "/php/php");
        FileUtils.chmod(exception, 511);
        file = new File(String.valueOf(this.serverPath) + "/mysql/data");
        exception = file;
        FileUtils.chmod(file.getParentFile(), 511);
        exception = file;
        exception = new File(String.valueOf(this.serverPath) + "/mysql/data/mysql");
        FileUtils.chmod(exception.getParentFile(), 511);
        return;
      } catch (Exception e) {}
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void setupConfigs() {
    File  file = new File(this.wwwroot);
    if (!file.exists())
      file.mkdir();
    String str;
    saveTextFile((str = readTextFileFromAssets("conf/lighttpd.conf").replace("${wwwroot}", this.wwwroot).replace("${port}", this.port).replace("${serverpath}", this.serverPath).replace("${logpath}", String.valueOf(this.serverPath) + "/lighttpd/tmp")).valueOf(this.serverPath) + "/lighttpd/lighttpd.conf", str);
    saveTextFile((str = readTextFileFromAssets("conf/my.cnf").replace("${serverpath}", this.serverPath)).valueOf(this.serverPath) + "/mysql/my.cnf", str);
    saveTextFile((str = readTextFileFromAssets("conf/php.ini").replace("${serverpath}", this.serverPath)).valueOf(this.serverPath) + "/php/php.ini", str);
    saveTextFile(String.valueOf(this.wwwroot) + "/phpinfo.php", "<?php \n   echo phpinfo();  \n?>");
    if (!file.exists()) {
      file.mkdir();
      try {
        FileUtils.chmod(file, 509);
        return;
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
    }
  }

  public boolean stopServer() {
    try {
      File  exception = new File(String.valueOf(this.serverPath) + "/lighttpd/killall");
      try {
        FileUtils.chmod(exception, 511);
        execCommand(String.valueOf(this.serverPath) + "/lighttpd/killall lighttpd");
        runCommand(String.valueOf(this.serverPath) + "/lighttpd/killall lighttpd");
        execCommand(String.valueOf(this.serverPath) + "/lighttpd/killall mysqld");
        runCommand(String.valueOf(this.serverPath) + "/lighttpd/killall mysqld");
        execCommand(String.valueOf(this.serverPath) + "/lighttpd/killall php/php");
        runCommand(String.valueOf(this.serverPath) + "/lighttpd/killall php");
        return true;
      } catch (Exception e) {}
    } catch (Exception e) {
      e.printStackTrace();
    }

    execCommand(String.valueOf(this.serverPath) + "/lighttpd/killall lighttpd");
    runCommand(String.valueOf(this.serverPath) + "/lighttpd/killall lighttpd");
    execCommand(String.valueOf(this.serverPath) + "/lighttpd/killall mysqld");
    runCommand(String.valueOf(this.serverPath) + "/lighttpd/killall mysqld");
    execCommand(String.valueOf(this.serverPath) + "/lighttpd/killall php/php");
    runCommand(String.valueOf(this.serverPath) + "/lighttpd/killall php");
    return true;
  }
}


/* Location:              C:\反编译\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\com\ayansoft\androphp\Server.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */
