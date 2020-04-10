package com.saptarshi.coronaupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

public class MainActivity extends AppCompatActivity {

    private BubbleNavigationLinearView navBar;
    TipsFragment tipsFragment;
    WorldwideFragment worldwideFragment;
    CountrybasedFragment countrybasedFragment;
    IndiaFragment indiaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTasks();
        //initializeFragment();

        //
        navBar.setCurrentActiveItem(0);
        if (navBar.getCurrentActiveItemPosition()==0){
            updateFragment(tipsFragment);
        }
        navBar.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                navBar.setCurrentActiveItem(position);
                if (navBar.getCurrentActiveItemPosition()==0)
                    updateFragment(tipsFragment);
                else if(navBar.getCurrentActiveItemPosition()==1)
                    updateFragment(worldwideFragment);
                else if (navBar.getCurrentActiveItemPosition()==2)
                    updateFragment(countrybasedFragment);
                else if (navBar.getCurrentActiveItemPosition()==3)
                    updateFragment(indiaFragment);
            }
        });


    }

    private void initTasks() {
         navBar=findViewById(R.id.navBar);

         tipsFragment=new TipsFragment();
         worldwideFragment=new WorldwideFragment();
         countrybasedFragment=new CountrybasedFragment();
         indiaFragment=new IndiaFragment();





    }

    /*private void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.mainContainer, tipsFragment);
        fragmentTransaction.add(R.id.mainContainer, worldwideFragment);
        fragmentTransaction.add(R.id.mainContainer, countrybasedFragment);
        fragmentTransaction.add(R.id.mainContainer, indiaFragment);

        fragmentTransaction.hide(worldwideFragment);
        fragmentTransaction.hide(countrybasedFragment);
        fragmentTransaction.hide(indiaFragment);

        fragmentTransaction.commit();

    }*/

    public void updateFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer,fragment);
        fragmentTransaction.commit();
    }
}
