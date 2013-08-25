package me.tysmith.dashclock.evernote;

import android.net.Uri;

/**
 * URIs from Evernote content provider
 */
public class EvernoteAPI {

  /**
   * The authority for the Evernote provider
   */
  private static final String AUTHORITY = "com.evernote.evernoteprovider";

  /**
   * A content:// style uri to the authority for the provider
   */
  private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);


  /**
   * URI for a reminder
   */
  public static final class ReminderNotes {
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, "remindernotes");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/remindernotes";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/remindernotes";
  }

  /**
   * Evernote Intent Actions
   */
  public static final class ACTIONS {
    /**
     * Action to show note list in Evernote
     */
    public static final String VIEW_NOTELIST = "com.evernote.action.VIEW_NOTELIST";
  }
}
