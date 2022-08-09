package com.example.toeic_app;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {


    public CategoryFragment() {
        // Required empty public constructor
    }

    private GridView catView;
    private List<CategoryModel> catList  = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        catView = view.findViewById(R.id.card_Gird);
        loadCategories();

        CategoryAdapter adapter = new CategoryAdapter(catList);
        catView.setAdapter(adapter);

        return view;
    }

    private void loadCategories(){
        catList.clear();
        catList.add(new CategoryModel("1", "Cloze Text", 20));
        catList.add(new CategoryModel("2", "Reading", 40));
        catList.add(new CategoryModel("3", "Listening", 15));
        catList.add(new CategoryModel("4", "Sign", 20));
        catList.add(new CategoryModel("5", "Test", 10));
    }


}