package com.ayansoft.androphp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.apache.http.conn.util.InetAddressUtils;

@SuppressLint({"HandlerLeak"})
public class ServerActivity extends Activity {
  public static final int MSG_DOWNLOADED = 0;
  
  Button exitButton;
  

  
  Button hideButton;
  
  String ip = null;
  
  boolean isRoot = false;
  
  Server mServer;
  
  ProgressDialog pd;
  
  String port = "8080";
  
  String portt = null;
  
  ToggleButton startButton;
  
  boolean waitTimer = false;
  String wwwroot = "/mnt/sdcard/www";

    @SuppressLint({"HandlerLeak"})
    private Handler handler = new Handler() {
        public void handleMessage(Message param1Message) {
            switch (param1Message.what) {
                default:
                    return;
                case 0:
                    break;
            }
            SystemClock.sleep(500L);
            pd.dismiss();
            waitTimer = false;
        }
    };
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_server);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.KILL_BACKGROUND_PROCESSES,
                        Manifest.permission.GET_TASKS,
                        Manifest.permission.ACCESS_NETWORK_STATE},1);
//    setRequestedOrientation(1);//
//    paramBundle = getIntent().getExtras();
//    Intent intent = new Intent(this, DisplayNotification.class);
//    intent.putExtra("NotifID", 1);
//    startActivity(intent);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.port = sharedPreferences.getString("updates_port", "8080");
        this.wwwroot = sharedPreferences.getString("updates_path", "/mnt/sdcard/www");
        this.isRoot = sharedPreferences.getBoolean("enable_root", false);
        this.mServer = new Server(this, port, this.wwwroot, isRoot);
        if (paramBundle != null && paramBundle.getBoolean("changes")) {
            this.mServer.stopServer();
            SystemClock.sleep(500L);
            startServer();
        }
        this.startButton = (ToggleButton)findViewById(R.id.toggleButton1);
        this.exitButton = (Button)findViewById(R.id.exitButton);
        this.exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                (new AlertDialog.Builder(ServerActivity.this))
                        .setTitle("Really Exit?").setMessage("Are you sure you want to exit?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                                ServerActivity.this.stopServer();
                                ServerActivity.CancelNotification(ServerActivity.this.getApplicationContext(), 1);
                                ServerActivity.killThisPackageIfRunning(ServerActivity.this.getApplicationContext(), ServerActivity.this.getPackageName());
                                SystemClock.sleep(500L);
                                Process.killProcess(Process.myPid());
                            }
                        }).create().show(); }
        });
        this.hideButton = (Button)findViewById(R.id.hideButton);
        this.hideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { ServerActivity.this.finish(); }
        });
        ((Button)findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(ServerActivity.this, ServerSettingActivity.class);
                intent.setFlags(ServerActivity.MSG_DOWNLOADED);
                startActivity(intent);
            }
        });
        this.startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (startButton.isChecked()) {
                    waitTimer = true;
                    startServer();
                    return;
                }
                waitTimer = true;
                stopServer();
            }
        });
//        this.ip = getLocalIpAddress();
        this.ip ="127.0.0.1";
        this.portt = this.port;
        if (this.ip == null)
            this.ip = "localhost";
        if (this.portt.equals("80"))
            this.portt = "";
        if (!this.portt.equals(""))
            this.portt = ":" + this.portt;
        TextView textView = (TextView)findViewById(R.id.textLink);
        textView.setText("http://" + this.ip + portt);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                String ipstr="http://" + ip + portt;
                System.out.println(">]2 server ip="+ipstr);
//                Intent intent = new Intent("android.intent.action.VIEW",
//                        Uri.parse(ipstr));
//                startActivity(intent);
                startWebView(ipstr);
            }
        });
        textView = (TextView)findViewById(R.id.TextLinkmyadmin);
        textView.setText("http://" + this.ip + this.portt + "/phpmyadmin");
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                String ipstr="http://" + ip + portt + "/phpmyadmin";
                System.out.println(">]1 server ip="+ipstr);
                //跳转到浏览器
//                Intent intent = new Intent("android.intent.action.VIEW",
//                        Uri.parse(ipstr));
//                startActivity(intent);
                startWebView(ipstr);
            }
        });
        controlServer(true, 10000);
    }
    public void startWebView(String url){
        Intent intent=new Intent(this,ServerWebviewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
    private void startServer() {
        this.pd = ProgressDialog.show(this, "AndroPHP", "Starting Server...", true, false);
        (new Thread() {
            public void run() {
                mServer.Start();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void stopServer() {
        this.pd = ProgressDialog.show(this, "AndroPHP", "Stopping Server...", true, false);
        (new Thread() {
            public void run() {
                mServer.stopServer();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    public static void CancelNotification(Context paramContext, int paramInt) {
    @SuppressLint("WrongConstant") NotificationManager nm=(NotificationManager)paramContext.getSystemService("notification");
    nm.cancel(paramInt);

//    ((NotificationManager)paramContext.getSystemService("notification"))
//            .cancel(paramInt);
  }
  
  private void controlServer(boolean paramBoolean, int paramInt) {
    if (paramBoolean){
        (new CountDownTimer(paramInt, 1000) {
            public void onFinish() {

            }

            public void onTick(long param1Long) {
                if (!waitTimer) {
                    if (!mServer.checkPort(Integer.parseInt(port)) || !mServer.checkPort(3306)) {
                        startButton.setChecked(false);
                        System.out.println(">]check port1=disable");
                        return;
                    }
                } else {
                    return;
                }
                startButton.setChecked(true);
                System.out.println(">]check port2=true");
            }
        }).start();
    }
  }
  
  @SuppressLint("WrongConstant")
  public static void killThisPackageIfRunning(Context paramContext, String paramString) {
    ((ActivityManager)paramContext.getSystemService("activity")).killBackgroundProcesses(paramString); }
  

  public String getLocalIpAddress() {
    try {
      ArrayList arrayList = Collections.list(NetworkInterface.getNetworkInterfaces());
      if (arrayList.size() > 0) {
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
          ArrayList arrayList1 = Collections.list(((NetworkInterface)iterator.next()).getInetAddresses());
          if (arrayList1.size() > 0)
              for (int i = 0; i <arrayList1.size() ; i++) {
                  InetAddress inetAddress=(InetAddress) arrayList1.get(i);
                  if (!inetAddress.isLoopbackAddress()) {
                      String str = inetAddress.getHostAddress();
                      boolean bool = InetAddressUtils.isIPv4Address(str);
                      if (bool){
                          return str;
                      }
                  }
              }
//            for (InetAddress inetAddress : arrayList1) {
//              if (!inetAddress.isLoopbackAddress()) {
//                String str = inetAddress.getHostAddress();
//                boolean bool = InetAddressUtils.isIPv4Address(str);
//                if (bool)
//                  return str;
//              }
//            }
        } 
      } 
    } catch (SocketException socketException) {}
    return null;
  }
  
  public void onBackPressed() {
    finish();
    super.onBackPressed();
  }
  

  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    paramMenu.add(0, 0, 0, "Settings");
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    switch (paramMenuItem.getItemId()) {
      default:
        return false;
      case 0:
        break;
    } 
    Intent intent = new Intent(this, ServerSettingActivity.class);
    intent.setFlags(MSG_DOWNLOADED);
    startActivity(intent);
    return true;
  }
}


/* Location:              C:\反编译\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\com\ayansoft\androphp\MainActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */
