package com.pet2u.pet2u.Helper;

import android.content.Context;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Pet_Adocao;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AdapterListaPetAdocao extends RecyclerView.Adapter<AdapterListaPetAdocao.ViewHolder> implements Filterable {
    private LayoutInflater layoutInflater;
    private List<Pet_Adocao> data;
    private List<Pet_Adocao> dataSearch;
    private OnItemClickListener mListener;
    private Context contextDp;
    private StorageReference storageReference = Conexao.getFirebaseStorage();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public AdapterListaPetAdocao(Context context, List<Pet_Adocao> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        dataSearch = new ArrayList<>(data);
        contextDp = context;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cards_listagem_pet_adocao, viewGroup, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String title = data.get(i).getNome();
        String descricao = data.get(i).getDescricaoPetAdocao();
        String idade = data.get(i).getIdade();

        viewHolder.descricaoPet.setText(descricao);

        String nome = Criptografia.codificar(title.replace(" ", ""));

        storageReference.child("FotoPetAdocao/" + nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(viewHolder.imagemPet);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("xesque", " A imagem não existe");
            }
        });
    }
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
            List<Pet_Adocao> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataSearch);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Pet_Adocao item : dataSearch) {
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
        TextView nomePet, descricaoPet, idadePet;
        ImageView imagemPet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomePet = itemView.findViewById(R.id.tituloProduto);
            descricaoPet = itemView.findViewById(R.id.descricaoProduto);
            idadePet = itemView.findViewById(R.id.valorProduto);
            imagemPet = itemView.findViewById(R.id.imagemProduto);
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
