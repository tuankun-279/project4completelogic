package tuan.aprotrain.projectpetcare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tuan.aprotrain.projectpetcare.Adapter.BookingHistoryAdapter;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.Pet;

public class BookingHistoryActivity extends AppCompatActivity {
    ListView listViewBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        listViewBooking = findViewById(R.id.listViewBooking);
        ArrayList<Booking> bookingList = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Bookings").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotBooking) {
                for (DataSnapshot bookings: snapshotBooking.getChildren()) {
                    Booking booking = bookings.getValue(Booking.class);
                    reference.child("Pets").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotPet) {
                            for (DataSnapshot pets:snapshotPet.getChildren()){
                                Pet pet = pets.getValue(Pet.class);
                                if (pet.getUserId().equals(user.getUid())
                                && pet.getPetId() == booking.getPetId()) {
                                    bookingList.add(booking);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BookingHistoryAdapter bookingHistoryAdapter = new BookingHistoryAdapter(BookingHistoryActivity.this,bookingList);

        listViewBooking.setAdapter(bookingHistoryAdapter);
        listViewBooking.setClickable(true);
    }
}