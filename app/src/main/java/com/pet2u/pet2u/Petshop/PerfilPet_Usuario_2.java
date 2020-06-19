package com.pet2u.pet2u.Petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pet2u.pet2u.ConexaoDB.Conexao;
import com.pet2u.pet2u.Helper.Criptografia;
import com.pet2u.pet2u.R;
import com.pet2u.pet2u.modelo.Petshop;

public class PerfilPet_Usuario_2 extends AppCompatActivity {

    private TextView nome_petshop_perfil, enderecoPetshop2, numeroPetshop2, horarioPetshop2;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet__usuario_2);
        getSupportActionBar().hide();
        inicializaComponenetes();

        nome_petshop_perfil.setText(getIntent().getExtras().getString("nomePetshop"));
        enderecoPetshop2.setText(getIntent().getExtras().getString("enderecoPetshop"));
        numeroPetshop2.setText(getIntent().getExtras().getString("telefonePetshop"));
        String horariopet = getIntent().getExtras().getString("horarioFuncionamento");
        String datapet = getIntent().getExtras().getString("dataFuncionamento");
        horarioPetshop2.setText(horariopet + "\n" + datapet);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Retorna o status se o usuário está com o google play services ativo ou não
//        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
//        switch (errorCode){
//            case ConnectionResult.SERVICE_MISSING:
//            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
//            case ConnectionResult.SERVICE_DISABLED: //Case para tratar caso o services esteja desativado.
//                Log.d("teste", "show dialog");
//                GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, 0, new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        finish();
//                    }
//                }).show();
//                break;
//            case ConnectionResult.SUCCESS:
//                Log.d("teste", "Google Play Services up-to-date");
//                break;
//        }
//    }

    public void voltar(View view){
        finish();
    }

    private void inicializaComponenetes(){
        nome_petshop_perfil = findViewById(R.id.nome_petshop_perfil);
        enderecoPetshop2 = findViewById(R.id.enderecoPetshop2);
        numeroPetshop2 = findViewById(R.id.numeroPetshop2);
        horarioPetshop2 = findViewById(R.id.horarioPetshop2);
        auth = Conexao.getFirebaseAuth();
        databaseReference = Conexao.getFirebaseDatabase();
        user = auth.getCurrentUser();
    }
}
