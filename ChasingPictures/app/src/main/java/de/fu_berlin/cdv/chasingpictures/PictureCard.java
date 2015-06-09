package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private Place[] places;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param places a list of places
     * @return A new instance of fragment PictureCard.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureCard newInstance(Place... places) {
        PictureCard fragment = new PictureCard();
        Bundle args = new Bundle();
        args.putSerializable(PLACE, places);
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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture_card, container, false);

        File cachedFile = places[0].getPicture().getCachedFile();
        if (cachedFile != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.picture_card_image);
            Bitmap bitmap = BitmapFactory.decodeFile(cachedFile.getPath());
            imageView.setImageBitmap(bitmap);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onSwipe(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

}
