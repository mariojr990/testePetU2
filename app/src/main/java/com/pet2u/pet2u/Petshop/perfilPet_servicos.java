package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Adapter;
import com.pet2u.pet2u.Helper.AdapterListaServico;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;
import com.pet2u.pet2u.modelo.Servico;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class perfilPet_servicos extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private ImageView imagePetshop;
    private HorizontalScrollView scrollviewTipoProdutos;
    private ConstraintLayout toolbarPerfilPetshop;
    private ScrollView petshopPerfilScrollView;
    private TextView nomePet;
    private TextView enderecoPetshop;
    private boolean isToolbarOpen;


    private RecyclerView listaServicos;
    private AdapterListaServico adapter;
    private ArrayList<Servico> servicos;
    private ArrayList<String> categoriasList;
    private ArrayList<ArrayList<Servico>> categoriasMatrix;
    private String emailCriptografado;
    private TabLayout tabLayout;
    private StorageReference storageReference = Conexao.getFirebaseStorage();

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet_servicos);
        getSupportActionBar().hide();

        databaseReference = Conexao.getFirebaseDatabase();
        petshopPerfilScrollView = findViewById(R.id.petshopPerfilScrollView2);
        nomePet = findViewById(R.id.nomePetServico);
        enderecoPetshop = findViewById(R.id.enderecoPetshopServico);
        final TextView tituloPetshop = findViewById(R.id.tituloPetshopToolbar);
        emailCriptografado = Criptografia.codificar(getIntent().getExtras().getString("emailPetshop"));
        listaServicos = findViewById(R.id.listaServicosPetshop);
        Button maisInfoButton = findViewById(R.id.maisInfoButtonServico);
        toolbarPerfilPetshop = findViewById(R.id.toolbarPerfilPetshop);
        toolbarPerfilPetshop.setVisibility(View.INVISIBLE);
        Button botao_voltar = findViewById(R.id.seta_voltar);
        tabLayout = findViewById(R.id.tabLayoutToolbar);
        imagePetshop = findViewById(R.id.imageViewServico);
        servicos = new ArrayList<>();
        categoriasMatrix = new ArrayList<>();
        categoriasList = new ArrayList<>();
        isToolbarOpen = false;

        nomePet.setText(getIntent().getExtras().getString("nomePetshop"));
        tituloPetshop.setText(getIntent().getExtras().getString("nomePetshop"));
        enderecoPetshop.setText(getIntent().getExtras().getString("enderecoPetshop") + ". " + getIntent().getExtras().getString("complementoEnderecoPetshop"));

        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(emailCriptografado).child("servico");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String categoriaNome = "";
                if (dataSnapshot.exists()) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) dataSnapshot.getValue()).entrySet()) {

                        Map singleUser = (Map) entry.getValue();

                        Boolean titulo = false;

                        Servico servicoClicked = new Servico();
                        servicoClicked.setNomeServico((String) singleUser.get("nomeServico"));
                        servicoClicked.setValorServico((String) singleUser.get("valorServico"));
                        servicoClicked.setDescricaoServico((String) singleUser.get("descricaoServico"));
                        categoriaNome = (String) singleUser.get("categoriaServico");
                        servicoClicked.setCategoriaServico(categoriaNome);

                        String petshopCriptografado = Criptografia.codificar(getIntent().getExtras().getString("emailPetshop"));
                        storageReference.child("FotoPerfilPet/" + petshopCriptografado).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).fit().centerInside().into(imagePetshop);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("xesque", " A imagem não existe");
                            }
                        }) ;


                        if (!categoriasList.contains(categoriaNome)) {
                            categoriasList.add(categoriaNome);
                            titulo = true;
                        }
                        if (!categoriasList.isEmpty()) {
                            categoriasMatrix.add(new ArrayList<Servico>());
                            if (!titulo) {
                                servicoClicked.setCategoriaServico("");
                            }
                            categoriasMatrix.get(categoriasList.indexOf(categoriaNome)).add(servicoClicked);
                        }
                    }

                    if (!categoriasList.isEmpty()) {
                        for (int i = 0; i < categoriasMatrix.size(); i++) {
                            for (int j = 0; j < categoriasMatrix.get(i).size(); j++) {
                                servicos.add(categoriasMatrix.get(i).get(j));
                            }
                        }
                    }
                    for (String nomeCategoria : categoriasList) {
                        tabLayout.addTab(tabLayout.newTab().setText(nomeCategoria));
                    }


                    listaServicos.setLayoutManager(new LinearLayoutManager(perfilPet_servicos.this));
                    listaServicos.setNestedScrollingEnabled(false);
                    adapter = new AdapterListaServico(perfilPet_servicos.this, servicos);
                    listaServicos.setAdapter(adapter);
                    int viewSize = adapter.getItemCount() * 520;
                    ViewGroup.LayoutParams layoutParams = listaServicos.getLayoutParams();
                    layoutParams.height = viewSize;
                    listaServicos.setLayoutParams(layoutParams);


                    adapter.setOnItemClickListener(new AdapterListaServico.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent perfilservico = new Intent(perfilPet_servicos.this, Pagina_do_servico.class);
                            perfilservico.putExtra("nomeServico", servicos.get(position).getNomeServico());
                            perfilservico.putExtra("nomeValor", servicos.get(position).getValorServico());
                            perfilservico.putExtra("nomeDescricao", servicos.get(position).getDescricaoServico());
                            startActivity(perfilservico);

                        }
                    });
                }else{
                    exibirAviso();
                }

            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tamanhoToolbar = toolbarPerfilPetshop.getHeight();
                scrollToView(petshopPerfilScrollView, listaServicos);
                //petshopPerfilScrollView.scrollTo(0, getRelativeTop(listaServicos) - tamanhoToolbar - 25);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        //listaServicos.setLayoutParams(lp);

        petshopPerfilScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = petshopPerfilScrollView.getScrollY();
                if (scrollY >= 630 && !isToolbarOpen) {
                    isToolbarOpen = true;
                    toolbarPerfilPetshop.post(new Runnable() {
                        @Override
                        public void run() {
                            int from = -toolbarPerfilPetshop.getWidth();
                            int to = 0;
                            ObjectAnimator slider = ObjectAnimator.ofFloat(toolbarPerfilPetshop, "y", from, to);
                            slider.setDuration(400);
                            slider.start();
                            toolbarPerfilPetshop.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else if (scrollY < 630 && isToolbarOpen) {
                    isToolbarOpen = false;
                    toolbarPerfilPetshop.post(new Runnable() {
                        @Override
                        public void run() {
                            int from = 0;
                            int to = -toolbarPerfilPetshop.getWidth();
                            ObjectAnimator slider = ObjectAnimator.ofFloat(toolbarPerfilPetshop, "y", from, to);
                            slider.setDuration(600);
                            slider.start();
                            toolbarPerfilPetshop.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        });


        botao_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(perfilPet_servicos.this, PerfilPet_Usuario_2.class);
                    intent.putExtra("nomePetshop", nomePet.getText().toString());
                    intent.putExtra("enderecoPetshop", enderecoPetshop.getText().toString());
                    intent.putExtra("telefonePetshop", getIntent().getExtras().getString("telefonePetshop"));
                    intent.putExtra("dataFuncionamento", getIntent().getExtras().getString("dataFuncionamento"));
                    intent.putExtra("horarioFuncionamento", getIntent().getExtras().getString("horarioFuncionamento"));
                    intent.putExtra("complementoEnderecoPetshop", getIntent().getExtras().getString("complementoEnderecoPetshop"));
                    intent.putExtra("emailPetshop", emailCriptografado);
                    startActivity(intent);

                } else {

                    Toast.makeText(this, "Permissão de Localização GPS negada!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y - toolbarPerfilPetshop.getHeight());
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */
    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    private void exibirAviso() {
        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(this);
        caixaDialogo.setTitle("Aviso!");
        caixaDialogo.setIcon(android.R.drawable.ic_menu_info_details);
        caixaDialogo.setMessage("O petshop não possui serviços cadastrados.");
        caixaDialogo.setPositiveButton("Voltar para o perfil do petshop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        caixaDialogo.show();
    }

    public void irProduto(View view){
//        Intent intent = new Intent(perfilPet_servicos.this, perfilPet_servicos.class);
//        intent.putExtra("nomePetshop", nomePet.getText().toString());
//        intent.putExtra("enderecoPetshop", enderecoPetshop.getText().toString());
//        intent.putExtra("telefonePetshop", getIntent().getExtras().getString("telefonePetshop"));
//        intent.putExtra("dataFuncionamento", getIntent().getExtras().getString("dataFuncionamento"));
//        intent.putExtra("horarioFuncionamento", getIntent().getExtras().getString("horarioFuncionamento"));
//        intent.putExtra("complementoEnderecoPetshop", getIntent().getExtras().getString("complementoEnderecoPetshop"));
//        intent.putExtra("emailPetshop", emailCriptografado);
//        startActivity(intent);
        finish();
    }

    public void perfilpet3(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            Intent intent = new Intent(perfilPet_servicos.this, PerfilPet_Usuario_2.class);
            intent.putExtra("nomePetshop", nomePet.getText().toString());
            intent.putExtra("enderecoPetshop", enderecoPetshop.getText().toString());
            intent.putExtra("telefonePetshop", getIntent().getExtras().getString("telefonePetshop"));
            intent.putExtra("dataFuncionamento", getIntent().getExtras().getString("dataFuncionamento"));
            intent.putExtra("horarioFuncionamento", getIntent().getExtras().getString("horarioFuncionamento"));
            intent.putExtra("complementoEnderecoPetshop", getIntent().getExtras().getString("complementoEnderecoPetshop"));
            intent.putExtra("emailPetshop", emailCriptografado);
            startActivity(intent);
        }
    }
}
