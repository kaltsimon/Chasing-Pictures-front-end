package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.location.Location;
import android.widget.TextView;

import java.io.File;

import de.fu_berlin.cdv.chasingpictures.api.Place;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PictureCard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PictureCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureCard extends Fragment {
    public static final String PLACE = "place_param";
    public static final String USER_LOCATION = "user_location";

    private Place[] places;
    private int currentPlace = 0;
    private SwipeDetector mSwipeDetector;
    private View view;
    private Location userLocation;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param places a list of places
     * @return A new instance of fragment PictureCard.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureCard newInstance(Location userLocation, Place... places) {
        PictureCard fragment = new PictureCard();
        Bundle args = new Bundle();
        args.putSerializable(PLACE, places);
        args.putParcelable(USER_LOCATION, userLocation);
        fragment.setArguments(args);
        return fragment;
    }

    public PictureCard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.places = (Place[]) getArguments().getSerializable(PLACE);
            this.userLocation = getArguments().getParcelable(USER_LOCATION);
        }
        mSwipeDetector = new SwipeDetector();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_picture_card, container, false);
        view.setOnTouchListener(mSwipeDetector);

        view.setOnClickListener(new ClickListener());

        updatePicture();
        showDelayedPlaceInfo(currentPlace);

        return view;
    }

    private void updatePicture() {
        File cachedFile = places[currentPlace].getPicture().getCachedFile();
        if (cachedFile != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(cachedFile.getPath());
            ((ImageView) view.findViewById(R.id.picture_card_image)).setImageBitmap(bitmap);
        }
    }

    private void showDelayedPlaceInfo(final int placeNr) {
        final TextView placeInfo = (TextView) view.findViewById(R.id.place_info);
        placeInfo.setText("");
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        placeInfo.setText(String.valueOf(Math.round(places[placeNr].distanceTo(userLocation))) + "m");
                    }
                },
                1000
        );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mSwipeDetector.swipeDetected()) {
                showNextPlace(mSwipeDetector.getAction());
            }
        }
    }

    private void showNextPlace(SwipeDetector.Action direction) {
        switch (direction) {
            case LR:
                // Because the modulo operator in Java is shit.
                if (currentPlace > 0)
                    currentPlace--;
                else
                    currentPlace = places.length - 1;
                break;
            case RL:
                currentPlace = (currentPlace + 1) % places.length;
                break;
            case TB:
            case BT:
            case None:
            default:
                break;
        }

        updatePicture();
        showDelayedPlaceInfo(currentPlace);
    }
}
