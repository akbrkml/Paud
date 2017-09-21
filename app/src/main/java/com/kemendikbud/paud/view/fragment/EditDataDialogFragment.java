package com.kemendikbud.paud.view.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.model.MessageResponse;
import com.kemendikbud.paud.network.UpdateService;
import com.medialablk.easytoast.EasyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kemendikbud.paud.adapter.PhotoAdapter.fotoId;
import static com.kemendikbud.paud.adapter.PhotoAdapter.kategoriId;
import static com.kemendikbud.paud.adapter.PhotoAdapter.keterangan;
import static com.kemendikbud.paud.adapter.PhotoAdapter.urlPicture;
import static com.kemendikbud.paud.util.Constant.URL_PICTURES;
import static com.kemendikbud.paud.util.Constant.getDateTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDataDialogFragment extends DialogFragment {

    @BindView(R.id.image_view)ImageView mImageView;
    @BindView(R.id.keterangan)EditText mTextKeterangan;
    @BindView(R.id.category)Spinner mSpinnerCategory;

    private String namaKategori;
    private int idKategori;

    public EditDataDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_data_dialog, container, false);

        ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents(){
        Glide
                .with(getActivity())
                .load(URL_PICTURES + urlPicture)
                .into(mImageView);

        mTextKeterangan.setText(keterangan);

        final String[] item = {"Plang Sekolah", "Tampak Depan"};
        final int[] id = {1, 2};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(adapter);

        if (kategoriId == 1){
            namaKategori = "Plang Sekolah";
        } else if (kategoriId == 2){
            namaKategori = "Tampak Depan";
        }

        if (!namaKategori.equals(null)){
            int position = adapter.getPosition(namaKategori);
            mSpinnerCategory.setSelection(position);
        }

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSpinnerCategory.getSelectedItem().toString().equals(item[i])){
                    idKategori = id[i];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.save_button)
    public void doUpdate(){
        updatePictures();
    }

    private void updatePictures(){
        UpdateService updateService = new UpdateService();
        updateService.doUpdate(fotoId,
                mTextKeterangan.getText().toString(),
                idKategori, getDateTime(), new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        MessageResponse message = (MessageResponse) response.body();
                        if (message != null) {
                            if (!message.isError()) {
                                getDialog().dismiss();
                                EasyToast.success(getActivity(), message.getMessage());
                            } else {
                                EasyToast.error(getActivity(), message.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
    }
}
