package com.example.interactingwithotherapps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCallIntent(View view) {
        Uri number = Uri.parse("tel:797204");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    public void startMapIntent(View view) {
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=Campus+Schoonmeersen+Hogeschool+Gent");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void startWebIntent(View view) {
        Uri webpage = Uri.parse("http://www.android.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    public void startEmailIntent(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris
        startActivity(emailIntent);
    }

    public void startCalendarIntent(View view) {
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 0, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 0, 19, 10, 30);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, "Ninja class");
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Secret dojo");
        startActivity(calendarIntent);
    }

    public void startChooserApp(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        shareIntent.setType("text/plain");

        // Verify it resolves
        //ComponentName appComponentName = shareIntent.resolveActivity(getPackageManager());
        //boolean isIntentSafe = appComponentName != null;
         PackageManager packageManager = getPackageManager();
         List<ResolveInfo> resolvingActivities = packageManager.queryIntentActivities(shareIntent, 0);
         boolean isIntentSafe = resolvingActivities.size() > 0;

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share");
        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(chooserIntent);
        }
    }

    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    public void startPickContactIntent(View v){
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        // Show user only contacts w/ phone numbers
        pickContactIntent.setType(Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure the request was successful
        if (resultCode == RESULT_OK) {
            // Check which request it is that we're responding to
            if (requestCode == PICK_CONTACT_REQUEST) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(column);

                // Do something with the phone number...
                Button startPickContactButton = (Button) findViewById(R.id.startPickContactButton);
                startPickContactButton.setText("startPickContactIntent picked phonenumber: " + number);
            }
            else if (requestCode == PICK_SOME_RESULT_REQUEST) {
                String stringResult = data.getExtras().getString(SecondActivity.RESULT_NAME);

                Button startActivityForResultButton = (Button) findViewById(R.id.startActivityForResultButton);
                startActivityForResultButton.setText("startPickContactIntent retrieved: " + stringResult);
            }
        }
    }

    static final int PICK_SOME_RESULT_REQUEST = 2;  // The request code
    public void startActivityForResult(View view) {
        Intent resultIntent = new Intent(this, SecondActivity.class);
        startActivityForResult(resultIntent, PICK_SOME_RESULT_REQUEST);
    }
}
