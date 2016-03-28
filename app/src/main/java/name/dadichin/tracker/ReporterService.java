package name.dadichin.tracker;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.loopj.android.http.*;

import org.apache.http.Header;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by yuriy on 3/29/16.
 */
public class ReporterService extends IntentService {

    public final String TAG = "TROLOLO";

    public ReporterService() {
        super("ReporterService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.i(TAG, "Found doodle!!");

        SmartLocation.with(this).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        sendLoacationData(intent, location);
                    }
                });
    }

    private void sendLoacationData(final Intent intent, Location loc) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://requestb.in/1g62rde1?lat=" + String.valueOf(loc.getLatitude()) + "&lon="
                + String.valueOf(loc.getLongitude()), new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  byte[] responseBody) {
                Log.i(TAG, "Request sent!!!");
                ReporterReceiver.completeWakefulIntent(intent);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Log.i(TAG, "Request not sent sent!!!");
                ReporterReceiver.completeWakefulIntent(intent);
            }
        });
    }
}

