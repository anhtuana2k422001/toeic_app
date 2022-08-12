package com.example.toeic_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toeic_app.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView drawerProfileName, drawerProfileText;


    private View main_frame; // là một Frament
    public  DrawerLayout mDrawer;
    // Code khai báo ở ây
    private BottomNavigationView bottomNavigationView;
    private  BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.nav_home :
                            //bottomNavigationView.setSelectedItemId(R.id.nav_home);
                           setFragment(new CategoryFragment());
                            return true;
                        case R.id.nav_leaderboard:
                            //bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
                            setFragment(new LeaderBoardFragment() );
                            return true;
                        case R.id.nav_account:
                            //bottomNavigationView.setSelectedItemId(R.id.nav_account);
                            setFragment(new AccountFragment());
                            return true;
                    }
                    return false;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Code ở đây
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.nav_host_fragment_content_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        mDrawer = findViewById(R.id.drawer_layout);
       // DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        // Code sẵn
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_leaderboard, R.id.nav_account)
                .setOpenableLayout(mDrawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setupDrawerContent(navigationView);
        // Sét thông tin người dùng
        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);
        String name = DbQuery.myProfile.getName();
        drawerProfileName.setText(name);
        drawerProfileText.setText(name.toUpperCase().substring(0,1));

        setFragment(new CategoryFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Hàm lăng nghe ---> điều hướng
    public void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    // Lắng nghe drawer để load fragment tương ứng
    @SuppressLint("NonConstantResourceId")
    public void selectDrawerItem(MenuItem menuItem){
        Fragment fragment;
        switch (menuItem.getItemId()){
            case R.id.nav_leaderboard:
                setFragment(new LeaderBoardFragment() );
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
                break;
            case R.id.nav_account:
                setFragment(new AccountFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_account);
                break;
            default:
                fragment = new CategoryFragment();
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                setFragment(fragment);
        }
        mDrawer.closeDrawers();

    }

    // Bấm vào menu
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Tạo Fragment cho menu
    private  void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(), fragment); // Thay thế fragment người dùng khi chọn
        transaction.commit(); //Chạy
    }


    /* Tắt menu options*/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


}