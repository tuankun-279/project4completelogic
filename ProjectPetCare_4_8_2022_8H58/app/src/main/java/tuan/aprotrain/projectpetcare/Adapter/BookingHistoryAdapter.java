package tuan.aprotrain.projectpetcare.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.Category;
import tuan.aprotrain.projectpetcare.entity.Pet;

public class BookingHistoryAdapter extends ArrayAdapter<Booking> {
    public BookingHistoryAdapter(Context context, ArrayList<Booking> bookingArrayList){
        super(context, R.layout.booking_histories_item,bookingArrayList);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Booking booking = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.booking_histories_item,parent,false);
        }
        ImageView imageQRBooking = convertView.findViewById(R.id.imageQRBooking);
        TextView textViewCategory = convertView.findViewById(R.id.textViewCategory);
        TextView tv_interval = convertView.findViewById(R.id.tv_duration);
        TextView tv_bookingPrice = convertView.findViewById(R.id.tv_bookingPrice);
        TextView tv_serviceCount = convertView.findViewById(R.id.tv_serviceCount);

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(booking.getBookingId(), BarcodeFormat.QR_CODE, 80, 80);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            imageQRBooking.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //get name
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String categoryName = null;
                for (DataSnapshot categorySnapshot : snapshot.child("Categories").getChildren()){
                    if(booking.getSelectedService().get(0).getCategoryId()
                            ==categorySnapshot.getValue(Category.class).getCategoryId()){
                        categoryName = categorySnapshot.getValue(Category.class).getCategoryName();
                    }
                }

                String petName = null;
                for (DataSnapshot petSnapshot : snapshot.child("Pets").getChildren()){
                    if(booking.getPetId() == petSnapshot.getValue(Pet.class).getPetId()){
                        petName = petSnapshot.getValue(Pet.class).getPetName();
                    }
                }

                textViewCategory.setText(categoryName + " for "+petName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tv_interval.setText(booking.getBookingStartDate() + " - "+booking.getBookingEndDate());

        tv_bookingPrice.setText(booking.getTotalPrice()+"$");
        tv_serviceCount.setText(booking.getSelectedService().size()+"services");
        return super.getView(position, convertView, parent);
    }
}
