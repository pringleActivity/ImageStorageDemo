package com.example.vadym.galleryapp.UI.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vadym.galleryapp.R;
import com.example.vadym.galleryapp.UI.MainActivity;
import com.example.vadym.galleryapp.UI.SlideshowActivity;
import com.example.vadym.galleryapp.UI.adapter.AdapterRecyclerImages;
import com.example.vadym.galleryapp.database.DatabaseHelper;
import com.example.vadym.galleryapp.database.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class GridFragment extends Fragment implements MainActivity.OnRemoteFragmentListener {
    private int numberTable;

    private RecyclerView rv;
    private List<ImageItem> images = new ArrayList<>();
    private AdapterRecyclerImages adapterRecycler;

    private DatabaseHelper db;

    public static GridFragment newInstance(int position) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putInt("numberFragment", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapterRecycler = new AdapterRecyclerImages(images, context, numberTable);
        db = new DatabaseHelper(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberTable = getArguments().getInt("numberFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grid, container, false);

        images.clear();

        images.addAll(db.getAllItems(numberTable));

        rv = (RecyclerView) v.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.setAdapter(adapterRecycler);

        return v;
    }

    @Override
    public void addImage(String uri) {
        long id = db.insertItem(numberTable, uri);

        ImageItem img = db.getItem(numberTable, id);
        images.add(0, img);

        adapterRecycler.notifyDataSetChanged();
    }

    @Override
    public void startSlideshow(int position) {
        Intent intent = new Intent(getContext(), SlideshowActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(SlideshowActivity.TAG_POSITION_ITEM, position);
        extras.putInt(SlideshowActivity.TAG_NUMBER_TABLE, numberTable);
        intent.putExtras(extras);
        getContext().startActivity(intent);
    }
}