package me.tysmith.dashclock.evernote;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import java.util.List;

public class ReminderService extends DashClockExtension {
  private final static String LOGTAG = "ReminderService";

  protected void onUpdateData(int reason) {

    Log.d(LOGTAG, "onUpdateData() starting");

    //Query when waking up
    setUpdateWhenScreenOn(true);

    //Query reminders from Evernote ContentProvider
    List<Reminder> reminders = Reminder.getReminders(getContentResolver());

    //If there is no data to publish, clear the UI
    if(reminders.isEmpty()) {

      publishUpdate(null);
      Log.d(LOGTAG, "onUpdateData() No data, publishing null");
      return;
    }

    publishUpdate(new ExtensionData()
        .visible(true)
        .icon(R.drawable.ic_launcher)
        .status(getStatus(reminders))
        .expandedTitle(getTitle(reminders))
        .expandedBody(getBody(reminders))
        .clickIntent(getClickIntent()));

    Log.d(LOGTAG, "onUpdateData() publishing " + reminders.size() + " Reminders");
  }

  /**
   * Get the status to display
   *
   * @param reminders
   * @return Status String
   */
  private String getStatus(List<Reminder> reminders) {
    return String.valueOf(reminders.size());
  }


  /**
   * Get title for one reminder or n reminders
   *
   * @param reminders list of Reminders
   * @return Title String
   */
  protected String getTitle(List<Reminder> reminders) {
    int size = reminders.size();

    return getResources().getQuantityString(R.plurals.n_reminders, size, size);
  }

  /**
   * Get the body to display when expanded, the titles of the reminders
   *
   * @param reminders
   * @return Body String
   */
  private String getBody(List<Reminder> reminders) {
    return  TextUtils.join("\n", reminders);

  }

  /**
   * This will provide an Intent to view a list of notes
   *
   * @return Evernote intent for single reminder or list of reminders
   */
  protected Intent getClickIntent() {

    String action = EvernoteAPI.ACTIONS.VIEW_NOTELIST;
    Intent intent = new Intent(action);
    return intent;
  }


}
