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
import com.pet2u.pet2u.Petshop.CadastroProdutoActivity;
import com.pet2u.pet2u.Petshop.lista_produtos_pet_petshop;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Produto;
import com.pet2u.pet2u.modelo.Petshop;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AdapterListaProdutosPet extends RecyclerView.Adapter<AdapterListaProdutosPet.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    //private String idProduto, idPetshop;
    private List<Produto> data;
    private List<Produto> dataSearch;
    private AdapterListaProdutosPet.OnItemClickListener mListener;
    private Context contextDp;
    private StorageReference storageReference = Conexao.getFirebaseStorage();
//    private FirebaseUser user;
//    private FirebaseAuth auth;
//    private String emailCriptografado;
//    private DatabaseReference databaseReference;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AdapterListaProdutosPet(Context context, List<Produto> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        dataSearch = new ArrayList<>(data);
        contextDp = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cards_listagem_produtos_pet, viewGroup, false);
        return new ViewHolder(view);
    }

//    public String getIdProduto() {
//        return idProduto;
//    }
//
//    public void setIdProduto(String idProduto) {
//        this.idProduto = idProduto;
//    }
//
//    public String getIdPetshop() {
//        return idPetshop;
//    }
//
//    public void setIdPetshop(String idPetshop) {
//        this.idPetshop = idPetshop;
//    }

//    public void ExcluirProduto(@NonNull final ViewHolder viewHolder, int i){
//        String title = data.get(i).getNome();
//        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(emailCriptografado).child("produto").child(title);
//        usuarioRef.removeValue();
//    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String title = data.get(i).getNome();
        String categoriaTitulo = data.get(i).getCategoria();
        String descricao = data.get(i).getDescricaoProduto();
        String preco = data.get(i).getValor();
        String valorfinal ="R$ " + preco.replace(".",",");

        viewHolder.descricaoProduto.setText(descricao);
        viewHolder.valorProduto.setText(valorfinal);

        String nome = Criptografia.codificar(title.replace(" ", ""));



//        private void exibirConfirmacao() {
//            AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(lista_produtos_pet_petshop.class);
//            caixaDialogo.setTitle("Sucesso!");
//            caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
//            caixaDialogo.setMessage("Uma novidade: Seu produto foi cadastrado com sucesso :P");
//            caixaDialogo.setPositiveButton("Voltar para Perfil", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ExcluirProduto();
//                }
//            });
//            caixaDialogo.setNegativeButton("Cadastrar outro produto", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            caixaDialogo.show();
//        }
//        exibirConfirmacao();

        storageReference.child("FotoProduto/" + nome).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(viewHolder.imagemProduto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("xesque", " A imagem não existe");
            }
        }) ;

        if (categoriaTitulo.isEmpty()) {
            viewHolder.tituloCategoria.setVisibility(View.GONE);
            //float scale = contextDp.getResources().getDisplayMetrics().density;
            //int dpAsPixels = (int) (30*scale + 0.5f);
            //ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(500, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            //params.setMargins(0, dpAsPixels,0,0);
            //viewHolder.tituloProduto.setLayoutParams(params);
        }
        else {
            viewHolder.tituloCategoria.setVisibility(View.VISIBLE);
        }
        viewHolder.tituloProduto.setText(title);
        if (!categoriaTitulo.isEmpty()) {
            viewHolder.tituloCategoria.setText(categoriaTitulo);
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
        TextView tituloProduto, descricaoProduto, valorProduto, tituloCategoria, excluirProduto, editarProduto;
        ImageView imagemProduto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloProduto = itemView.findViewById(R.id.tituloProduto2);
            descricaoProduto = itemView.findViewById(R.id.descricaoProduto2);
            valorProduto = itemView.findViewById(R.id.valorProduto2);
            imagemProduto = itemView.findViewById(R.id.imagemProduto2);
            tituloCategoria = itemView.findViewById(R.id.tituloCategoria2);
            excluirProduto = itemView.findViewById(R.id.excluir_produto);
            editarProduto = itemView.findViewById(R.id.editar_produto);
            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            viewSize = itemView.getMeasuredHeight();
//            databaseReference = Conexao.getFirebaseDatabase();
//            auth = Conexao.getFirebaseAuth();
//            user = auth.getCurrentUser();
//            emailCriptografado = Criptografia.codificar(user.getEmail());


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
