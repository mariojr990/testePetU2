package com.pet2u.pet2u.Helper;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;
import com.pet2u.pet2u.modelo.Produto;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AdapterListaProdutos extends RecyclerView.Adapter<AdapterListaProdutos.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<Produto> data;
    private List<Produto> dataSearch;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AdapterListaProdutos(Context context, List<Produto> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        dataSearch = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cards_listagem_produtos, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title = data.get(i).getNome();
        String preco = data.get(i).getValor();
        viewHolder.tituloProduto.setText(title);
        viewHolder.valorProduto.setText("R$ " + preco);
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
            List<Produto> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataSearch);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Produto item : dataSearch) {
                    if (Normalizer.normalize(item.getNome().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").contains(filterPattern)) {
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

        int viewSize;
        TextView tituloProduto, descricaoProduto, valorProduto;
        ImageView imagemProduto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloProduto = itemView.findViewById(R.id.tituloProduto);
            descricaoProduto = itemView.findViewById(R.id.descricaoProduto);
            valorProduto = itemView.findViewById(R.id.valorProduto);
            imagemProduto = itemView.findViewById(R.id.imagemProduto);
            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            viewSize= itemView.getMeasuredHeight();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
