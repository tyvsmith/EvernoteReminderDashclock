package me.tysmith.dashclock.evernote;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Reminder from Evernote
 */
public class Reminder {
  private static final String LOGTAG = "Reminder";
  private static final int MAX_TITLE_LENGTH = 25;

  /**
   * Get a list of reminders from the Evernote Content Provider
   * @param resolver
   * @return List of Reminders
   */
  public static List<Reminder> getReminders(ContentResolver resolver) {
    ArrayList<Reminder> reminders = new ArrayList<Reminder>();
    Cursor cursor = null;

    try {
      cursor = resolver.query(EvernoteAPI.ReminderNotes.CONTENT_URI,
          Projection.PROJECTION, null, null, Table.REMINDER_ORDER + " ASC");

      while (cursor != null && cursor.moveToNext()) {
        String guid = cursor.getString(cursor.getColumnIndex(Table.GUID));
        String title = cursor.getString(cursor.getColumnIndex(Table.TITLE));
        long time = cursor.getLong(cursor.getColumnIndex(Table.REMINDER_TIME));
        long doneTime = cursor.getLong(cursor.getColumnIndex(Table.REMINDER_DONE_TIME));
        int order = cursor.getInt(cursor.getColumnIndex(Table.REMINDER_ORDER));

        Reminder reminder = new Reminder(guid, title, time, doneTime, order);
        reminders.add(reminder);
      }

    } catch (Exception ex) {
      Log.d(LOGTAG, "Error querying reminders.", ex);
    } finally {

      //Close the cursor if possible
      if(cursor != null) {
        cursor.close();
      }
    }

    return reminders;
  }

  public static final class Table {
    public static final String GUID = "guid";
    public static final String TITLE = "title";
    public static final String REMINDER_TIME = "task_due_date";
    public static final String REMINDER_DONE_TIME = "task_complete_date";
    public static final String REMINDER_ORDER = "task_date";
  }

  public static class Projection {
    private static final String[] PROJECTION = new String[]{
        Table.GUID,
        Table.TITLE,
        Table.REMINDER_TIME,
        Table.REMINDER_DONE_TIME,
        Table.REMINDER_ORDER,
    };
  }

  public String guid;
  public String title;
  public long reminderTime;
  public long reminderDoneTime;
  public int reminderOrder;

  public Reminder(String guid, String title, long reminderTime, long reminderDoneTime, int reminderOrder) {
    this.guid = guid;
    this.title = title;
    this.reminderTime = reminderTime;
    this.reminderDoneTime = reminderDoneTime;
    this.reminderOrder = reminderOrder;
  }

  @Override
  public String toString() {

    return getPrefix() + getTrimmedTitle() + getSuffix();
  }

  /**
   * Trims the string to {@link Reminder#MAX_TITLE_LENGTH} chars to fit most devices
   * @return
   */
  private String getTrimmedTitle() {
    int max = title.length() > MAX_TITLE_LENGTH ? MAX_TITLE_LENGTH : title.length();
    return title.substring(0, max);
  }

  /**
   * Return ellipsis if needed
   * @return
   */
  private String getSuffix() {
    if(title.length() > MAX_TITLE_LENGTH ) {
      return "…";
    }
    return "";
  }

  /**
   * Return bullet and a possible checkbox
   * @return
   */
  private String getPrefix() {
    if(reminderDoneTime != 0 ) {
      return "•✓";
    }
    return "•";
  }
}
