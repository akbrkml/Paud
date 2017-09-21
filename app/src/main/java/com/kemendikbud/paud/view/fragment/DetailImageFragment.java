package com.kemendikbud.paud.view.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kemendikbud.paud.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kemendikbud.paud.adapter.DetailCategoryAdapter.urlPicture;
import static com.kemendikbud.paud.util.Constant.URL_PICTURES;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailImageFragment extends DialogFragment {

    @BindView(R.id.image_view)ImageView mImageView;

    public DetailImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_image, container, false);

        ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents(){
        Glide
                .with(getActivity())
                .load(URL_PICTURES + urlPicture)
                .into(mImageView);
    }

    @OnClick(R.id.clear_button)
    public void dismissDialog(){
        getDialog().dismiss();
    }

}
