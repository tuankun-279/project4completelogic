package tuan.aprotrain.projectpetcare.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import tuan.aprotrain.projectpetcare.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prf;
    Button btnLogout, buttonLogout;
    FirebaseAuth mAuth;
    private ImageSlider imageSlider;
    ImageButton petHotel_Btn, petSpa_Btn, petHealth_Btn, petBurry_Btn;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    TextView textView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        btnLogout = findViewById(R.id.btnLogout);

        petHotel_Btn = findViewById(R.id.petHotel_Btn);
        petSpa_Btn = findViewById(R.id.petSpa_Btn);
        petHealth_Btn = findViewById(R.id.petHealth_Btn);
        petBurry_Btn = findViewById(R.id.petBurry_Btn);

        buttonLogout = findViewById(R.id.buttonLogout);

        petHotel_Btn.setOnClickListener(this);
        petSpa_Btn.setOnClickListener(this);
        petHealth_Btn.setOnClickListener(this);
        petBurry_Btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        textView4 = findViewById(R.id.textView4);

        //code cua kien
        imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://img.freepik.com/free-vector/cute-pets-illustration_53876-112522.jpg?w=2000", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://kienthucbonphuong.com/images/202006/pet-la-gi/pet-la-gi.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://img.freepik.com/premium-photo/group-pets-posing-around-border-collie-dog-cat-ferret-rabbit-bird-fish-rodent_191971-22249.jpg?w=2000", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

//        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//            }
//        });
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount !=null){
            textView4.setText(signInAccount.getEmail());
        }


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {

        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.petHotel_Btn:
                intent= new Intent(MainActivity.this, BookingActivity.class);
                intent.putExtra("ID_BUTTON", "Homestay");
                startActivity(intent);
                break;
            case R.id.petSpa_Btn:
                intent= new Intent(MainActivity.this, BookingActivity.class);
                intent.putExtra("ID_BUTTON", "Spa");
                startActivity(intent);
                break;
            case R.id.petHealth_Btn:
                intent= new Intent(MainActivity.this, BookingActivity.class);
                intent.putExtra("ID_BUTTON", "Hospital");
                startActivity(intent);
                break;
            case R.id.petBurry_Btn:
                intent= new Intent(MainActivity.this, BookingActivity.class);
                intent.putExtra("ID_BUTTON", "Burial");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}