package com.kemendikbud.paud.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.kemendikbud.paud.Main;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.model.MessageResponse;
import com.kemendikbud.paud.model.Picture;
import com.kemendikbud.paud.network.DeleteService;
import com.kemendikbud.paud.util.SessionManager;
import com.kemendikbud.paud.view.activity.SignInActivity;
import com.kemendikbud.paud.view.fragment.EditDataDialogFragment;
import com.medialablk.easytoast.EasyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.kemendikbud.paud.Main.userName;
import static com.kemendikbud.paud.util.Constant.URL_PICTURES;

/**
 * Created by akbar on 12/08/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private ArrayList<Picture> pictureList;
    private Context context;
    private PhotoViewAttacher mAttacher;

    public static String urlPicture;
    public static String keterangan;
    public static String fotoId;
    public static int kategoriId;

    public String[] mColors = {
            "#39add1", // light blue
    };

    public PhotoAdapter(Context context) {
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

    public List<Picture> getDataAdapter() {
        return pictureList;
    }

    public class PhotoHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_date)TextView mTextViewDate;
        @BindView(R.id.text_view_name)TextView mTextViiewName;
        @BindView(R.id.image_view_photo)ImageView mImageViewPhoto;
        @BindView(R.id.txtStatusMsg)TextView mTextViewMsg;
        @BindView(R.id.profile_image)ImageView mProfileImage;
        @BindView(R.id.options)TextView mButtonViewOption;

        public PhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public PhotoAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        PhotoHolder photoHolder = new PhotoHolder(view);
        return photoHolder;
    }

    @Override
    public void onBindViewHolder(final PhotoAdapter.PhotoHolder holder, int position) {
        final Picture picture = pictureList.get(position);

        mAttacher = new PhotoViewAttacher(holder.mImageViewPhoto);
        holder.mTextViewDate.setText(picture.getTimestamp());
        holder.mTextViiewName.setText(userName);
        holder.mTextViewMsg.setText(picture.getKeterangan());
        Glide
                .with(context)
                .load(URL_PICTURES + picture.getImageUrl())
                .into(holder.mImageViewPhoto);
        String firstCharUserName = userName.substring(0,1);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstCharUserName, getColor());
        holder.mProfileImage.setImageDrawable(drawable);

        holder.mButtonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.mButtonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_update:
                                fotoId = picture.getImageId();
                                urlPicture = picture.getImageUrl();
                                keterangan = picture.getKeterangan();
                                kategoriId = picture.getKategoriId();
                                EditDataDialogFragment fragment = new EditDataDialogFragment();
                                fragment.show(((Main) context).getSupportFragmentManager(), null);
                                break;
                            case R.id.menu_delete:
                                new AlertDialog.Builder(context)
                                        .setMessage("Hapus foto?")
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                DeleteService deleteService = new DeleteService();
                                                deleteService.doDelete(picture.getImageId(), 1, new Callback() {
                                                    @Override
                                                    public void onResponse(Call call, Response response) {
                                                        MessageResponse message = (MessageResponse) response.body();
                                                        if (message != null) {
                                                            if (!message.isError()) {
                                                                EasyToast.success(context, message.getMessage());
                                                            } else {
                                                                EasyToast.error(context, message.getMessage());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call call, Throwable t) {

                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("Tidak", null)
                                        .show();
                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public int getColor() {
        String color;

        // Randomly select a fact
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);

        color = mColors[randomNumber];
        int colorAsInt = Color.parseColor(color);

        return colorAsInt;
    }
}
