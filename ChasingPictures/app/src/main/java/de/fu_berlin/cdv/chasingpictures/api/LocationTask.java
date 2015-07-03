package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Task for location requests.
 * You will usually want to override {@link #onPostExecute(List) onPostExecute}.
 *
 * @author Simon Kalt
 */
public class LocationTask extends AsyncTask<Location, Object, List<Place>> {
    protected final Context context;

    public LocationTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Place> doInBackground(@NonNull Location... params) {
        if (params.length == 0 || params[0] == null)
            return null;

        LocationRequest request = new LocationRequest(context, params[0]);
        ResponseEntity<PlacesApiResult> result = request.sendRequest();
        PlacesApiResult body = result == null ? null : result.getBody();
        return body == null ? null : body.getPlaces();
    }

    @Override
    protected void onPostExecute(@Nullable List<Place> places) {
    }
}
