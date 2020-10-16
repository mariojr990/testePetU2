package com.pet2u.pet2u.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Pets;
import com.pet2u.pet2u.modelo.Petshop;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterListaPets extends RecyclerView.Adapter<AdapterListaPets.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater1;
    //private String idPets, idPetshop;
    private List<Pets> data1;
    private List<Pets> dataSearch1;
    private AdapterListaPets.OnItemClickListener mListener1;
    private Context contextDp;
    private StorageReference storageReference1 = Conexao.getFirebaseStorage();
//    private FirebaseUser user;
   private FirebaseAuth auth;
//    private String emailCriptografado;
//    private DatabaseReference databaseReference;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener1 = listener;
    }

    public AdapterListaPets(Context context, List<Pets> data) {
        this.layoutInflater1 = LayoutInflater.from(context);
        this.data1 = data;
        dataSearch1 = new ArrayList<>(data);
        contextDp = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater1.inflate(R.layout.cards_listagem_pets, viewGroup, false);
        return new ViewHolder(view);
    }

//    public String getIdPets() {
//        return idPets;
//    }
//
//    public void setIdPets(String idPets) {
//        this.idPets = idPets;
//    }
//
//    public String getIdPetshop() {
//        return idPetshop;
//    }
//
//    public void setIdPetshop(String idPetshop) {
//        this.idPetshop = idPetshop;
//    }

//    public void ExcluirPets(@NonNull final ViewHolder viewHolder, int i){
//        String title = data.get(i).getNome();
//        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(emailCriptografado).child("Pets").child(title);
//        usuarioRef.removeValue();
//    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String tituloCategoriaPet = data1.get(i).getCategoriaPet();
        String generoPet = data1.get(i).getGeneroPet();
        String castradoPet = data1.get(i).getCastradoPet();
        String castradoPetFinal = "";
            if(castradoPet.equals("sim")){
                 castradoPetFinal = "Castrado";
            }else{
                 castradoPetFinal = "Não castrado";
            }
        String racaPet = data1.get(i).getRacaPet();
        int nascimentoPet = Integer.parseInt(data1.get(i).getNascimentoPet());
            int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
            int calculoIdade = (anoAtual - nascimentoPet);
            String nascimentoPetFinal = "";
            if(calculoIdade > 0){
                nascimentoPetFinal = "Idade: " + calculoIdade + " anos";
            }else{
                nascimentoPetFinal = "Idade: Menos de 1 ano";
            }

        String nomePet = data1.get(i).getNomePet();


        viewHolder.generoPet.setText(generoPet);
        viewHolder.castradoPet.setText(castradoPetFinal);
        viewHolder.racaPet.setText(racaPet);
        viewHolder.nascimentoPet.setText(nascimentoPetFinal);


        String email = auth.getCurrentUser().getEmail();
        email += nomePet;
        String nomefinal = email.replace(" ", "");

//        private void exibirConfirmacao() {
//            AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(lista_Petss_pet_petshop.class);
//            caixaDialogo.setTitle("Sucesso!");
//            caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
//            caixaDialogo.setMessage("Uma novidade: Seu Pets foi cadastrado com sucesso :P");
//            caixaDialogo.setPositiveButton("Voltar para Perfil", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ExcluirPets();
//                }
//            });
//            caixaDialogo.setNegativeButton("Cadastrar outro Pets", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            caixaDialogo.show();
//        }
//        exibirConfirmacao();

        storageReference1.child("FotoPets/" + Criptografia.codificar(nomefinal)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        if (tituloCategoriaPet.isEmpty()) {
            viewHolder.tituloCategoriaPet.setVisibility(View.GONE);
            //float scale = contextDp.getResources().getDisplayMetrics().density;
            //int dpAsPixels = (int) (30*scale + 0.5f);
            //ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(500, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            //params.setMargins(0, dpAsPixels,0,0);
            //viewHolder.tituloPets.setLayoutParams(params);
        }
        else {
            viewHolder.tituloCategoriaPet.setVisibility(View.VISIBLE);
        }
        viewHolder.nomePet.setText(nomePet);
        if (!tituloCategoriaPet.isEmpty()) {
            viewHolder.tituloCategoriaPet.setText(tituloCategoriaPet);
            viewHolder.tituloCategoriaPet.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 0));
        }
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pets> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataSearch1);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Pets item : dataSearch1) {
                    if (Normalizer.normalize(item.getNomePet().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").contains(filterPattern)) {
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
            data1.clear();
            data1.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {

        int viewSize;
        TextView nomePet, generoPet, castradoPet, tituloCategoriaPet, nascimentoPet, racaPet;
        ImageView imagemPet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomePet = itemView.findViewById(R.id.nomePet);
            generoPet = itemView.findViewById(R.id.generoPet);
            castradoPet = itemView.findViewById(R.id.castradoPet);
            imagemPet = itemView.findViewById(R.id.imagemPet);
            tituloCategoriaPet = itemView.findViewById(R.id.tituloCategoriaPet);
            nascimentoPet = itemView.findViewById(R.id.nascimentoPet);
            racaPet = itemView.findViewById(R.id.racaPet);
            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            viewSize = itemView.getMeasuredHeight();
            auth = Conexao.getFirebaseAuth();



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener1 != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener1.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
