package com.pet2u.pet2u.Helper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import 	androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.pet2u.pet2u.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<String> data;
    private List<String> dataSearch;

    public Adapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        dataSearch = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cards_listagem_petshop, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title = data.get(i);
        viewHolder.nomePetshop.setText(title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataSearch);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String item : dataSearch) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nomePetshop, tempoDeEsperaPetshop, scorePetshop;
        ImageView imagemPetshop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomePetshop = itemView.findViewById(R.id.nomePetshop);
            tempoDeEsperaPetshop = itemView.findViewById(R.id.tempoDeEsperaPetshop);
            scorePetshop = itemView.findViewById(R.id.scorePetshop);
            imagemPetshop = itemView.findViewById(R.id.imagemPetshop);
        }

    }
}
