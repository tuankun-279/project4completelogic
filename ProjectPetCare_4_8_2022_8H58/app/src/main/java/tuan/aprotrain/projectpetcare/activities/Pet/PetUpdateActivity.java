package tuan.aprotrain.projectpetcare.activities.Pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.databinding.ActivityPetUpdateBinding;

public class PetUpdateActivity extends AppCompatActivity {
    ActivityPetUpdateBinding petUpdateBinding;
    DatabaseReference petUpdateReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petUpdateBinding = ActivityPetUpdateBinding.inflate(getLayoutInflater());
        setContentView(petUpdateBinding.getRoot());

        petUpdateBinding.btnPetUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PetName = petUpdateBinding.petNameUpdate.getText().toString();
                String PetHeight = petUpdateBinding.petHeightUpdate.getText().toString();
                String PetWeight = petUpdateBinding.petWeightUpdate.getText().toString();
                String PetIntact = petUpdateBinding.petIntactUpdate.getText().toString();
                String PetNote = petUpdateBinding.petNoteUpdate.getText().toString();
                String petId = petUpdateBinding.petIdUpdate.getText().toString();
                updatePetDate(PetName, PetHeight, PetWeight, PetIntact, PetNote, petId);
            }
        });
    }

    private void updatePetDate(String PetName, String PetHeight, String PetWeight, String PetIntact, String PetNote, String petId) {
        HashMap Pet = new HashMap();
        Pet.put("petName", PetName);
        Pet.put("petHeight", PetHeight);
        Pet.put("petWeight", PetWeight);
        Pet.put("intact", PetIntact);
        Pet.put("notes", PetNote);

        petUpdateReference = FirebaseDatabase.getInstance().getReference("Pets");
        petUpdateReference.child(petId).updateChildren(Pet).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    petUpdateBinding.petNameUpdate.setText("");
                    petUpdateBinding.petHeightUpdate.setText("");
                    petUpdateBinding.petWeightUpdate.setText("");
                    petUpdateBinding.petIntactUpdate.setText("");
                    petUpdateBinding.petNoteUpdate.setText("");

                    Toast.makeText(PetUpdateActivity.this, "Update data complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}