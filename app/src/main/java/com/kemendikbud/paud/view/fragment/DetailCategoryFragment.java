package com.kemendikbud.paud.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.adapter.DetailCategoryAdapter;
import com.kemendikbud.paud.model.Category;
import com.kemendikbud.paud.model.User;
import com.kemendikbud.paud.network.CategoryPictureService;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kemendikbud.paud.Main.id_sekolah;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailCategoryFragment extends Fragment {

    @BindView(R.id.grid_view_picture)GridView mGridPicture;
    @BindView(R.id.toolbar)Toolbar toolbar;

    private String categoryList;
    private Category category;
    private DetailCategoryAdapter adapter;

    private CategoryPictureService categoryPictureService;

    public DetailCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_category, container, false);

        ButterKnife.bind(this, view);

        initComponents();

        loadCategoryPictures();

        return view;
    }

    private void initComponents(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        bindData();

        getActivity().setTitle("Category: " + category.getNamaKategori());

        adapter = new DetailCategoryAdapter(getActivity());
        mGridPicture.setAdapter(adapter);
    }

    private void bindData(){
        if (getArguments() != null){
            categoryList = getArguments().getString("category");
        }

        category = new Gson().fromJson(categoryList, Category.class);
    }

    private void loadCategoryPictures(){
        categoryPictureService = new CategoryPictureService();
        categoryPictureService.getCategoryPictures(id_sekolah, Integer.parseInt(category.getIdKategori()),
                new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                User user = (User) response.body();
                if (user != null){
                    if (!user.isError()){
                        adapter.setDataAdapter(user.getPictures());
                    } else {
                        Log.d("TAG", user.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

}
