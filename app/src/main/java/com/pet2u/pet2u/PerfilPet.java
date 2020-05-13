package com.pet2u.pet2u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;
import com.pet2u.pet2u.Fragment.Brinquedos;
import com.pet2u.pet2u.Fragment.OutrosFragment;
import com.pet2u.pet2u.Fragment.PetsFragment;
import com.pet2u.pet2u.Fragment.RacaoFragment;
import com.pet2u.pet2u.Fragment.ServicosFragment;

public class PerfilPet extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet);

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);

        //Configurar adapter para abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Rações", RacaoFragment.class)
                        .add("Brinquedos", Brinquedos.class)
                        .add("Serviços", ServicosFragment.class)
                        .add("Pets", PetsFragment.class)
                        .add("Outros", OutrosFragment.class)
                .create()
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
