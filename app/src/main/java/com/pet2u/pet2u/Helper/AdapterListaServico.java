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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Servico;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AdapterListaServico extends RecyclerView.Adapter<AdapterListaServico.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<Servico> data;
    private List<Servico> dataSearch;
    private AdapterListaServico.OnItemClickListener mListener;
    private Context contextDp;
    private StorageReference storageReference1 = Conexao.getFirebaseStorage();


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AdapterListaServico(Context context, List<Servico> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        dataSearch = new ArrayList<>(data);
        contextDp = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cards_listagem_servico, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String title1 = data.get(i).getNomeServico();
        String categoriaTitulo1 = data.get(i).getCategoriaServico();
        String descricao1 = data.get(i).getDescricaoServico();
        String preco1 = data.get(i).getValorServico();
        String valorfinal1 ="R$ " + preco1.replace(".",",");

        viewHolder.descricaoServico.setText(descricao1);
        viewHolder.valorServico.setText(valorfinal1);


        if (categoriaTitulo1.isEmpty()) {
            viewHolder.tituloCategoria.setVisibility(View.GONE);
        }
        else {
            viewHolder.tituloCategoria.setVisibility(View.VISIBLE);
        }
        viewHolder.tituloServico.setText(title1);
        if (!categoriaTitulo1.isEmpty()) {
            viewHolder.tituloCategoria.setText(categoriaTitulo1);
            viewHolder.tituloCategoria.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 0));
        }
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
            List<Servico> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataSearch);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Servico item : dataSearch) {
                    if (Normalizer.normalize(item.getNomeServico().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").contains(filterPattern)) {
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
        TextView tituloServico, descricaoServico, valorServico, tituloCategoria;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloServico = itemView.findViewById(R.id.tituloServico2);
            descricaoServico = itemView.findViewById(R.id.descricaoServico2);
            valorServico = itemView.findViewById(R.id.valorServico2);
            tituloCategoria = itemView.findViewById(R.id.tituloCategoriaservico);
            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            viewSize = itemView.getMeasuredHeight();

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
