package com.pet2u.pet2u.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pet2u.pet2u.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Brinquedos extends Fragment {

    public Brinquedos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  View rootView = inflater.inflate(R.layout.fragment_brinquedos, container, false);
//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.listaProdutos);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        return inflater.inflate(R.layout.fragment_brinquedos, container, false);
    }
}
