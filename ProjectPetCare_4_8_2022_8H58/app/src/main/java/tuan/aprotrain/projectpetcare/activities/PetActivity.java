package tuan.aprotrain.projectpetcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Pet;


public class PetActivity extends AppCompatActivity {

    EditText editTextPetName,
            editTextPetBreed,
            editTextPetGender,
            editTextPetBirth,
            editTextPetHeight,
            editTextPetWeight,
            editTextPetColor,
            editTextPetIntact,
            editTextPetNote;
    Button btnPetSave;
    long petId ;
    DatabaseReference refPet;
    private Boolean isUpdating = false;
    Pet pets;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);


        editTextPetName = findViewById(R.id.petName);
        editTextPetBreed = findViewById(R.id.petBreed);
        editTextPetGender = findViewById(R.id.petGender);
        editTextPetBirth = findViewById(R.id.petBirth);
        editTextPetHeight = findViewById(R.id.petHeight);
        editTextPetWeight = findViewById(R.id.petWeight);
        editTextPetColor = findViewById(R.id.petColor);
        editTextPetIntact = findViewById(R.id.petIntact);
        editTextPetNote = findViewById(R.id.petNote);
        btnPetSave = findViewById(R.id.btnPetSave);



        btnPetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUpdating = true;
                String PetName = editTextPetName.getText().toString();
                String PetBreed = editTextPetBreed.getText().toString();
                String PetGender = editTextPetGender.getText().toString();
                String PetBirth = editTextPetBirth.getText().toString();
                Float PetHeight = Float.parseFloat(editTextPetHeight.getText().toString());
                Float PetWeight = Float.parseFloat(editTextPetWeight.getText().toString());
                String PetColor = editTextPetColor.getText().toString();
                String PetIntact = editTextPetIntact.getText().toString();
                String PetNote = editTextPetNote.getText().toString();

                PetAdds(PetName, PetBreed, PetGender, PetBirth, PetHeight, PetWeight, PetColor, PetIntact, PetNote);

            }
        });
    }
    public void PetAdds(String PetName,String PetBreed,String PetGender,String PetBirth,Float PetHeight,Float PetWeight,String PetColor,String PetIntact,String PetNote){
        refPet = FirebaseDatabase.getInstance().getReference().child("Pets");
        pets = new Pet();

        refPet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!isUpdating) {
                    return;
                }
                petId = snapshot.exists() ? snapshot.getChildrenCount() + 1 : 1;
                pets.setPetId(petId);
                pets.setPetName(PetName);
                pets.setKind(PetBreed);
                pets.setGender(PetGender);
                pets.setBirthDate(PetBirth);
                pets.setPetHeight(PetHeight);
                pets.setPetWeight(PetWeight);
                pets.setColor(PetColor);
                pets.setIntact(PetIntact);
                pets.setNotes(PetNote);

                refPet.child(String.valueOf(petId)).setValue(pets);
                isUpdating = false;
                Toast.makeText(PetActivity.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                //finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isUpdating = false;
            }
        });
    }
}