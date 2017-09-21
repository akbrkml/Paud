package com.kemendikbud.paud.view.fragment;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.gson.Gson;
import com.kemendikbud.paud.Main;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.adapter.CategoryAdapter;
import com.kemendikbud.paud.model.Category;
import com.kemendikbud.paud.model.User;
import com.kemendikbud.paud.network.CategoryService;
import com.kemendikbud.paud.util.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.category)RecyclerView mRecyclerViewCat;

    private CategoryAdapter adapter;

    private CategoryService categoryService;
    private FragmentManager fm;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle("Category");

        mRecyclerViewCat.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewCat.setItemAnimator(new DefaultItemAnimator());

        adapter = new CategoryAdapter(getActivity());

        mRecyclerViewCat.setAdapter(adapter);

        loadDataCategories();
    }

    private void loadDataCategories(){
        categoryService = new CategoryService();
        categoryService.getCategories(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                final User user = (User) response.body();

                if (user != null){
                    if (!user.isError()) {
                        adapter.setCategory(user.getCategories());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }
}
