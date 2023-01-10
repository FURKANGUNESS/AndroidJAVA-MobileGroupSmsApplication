package com.example.mobilegroupsms;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilegroupsms.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

//Navigasyon ve menu islemlerinin yapildigi activitydir.
    //Logout icin bir implements ekliyorum.Bu on navigationdir.Bu bize bir metot sart kiliyor.
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private NavController navController; //Logout olduktan sonra navigasyon kontrolu icin kullanilir.Assagidaki navi sinif degiskeni yapicaz.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        //Apparbarin atandigi bir actionlist oldugu icin hata veriyordu kaldirdim.

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_groupcreate,R.id.nav_addtogroup,R.id.nav_messagecreate,R.id.nav_sendmessage) //Olusturdugumuz fragmentlarin id si
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this); //Bu sayede birseye basildiginda artik bizim metodumuz cagirilacak.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_logout){  //item id si bizim belirledigimiz logout ise
            FirebaseAuth.getInstance().signOut();  //instance aldim cikis yaptim ve finishledim.
            finish();

            return true;
        }
            //Otomatik olarak acilir pencere kapansin
            binding.drawerLayout.closeDrawer(GravityCompat.START);

            //cikis butonuna basildiysa geri don eger basilmadiysa artik otomatik olarak nereye gideceksen oraya git.
            return NavigationUI.onNavDestinationSelected(item, navController)||super.onOptionsItemSelected(item);
        }
    }