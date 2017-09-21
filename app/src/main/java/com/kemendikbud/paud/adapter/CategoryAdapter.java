package com.kemendikbud.paud.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.model.Category;
import com.kemendikbud.paud.view.fragment.DetailCategoryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akbar on 15/09/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private Context context;
    private ArrayList<Category> category;

    private FragmentManager fm;

    public CategoryAdapter(Context context){
        this.category = new ArrayList<>();
        this.context = context;
    }

    public void setCategory(List<Category> category) {
        this.category.clear();
        this.category.addAll(category);
        notifyDataSetChanged();
    }

    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_category, parent, false);
        CategoryHolder categoryHolder = new CategoryHolder(view);
        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryHolder holder, int position) {
        final Category cat = category.get(position);

        holder.mTextViewCat.setText(cat.getNamaKategori());

        final String categoryList = new Gson().toJson(cat, Category.class);

        holder.mTextViewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category", categoryList);

                DetailCategoryFragment dcf = new DetailCategoryFragment();
                dcf.setArguments(bundle);

                fm = ((Activity) context).getFragmentManager();
                fm.beginTransaction().replace(R.id.content, dcf).addToBackStack("tag").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public class CategoryHolder extends ViewHolder {
        @BindView(R.id.text_view_cat)TextView mTextViewCat;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
