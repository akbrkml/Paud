package com.kemendikbud.paud.view.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kemendikbud.paud.App;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.adapter.PhotoAdapter;
import com.kemendikbud.paud.controller.PictureController;
import com.kemendikbud.paud.event.PictureEvent;
import com.kemendikbud.paud.model.MessageResponse;
import com.kemendikbud.paud.model.Picture;
import com.kemendikbud.paud.model.User;
import com.kemendikbud.paud.network.CategoryService;
import com.kemendikbud.paud.network.PictureService;
import com.kemendikbud.paud.network.UploadService;
import com.kemendikbud.paud.util.BitmapUtils;
import com.kemendikbud.paud.util.NetworkUtils;
import com.kemendikbud.paud.util.SessionManager;
import com.medialablk.easytoast.EasyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.kemendikbud.paud.Main.id_sekolah;
import static com.kemendikbud.paud.util.Constant.getDate;
import static com.kemendikbud.paud.util.Constant.getDateTime;
import static com.kemendikbud.paud.util.Constant.uniqueID;
import static com.kemendikbud.paud.util.ViewUtils.numberOfColumns;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.kemendikbud.paud.fileprovider";

    @BindView(R.id.rv_photo)RecyclerView mRecyclerView;
    @BindView(R.id.fab)FloatingActionButton mTakePicture;
    @BindView(R.id.save_button)FloatingActionButton mSaveFab;
    @BindView(R.id.clear_button)FloatingActionButton mClearFab;
    @BindView(R.id.image_view)ImageView mImageView;
    @BindView(R.id.keterangan)EditText mTextKeterangan;
    @BindView(R.id.main_error_layout)RelativeLayout mErrorLayout;
    @BindView(R.id.tv_message_display)TextView mTextMessage;
    @BindView(R.id.category)Spinner mSpinnerCategory;
    @BindView(R.id.refresh)SwipeRefreshLayout mRefresh;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.main_progress_layout)RelativeLayout mProgressLayout;

    private LinearLayoutManager linearLayoutManager;

    private String mTempPhotoPath;
    private boolean mNeedReload = true;

    private Bitmap mResultsBitmap;
    private Uri photoURI;
    private ProgressDialog progressDialog;

    private PictureService pictureService;
    private CategoryService categoryService;
    private UploadService uploadService;

    private PhotoAdapter adapter;
    private User user;

    private EventBus eventBus;

    private int id_kategori;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        eventBus = App.getInstance().getEventBus();
//        eventBus.register(this);

        ButterKnife.bind(this, view);

        initComponents();

        if (savedInstanceState != null){
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable("List-State"));
            adapter.setDataAdapter(Arrays.asList(App.getInstance().getGson().fromJson(savedInstanceState.getString("List-Data"), Picture[].class)));
            mNeedReload = savedInstanceState.getBoolean("List-Need-Loading");
        } else {
            getDataPictures();
        }

        mRefresh.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(false);
                getDataPictures();
            }
        });

//        if (mNeedReload) getDataPictures();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        eventBus.unregister(this);
    }

    @Subscribe()
    public void getDataPictures(PictureEvent event){
        mProgressLayout.setVisibility(View.GONE);
        if (event.isSuccess()){
            adapter.setDataAdapter(event.getPictures());
            mNeedReload = false;
        } else {
            mNeedReload = true;
            mErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    public boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(getActivity());
    }

    @OnClick(R.id.fab)
    public void takePicture(){
        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists
            launchCamera();
        }
    }

    @OnClick(R.id.save_button)
    public void saveMe(View view) {
        // Delete the temporary image file
        BitmapUtils.deleteImageFile(getActivity(), mTempPhotoPath);

        // Upload the image
        encodeBitmapAndSave(mResultsBitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void encodeBitmapAndSave(Bitmap bitmap) {
        Double latitude = Double.parseDouble(SessionManager.grabString("latitude"));
        Double longitude = Double.parseDouble(SessionManager.grabString("longitude"));
        String keterangan = mTextKeterangan.getText().toString();

        showProgress();
        uploadService = new UploadService();
        uploadService.doUpload(uniqueID(), BitmapUtils.bitmapToBase64String(bitmap, 20),
                latitude, longitude, getDate(),
                keterangan, id_sekolah, id_kategori,
                getDateTime(), 0, getDateTime(), uniqueID(), new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                hideProgress();
                MessageResponse messageResponse = (MessageResponse) response.body();
                if (messageResponse != null) {
                    if (!messageResponse.isError()) {
                        clearImage();
                        EasyToast.success(getActivity(), messageResponse.getMessage());

                        // Save the image
                        BitmapUtils.saveImage(getActivity(), mResultsBitmap);

                        adapter.notifyDataSetChanged();
                    } else {
                        EasyToast.error(getActivity(), messageResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void showProgress(){
        //creating and showing progress dialog
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void hideProgress(){
        progressDialog.dismiss();
    }

    private void initComponents(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle("Home");
        progressDialog = new ProgressDialog(getActivity());

        mRecyclerView.setHasFixedSize(true);

        linearLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns(getActivity()));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PhotoAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
    }

    private void getDataPictures(){
        if (isInternetAvailable()){
            loadDataPictures();
            loadDataCategories();
        } else {
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getDataPictures();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void loadDataPictures(){
        mProgressLayout.setVisibility(View.VISIBLE);
//        PictureController controller = new PictureController();
//        controller.getDataPictures();
        pictureService = new PictureService();
        pictureService.getPictures(id_sekolah, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                user = (User) response.body();
                if (user != null){
                    mProgressLayout.setVisibility(View.GONE);
                    if (!user.isError()){
                        adapter.setDataAdapter(user.getPictures());
                    } else {
                        mErrorLayout.setVisibility(View.VISIBLE);
                        mTextMessage.setText(user.getMessage());
                        Log.d("TAG", user.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void loadDataCategories(){
        final String[] item = {"Plang Sekolah", "Tampak Depan"};
        final int[] id = {1, 2};
        mSpinnerCategory.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, item));

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (mSpinnerCategory.getSelectedItem().toString().equals(item[i])){
                    id_kategori = id[i];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.clear_button)
    public void clearImage(View view) {
        clearImage();
    }

    private void clearImage(){
        // Clear the image and toggle the view visibility
        mImageView.setImageResource(0);
        mRecyclerView.setVisibility(View.VISIBLE);
        mTakePicture.setVisibility(View.VISIBLE);
        mSaveFab.setVisibility(View.GONE);
        mClearFab.setVisibility(View.GONE);
        mTextKeterangan.setVisibility(View.GONE);
        mSpinnerCategory.setVisibility(View.GONE);

        // Delete the temporary image file
        BitmapUtils.deleteImageFile(getActivity(), mTempPhotoPath);
    }

    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(getActivity());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                photoURI = FileProvider.getUriForFile(getActivity(),
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void processAndSetImage() {

        // Toggle Visibility of the views
        mRecyclerView.setVisibility(View.GONE);
        mTakePicture.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.VISIBLE);
        mClearFab.setVisibility(View.VISIBLE);
        mTextKeterangan.setVisibility(View.VISIBLE);
        mSpinnerCategory.setVisibility(View.VISIBLE);

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(getActivity(), mTempPhotoPath);

        // Set the new bitmap to the ImageView
        mImageView.setImageBitmap(mResultsBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {

            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(getActivity(), mTempPhotoPath);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("List-State", mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putString("List-Data", App.getInstance().getGson().toJson(adapter.getDataAdapter()));
        outState.putBoolean("List-Need-Loading", mNeedReload);
    }
}