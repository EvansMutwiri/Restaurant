package ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import com.evans.restaurant.Constants;
import com.evans.restaurant.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mSearchedLocationReference;
    private ValueEventListener mSearchedLocationReferenceListener;

    @BindView(R.id.findRestaurantsButton)
    Button mFindRestaurantsButton;
    @BindView(R.id.locationEditText)
    EditText mLocationEditText;
    @BindView(R.id.appNameTextView)
    TextView mAppNameTextView;

//    private SharedPreferences mSharedPreferences;
//    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION); //pinpoint location node

        mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() { //attach listener
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //something changed!
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String location = locationSnapshot.getValue().toString();

                    Log.d("Locations updated", "location: " + location); //log
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //update UI here if error occured

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mEditor = mSharedPreferences.edit();

        mFindRestaurantsButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view == mFindRestaurantsButton){
        String location = mLocationEditText.getText().toString();

//        addToSharedPreferences(location);

//            if(!(location).equals("")){
//                addToSharedPreferences(location);
//            }
            saveLocationToFirebase(location);

            Intent intent = new Intent(MainActivity.this, RestaurantsListActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        }
    }
    private void saveLocationToFirebase(String location) {

//        push()to add new entries and not replace the existing ones
        mSearchedLocationReference.push().setValue(location);
    }

//    private void addToSharedPreferences(String location) {
//        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
    }

}

