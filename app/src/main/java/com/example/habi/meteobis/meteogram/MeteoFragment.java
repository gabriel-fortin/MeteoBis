package com.example.habi.meteobis.meteogram;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habi.meteobis.main.Config;
import com.example.habi.meteobis.R;
import com.example.habi.meteobis.mvp.Meteogram;

import javax.inject.Inject;

/**
 * Created by Gabriel Fortin
 */
public class MeteoFragment extends Fragment implements Meteogram.View {
    public static final String TAG = MeteoFragment.class.getSimpleName();

    /** The fragment argument representing the section number for this fragment. */
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Inject
    public Meteogram.Presenter presenter;

    private TextView sectionLabel;
    private ImageView img;
    private int position;

    public MeteoFragment() {
        Config.getMeteogramComponent().inject(this);
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static MeteoFragment newInstance(int sectionNumber) {
        MeteoFragment fragment = new MeteoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /** Initialize views */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        int pageNum = getArguments().getInt(ARG_SECTION_NUMBER);

        // show dev info (only visible in dev_noDex flavour
        sectionLabel = (TextView) rootView.findViewById(R.id.section_label);
        sectionLabel.setText(getString(R.string.section_format, pageNum));

        img = (ImageView) rootView.findViewById(R.id.meteoImg);
        position = -(Config.TOTAL_PAGES - pageNum);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attach(this, position);
    }

    @Override
    public void onPause() {
        presenter.detach(this);
        super.onPause();
    }

    @Override
    public void meteogramLoading() {
        Log.v(TAG, "'meteogramLoading()'");
        sectionLabel.setText("loading...");
        // TODO: show something when meteogram is loading?
        //       → it needs to add something to the layout file, probably
    }

    @Override
    public void meteogramImage(byte[] imageBytes) {
        Log.v(TAG, "'meteogramImage()'");
        sectionLabel.setText("done: image");
        animateCardIn(img);
        displayImageFromBytes(img, imageBytes);
    }

    @Override
    public void meteogramNotAvailable() {
        Log.v(TAG, "'meteogramNotAvailable()'");
        sectionLabel.setText("done: unavailable");
        animateCardIn(img);
        displayUnavailableImage(img);
    }

    @Override
    public void meteogramError(String text) {
        Log.v(TAG, "'meteogramError()'");
        sectionLabel.setText("error: " + text);
    }

    private void animateCardIn(ImageView img) {
        ViewParent card = img.getParent();
        int interpolator = android.R.interpolator.linear;

        ObjectAnimator anim = ObjectAnimator.ofFloat(card, "alpha", 1f);

        // 2016-10-23: using 'activityInstance' because 'getContext()' can be null when swiping pages quickly
//            anim.setInterpolator(AnimationUtils.loadInterpolator(activityInstance, interpolator));
        // 2016-12-28: apparently it works now
        anim.setInterpolator(AnimationUtils.loadInterpolator(getContext(), interpolator));

        anim.start();
    }

    private static void displayUnavailableImage(ImageView img) {
        img.setImageResource(android.R.drawable.ic_delete);

        CardView card = (CardView) img.getParent();
        ViewGroup.LayoutParams cardLP = card.getLayoutParams();

        card.setBackgroundColor(Color.TRANSPARENT);
        cardLP.height = 300;  // for vertical positioning
    }

    private static void displayImageFromBytes(ImageView img, byte[] imgBytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        img.setImageBitmap(bitmap);

        int width = img.getWidth();
        int intrinsicWidth = img.getDrawable().getIntrinsicWidth();
        int intrinsicHeight = img.getDrawable().getIntrinsicHeight();

        CardView card = (CardView) img.getParent();
        ViewGroup.LayoutParams cardLP = card.getLayoutParams();

        // adjust card height to match the image
        double scaleX = (double) width / intrinsicWidth;
        cardLP.height = (int) (intrinsicHeight * scaleX);
    }

}
