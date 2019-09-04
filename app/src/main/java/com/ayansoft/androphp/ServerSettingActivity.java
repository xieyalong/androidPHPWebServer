package com.ayansoft.androphp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ServerSettingActivity extends PreferenceActivity {
  public void onBackPressed() {
    Intent intent = new Intent(this, ServerActivity.class);
    intent.setFlags(ServerActivity.MSG_DOWNLOADED);
    intent.putExtra("changes", true);
    startActivity(intent);
    finish();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.preferences_android_php_);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    paramMenu.add(0, 0, 0, "Save");
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    switch (paramMenuItem.getItemId()) {
      default:
        return false;
      case 0:
        break;
    } 
    Intent intent = new Intent(this, ServerActivity.class);
    intent.setFlags(ServerActivity.MSG_DOWNLOADED);
    intent.putExtra("changes", true);
    startActivity(intent);
    finish();
    return true;
  }
}


/* Location:              C:\反编译\dex2jar-2.0\dex2jar-2.0\classes-dex2jar.jar!\com\ayansoft\androphp\QuickPrefsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */
