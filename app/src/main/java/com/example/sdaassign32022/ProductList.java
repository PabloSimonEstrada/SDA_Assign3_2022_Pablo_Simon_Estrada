package com.example.sdaassign32022;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/*
 * A simple {@link Fragment} subclass.
 * @author Chris Coughlan 2019
 */
public class ProductList extends Fragment implements FlavorViewAdapter.OnItemClickListener {

    private static final String TAG = "RecyclerViewActivity";
    private ArrayList<FlavorAdapter> mFlavor = new ArrayList<>();

    public ProductList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_product_list, container, false);

        // Create an ArrayList of AndroidFlavor objects
        mFlavor.add(new FlavorAdapter("Blue T-shirt ", "24,99€", R.drawable.tshirt_design_1));
        mFlavor.add(new FlavorAdapter("Black T-shirt ", "21,99€", R.drawable.tshirt_design_2));
        mFlavor.add(new FlavorAdapter("USA T-shirt ", "23,99€", R.drawable.tshirt_design_3));
        mFlavor.add(new FlavorAdapter("Red with flowers T-shirt ", "20,99€", R.drawable.tshirt_design_4));
        mFlavor.add(new FlavorAdapter("Sky blue T-shirt ", "26,99€", R.drawable.tshirt_design_5));
        mFlavor.add(new FlavorAdapter("Nº one cares T-shirt ", "22,99€", R.drawable.tshirt_design_6));
        mFlavor.add(new FlavorAdapter("Yellow T-shirt ", "27,99€", R.drawable.tshirt_design_7));
        mFlavor.add(new FlavorAdapter("White T-shirt ", "21,99€", R.drawable.tshirt_design_8));
        mFlavor.add(new FlavorAdapter("Black 333 T-shirt ", "23,99€", R.drawable.tshirt_design_9));
        mFlavor.add(new FlavorAdapter("Positive T-shirt ", "20,99€", R.drawable.tshirt_design_10));

        //start it with the view
        Log.d(TAG, "Starting recycler view");
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_view);
        FlavorViewAdapter recyclerViewAdapter = new FlavorViewAdapter(getContext(), mFlavor, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        return root;
    }

    @Override
    public void onItemClick(FlavorAdapter flavor) {
        Toast.makeText(getContext(), "Clicked: " + flavor.getVersionName(), Toast.LENGTH_SHORT).show();
    }
}