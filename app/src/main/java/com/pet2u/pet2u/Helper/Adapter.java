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
import 	androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<Petshop> data;
    private List<Petshop> dataSearch;
    private OnItemClickListener mListener;
    private StorageReference storageReference = Conexao.getFirebaseStorage();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adapter(Context context, List<Petshop> data) {
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String title = data.get(i).getNome();
        String score = data.get(i).getScore();
        viewHolder.nomePetshop.setText(title);
        viewHolder.scorePetshop.setText(score);

        String petshopCriptografado = Criptografia.codificar(data.get(i).getEmail());
        storageReference.child("FotoPerfilPet/" + petshopCriptografado).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(viewHolder.imagemPetshop);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("xesque", " A imagem não existe");
            }
        }) ;
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
            List<Petshop> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataSearch);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Petshop item : dataSearch) {
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

        TextView nomePetshop, tempoDeEsperaPetshop, scorePetshop;
        ImageView imagemPetshop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomePetshop = itemView.findViewById(R.id.nomePetshop);
            tempoDeEsperaPetshop = itemView.findViewById(R.id.tempoDeEsperaPetshop);
            scorePetshop = itemView.findViewById(R.id.scorePetshop);
            imagemPetshop = itemView.findViewById(R.id.imagemPetshop);

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
