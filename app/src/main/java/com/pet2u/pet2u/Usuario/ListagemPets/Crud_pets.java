package com.pet2u.pet2u.Usuario.ListagemPets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.squareup.picasso.Picasso;

public class Crud_pets extends AppCompatActivity {

    private Button botaoVoltar_crudpets;
    private TextView NomePets;
    private ImageView fotoPerfilPets;

    private StorageReference storageReference;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private int posicao;
    private Dialog dialog, dialogDeletarPerfilPet;
    private EditText dialogNovoUpdate2;
    private String novoUpdate, novoNome, novoAnoNascimento, novoEstadoCastracao;
    private boolean updatedNome = false, updatedAnoNascimento = false, updatedEstadoCastracao = false;
    private boolean nomeDialogClicked = false, AnoNascimentoDialogClicked = false, EstadoCastracaoDialogClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_pets);
        getSupportActionBar().hide();
        inicializaComponenetes();

        String nome = auth.getCurrentUser().getEmail();
        nome += getIntent().getExtras().getString("nomePet");
        String nomefinal = nome.replace(" ", "");

        storageReference.child("FotoPets/" + Criptografia.codificar(nomefinal)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(fotoPerfilPets);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("erroImagem", " A imagem não existe");
            }
        });

        NomePets.setText(getIntent().getExtras().getString("nomePet"));

    }

    @Override
    public void onBackPressed() {
        BotaoVoltarCrudPets(botaoVoltar_crudpets);
    }


    public void BotaoVoltarCrudPets(View view) {
        Intent returnIntent = new Intent();
        if (updatedNome) {
            returnIntent.putExtra("updateNome", updatedNome);
            returnIntent.putExtra("novoNome", novoNome);
        }
        if (updatedAnoNascimento) {
            returnIntent.putExtra("updateAnoNascimento", updatedAnoNascimento);
            returnIntent.putExtra("novoAnoNascimento", novoAnoNascimento);
        }
        if (updatedEstadoCastracao) {
            returnIntent.putExtra("updateEstadoCastracao", updatedEstadoCastracao);
            returnIntent.putExtra("novoEstadoCastracao", novoEstadoCastracao);
        }
        returnIntent.putExtra("position", posicao);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void SimExcluirPet(View view) {
        DatabaseReference usuarioRef = databaseReference.child("Usuario").child(Criptografia.codificar(user.getEmail())).child("petUsuario");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int indexPet = 0;
                for (DataSnapshot petUsuario : dataSnapshot.getChildren()){
                    Log.d("xesque", posicao + " POSICAO");
                    if (posicao == indexPet) {
                        petUsuario.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Crud_pets.this, "Pet Removido!", Toast.LENGTH_SHORT);
                            }
                        });
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("delete", true);
                        returnIntent.putExtra("position", posicao);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    indexPet++;
                }
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }

    public void NaoExcluirPet(View view) {
        dialogDeletarPerfilPet.dismiss();
    }

    public void ExlcuirPet(View view) {
        dialogDeletarPerfilPet.setContentView(R.layout.dialog_confirmacao_excluir_pet);
        dialogDeletarPerfilPet.show();
    }

    public void EditarNomePet(View view) {
        dialog.setContentView(R.layout.dialog_atualizar_pets_usuario);
        TextView dialogTitle = dialog.findViewById(R.id.dialogNomePetAtualizado);
        dialogNovoUpdate2 = dialog.findViewById(R.id.dialogNovoUpdate2);

        dialogTitle.setText(getText(R.string.nome_pet_atual) + " " + getIntent().getExtras().getString("nomePet"));

        dialogNovoUpdate2.setHint(getText(R.string.novo_nome_pet));

        dialog.show();

        nomeDialogClicked = true;
        AnoNascimentoDialogClicked = false;
        EstadoCastracaoDialogClicked = false;
    }

    public void EditarAnoNascimentoPet(View view) {
        dialog.setContentView(R.layout.dialog_atualizar_pets_usuario);
        TextView dialogTitle = dialog.findViewById(R.id.dialogNomePetAtualizado);
        dialogNovoUpdate2 = dialog.findViewById(R.id.dialogNovoUpdate2);

        dialogTitle.setText(getText(R.string.nascimento_pet_atual) + " " + getIntent().getExtras().getString("nascimentoPet"));

        dialogNovoUpdate2.setHint(getText(R.string.novo_nascimento_pet));

        dialog.show();

        nomeDialogClicked = false;
        AnoNascimentoDialogClicked = true;
        EstadoCastracaoDialogClicked = false;
    }
    public void EditarEstadoCastradoPet(View view) {
        dialog.setContentView(R.layout.dialog_atualizar_pets_usuario);
        TextView dialogTitle = dialog.findViewById(R.id.dialogNomePetAtualizado);
        dialogNovoUpdate2 = dialog.findViewById(R.id.dialogNovoUpdate2);

        //dialogNovoUpdate2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        dialogTitle.setText(getText(R.string.castrado_pet_atual) + " " + getIntent().getExtras().getString("castradoPet"));

        dialogNovoUpdate2.setHint(getText(R.string.novo_castrado_pet));

        dialog.show();

        nomeDialogClicked = false;
        AnoNascimentoDialogClicked = false;
        EstadoCastracaoDialogClicked = true;
    }

    public void BotaoAtualizarPet(View view) {
        if (nomeDialogClicked == true) {
            AtualizarValorDatabase("nomePet");
        }
        else if (AnoNascimentoDialogClicked == true) {
            AtualizarValorDatabase("nascimentoPet");
        }
        else if (EstadoCastracaoDialogClicked == true) {
            AtualizarValorDatabase("castradoPet");
        }
    }

    public void AtualizarValorDatabase(final String valor) {
        if (valor.equals("nascimentoPet")) {
            if (dialogNovoUpdate2.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, preencha com um novo Ano de Nascimento!", Toast.LENGTH_SHORT);
                return;
            }
        }
        else if (valor.equals("nomePet")) {
            if (dialogNovoUpdate2.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, preencha com um novo nome!", Toast.LENGTH_SHORT);
                return;
            }
        }
        else if (valor.equals("castradoPet")) {
            if (dialogNovoUpdate2.getText().toString().isEmpty()) {
                Toast.makeText(this, "Por favor, preencha se o pet é castrado ou não!", Toast.LENGTH_SHORT);
                return;
            }
        }

        DatabaseReference usuarioRef = databaseReference.child("Usuario").child(Criptografia.codificar(user.getEmail())).child("petUsuario");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int indexPet = 0;
                for (DataSnapshot petUsuario : dataSnapshot.getChildren()){
                    String value = dialogNovoUpdate2.getText().toString();
                    if (posicao == indexPet) {
                        if(valor.equals("castradoPet")){
                            if(value.equals("s") || value.equals("S") || value.equals("SIM") || value.equals("Sim") || value.equals("sim")){
                                novoUpdate = "sim";
                            }else if(value.equals("n") || value.equals("N") || value.equals("nao") || value.equals("não") || value.equals("Não") || value.equals("NÃO")){
                                novoUpdate = "não";
                            }
                        }else{
                            novoUpdate = value;
                        }
                        petUsuario.child(valor).getRef().setValue(novoUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Crud_pets.this, "Pet Atualizado!", Toast.LENGTH_SHORT);
                            }
                        });
                        if (valor.equals("nomePet")) {
                            updatedNome = true;
                            novoNome = novoUpdate;
                            NomePets.setText(novoNome);
                        }
                        else if (valor.equals("nascimentoPet")) {
                            updatedAnoNascimento = true;
                            novoAnoNascimento = novoUpdate;
                        }
                        else if (valor.equals("castradoPet")) {
                            updatedEstadoCastracao = true;
                            novoEstadoCastracao = novoUpdate;
                        }
                        dialog.dismiss();
                    }
                    indexPet++;
                }
            }

            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }

    public void BotaoFecharDialogo2(View view) {
        dialog.dismiss();
    }

    private void inicializaComponenetes(){
        botaoVoltar_crudpets = findViewById(R.id.botaoVoltar_crudpets);
        posicao = getIntent().getExtras().getInt("posicaoPet");

        NomePets = findViewById(R.id.NomeDosPets);

        fotoPerfilPets = findViewById(R.id.ImagemPets);

        //Componentes Dialog
        dialog = new Dialog(this);
        dialogDeletarPerfilPet = new Dialog(this);

        storageReference = Conexao.getFirebaseStorage();
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        databaseReference = Conexao.getFirebaseDatabase();

    }
}