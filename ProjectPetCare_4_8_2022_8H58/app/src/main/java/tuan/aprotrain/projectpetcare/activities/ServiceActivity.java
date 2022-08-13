package tuan.aprotrain.projectpetcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import tuan.aprotrain.projectpetcare.entity.Service;

public class ServiceActivity extends AppCompatActivity {
    EditText editTextCategoryId;
    EditText editTextServiceName;
    EditText editTextServicePrice;
    EditText editTextServiceTime;
    long serviceId;
    Button btnSave;
    DatabaseReference refServices;
    private Boolean isUpdating = false;
    Service services;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        editTextCategoryId = findViewById(R.id.categoryId);
        editTextServiceName = findViewById(R.id.serviceName);
        editTextServicePrice = findViewById(R.id.servicePrice);
        editTextServiceTime = findViewById(R.id.serviceTime);
        btnSave = findViewById(R.id.btnServiceAdd);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUpdating = true;
                String cateId = editTextCategoryId.getText().toString();
                String ServiceName = editTextServiceName.getText().toString().trim();
                Float ServicePrice = Float.parseFloat(String.valueOf(editTextServicePrice.getText().toString().trim()));
                Long ServiceTime = Long.parseLong(String.valueOf(editTextServiceTime.getText().toString().trim()));
                ServiceAdd(cateId,ServiceName,ServicePrice,ServiceTime);

            }
        });
    }
    public void ServiceAdd(String CategoryId, String ServiceName, Float ServicePrice, Long ServiceTime){
        refServices = FirebaseDatabase.getInstance().getReference().child("Services");
        services = new Service();

        refServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!isUpdating) {
                    return;
                }
                serviceId = snapshot.exists() ? snapshot.getChildrenCount() + 1 : 1;
                services.setServiceId(serviceId);
                services.setServiceName(ServiceName);
                services.setServicePrice(ServicePrice);
                services.setServiceTime(ServiceTime);
                services.setCategoryId(CategoryId);

                refServices.child(String.valueOf(serviceId)).setValue(services);
                isUpdating = false;
                Toast.makeText(ServiceActivity.this, "Data Inserted Succesfully", Toast.LENGTH_LONG).show();
                //finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isUpdating = false;
            }
        });

    }
}