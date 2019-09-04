package com.ayansoft.androphp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class DisplayNotification extends Activity {
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    int i = getIntent().getExtras().getInt("NotifID");
    Intent intent = new Intent("com.ayansoft.androphp.MainActivity");
    intent.putExtra("NotifID", i);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    NotificationManager notificationManager = (NotificationManager)getSystemService("notification");
    Notification notification = new Notification(2130837507, "AndroPHP Runing..", System.currentTimeMillis());
//    notification.setLatestEventInfo(this, "AndroPHP", "Web Server Runing...", pendingIntent);
    notificationManager.notify(i, notification);
    finish();
  }
}


/* Location:              C:\反编译\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\com\ayansoft\androphp\DisplayNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */
