package com.kemendikbud.paud.view.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.kemendikbud.paud.Main;
import com.kemendikbud.paud.MainActivity;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.model.User;
import com.kemendikbud.paud.util.SessionManager;
import com.kemendikbud.paud.view.activity.SignInActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.kemendikbud.paud.Main.userName;
import static com.kemendikbud.paud.view.activity.SignInActivity.USER_SESSION;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.user_profile_name)TextView mTextProfileName;
    @BindView(R.id.user_profile_email)TextView mTextProfileEmail;
    @BindView(R.id.text_view_address)TextView mTextProfileAddress;
    @BindView(R.id.text_view_phone)TextView mTextProfilePhone;
    @BindView(R.id.text_view_title)TextView mTextProfileTitle;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.user_profile_photo)ImageView mProfilePhoto;

    public String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle("Profile");
        setHasOptionsMenu(true);

        User user = SessionManager.getUser(getActivity(), USER_SESSION);
        if (user != null){
            mTextProfileName.setText(user.getData().getName());
            mTextProfileEmail.setText(user.getData().getEmail());
            mTextProfileAddress.setText(user.getData().getAlamat());
            mTextProfilePhone.setText(user.getData().getNoHp());
            mTextProfileTitle.setText(user.getData().getJabatan());

            String firstCharUserName = user.getData().getName().substring(0,1);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstCharUserName, getColor());
            mProfilePhoto.setImageDrawable(drawable);
        }
    }

    private void logout(){
        new AlertDialog.Builder(getActivity())
                .setMessage("Apakah anda yakin ingin logout?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SessionManager.clear(getActivity());
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_about) {

            return true;
        } else if (id == R.id.menu_logout){
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
