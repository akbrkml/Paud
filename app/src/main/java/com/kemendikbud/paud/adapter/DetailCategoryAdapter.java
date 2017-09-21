package com.kemendikbud.paud.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kemendikbud.paud.Main;
import com.kemendikbud.paud.model.Picture;
import com.kemendikbud.paud.view.fragment.DetailImageFragment;

import java.util.ArrayList;
import java.util.List;

import static com.kemendikbud.paud.util.Constant.URL_PICTURES;

/**
 * Created by akbar on 21/09/17.
 */

public class DetailCategoryAdapter extends BaseAdapter {

    private ArrayList<Picture> pictureList;
    private Context context;

    public static String urlPicture;

    public DetailCategoryAdapter(Context context) {
        this.pictureList = new ArrayList<>();
        this.context = context;
    }

    public void setDataAdapter(List<Picture> pictures){
        if (pictures == null || pictures.size() == 0)
            return;
        if (pictureList != null && pictureList.size() > 0)
            this.pictureList.clear();
        this.pictureList.addAll(pictures);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pictureList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // If the view is not recycled, this creates a new ImageView to hold an image
            imageView = new ImageView(context);
            // Define the layout parameters
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(310, 310));
        } else {
            imageView = (ImageView) view;
        }

        // Set the image resource and return the newly created ImageView
        final Picture picture = pictureList.get(i);
        Glide
                .with(context)
                .load(URL_PICTURES + picture.getImageUrl())
                .into(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                urlPicture = picture.getImageUrl();
                DetailImageFragment fragment = new DetailImageFragment();
                fragment.show(((Main) context).getSupportFragmentManager(), null);
                return false;
            }
        });
        return imageView;
    }
}
