package tuan.aprotrain.projectpetcare.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.List;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.FCMSend;
import tuan.aprotrain.projectpetcare.entity.Pet;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private List<Pet> mlistPets;
    private Context parentContext;
    TextView tv_pet_name, tv_pet_gender, tv_pet_breed, tv_pet_species,
            tv_pet_height, tv_pet_weight,
            tv_pet_birthdate, tv_pet_color, tv_pet_intact, tv_pet_notes;

    public PetAdapter(List<Pet> mlistPets) {
        this.mlistPets = mlistPets;
    }

    public void setFilteredList(List<Pet> filteredList) {
        this.mlistPets = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_information, parent, false);
        parentContext = parent.getContext();
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) { //go data
        Pet pet = mlistPets.get(position);
        if (pet == null) {
            return;
        }

        holder.tv_name.setText(pet.getPetName());
        holder.tv_species.setText(pet.getSpecies());
        if (pet.getGender().equals("male")) {
            holder.tv_gender.setText("male");
        } else {
            holder.tv_gender.setText("female");
        }
    }

    @Override
    public int getItemCount() {
        if (mlistPets != null) {
            return mlistPets.size();
        }
        return 0;
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView tv_name, tv_species, tv_gender;
        RelativeLayout layout_bar;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.imgAvatar);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_species = itemView.findViewById(R.id.tv_species);
            tv_gender = itemView.findViewById(R.id.tv_gender);

            layout_bar = itemView.findViewById(R.id.layout_bar);

            layout_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos_click = getAdapterPosition();
                    openRequestDialog(pos_click, view, Gravity.CENTER);
                }
            });
        }
    }

    private void openRequestDialog(int pos_click, View view, int gravity) {

        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_request);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        tv_pet_name = dialog.findViewById(R.id.tv_pet_name);
        tv_pet_gender = dialog.findViewById(R.id.tv_pet_gender);
        tv_pet_breed = dialog.findViewById(R.id.tv_pet_breed);
        tv_pet_species = dialog.findViewById(R.id.tv_pet_species);

        tv_pet_height = dialog.findViewById(R.id.tv_pet_height);
        tv_pet_weight = dialog.findViewById(R.id.tv_pet_weight);


        tv_pet_birthdate = dialog.findViewById(R.id.tv_pet_birthdate);
        tv_pet_color = dialog.findViewById(R.id.tv_pet_color);
        tv_pet_intact = dialog.findViewById(R.id.tv_pet_intact);
        tv_pet_notes = dialog.findViewById(R.id.tv_pet_notes);

        Pet pet = mlistPets.get(pos_click);
        if (pet == null) {
            return;
        }
//        holder.imgAvatar.setImageResource(pet);
        tv_pet_name.setText("Name: " + pet.getPetName());

        if (pet.getGender().equals("male")) {
            tv_pet_gender.setText("Gender: Male");
        } else {
            tv_pet_gender.setText("Gender: Female");
        }

        tv_pet_breed.setText("Breed: " + pet.getKind());
        tv_pet_species.setText("Species: " + pet.getSpecies());

        tv_pet_height.setText("Height: " + pet.getPetHeight() + " cm");
        tv_pet_weight.setText("Weight: " + pet.getPetWeight() + "kg");

        tv_pet_birthdate.setText("Birth Date: " + pet.getBirthDate());
        tv_pet_color.setText("Color: " + pet.getColor());
        if (pet.getGender().equals("male")) {
            tv_pet_intact.setText("Intact: Yes");
        } else {
            tv_pet_intact.setText("Intact: No");
        }
        tv_pet_notes.setText("Notes: " + pet.getNotes());


        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_send = dialog.findViewById(R.id.btn_send);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tuan
                sendRequest(pet);
            }
        });
        dialog.show();
    }

    // tuan
    public void sendRequest(Pet pet){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String title = pet.getPetName()+" owner send you a breeding request";
        String message = "Species: "+pet.getSpecies()+"Breed: "+pet.getKind()+"Gender: " + pet.getGender() +
                "Color: " + pet.getColor();
        reference.child("Users").orderByChild("userId").equalTo(pet.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    reference.child("Users").child(pet.getUserId()).child("token").
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot userToken : snapshot.getChildren()) {
                                        System.out.println("user token: " + userToken.getValue());
                                        String token = userToken.getValue(String.class);

                                        try {
                                            FCMSend.pushNotification(parentContext,
                                                    token,
                                                    title, message);
                                            Toast.makeText(parentContext, "Request sent", Toast.LENGTH_LONG).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
                else
                    System.out.println("Error: no user");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}
