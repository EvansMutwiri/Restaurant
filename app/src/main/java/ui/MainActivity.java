package ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vinge1718.restaurant.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.findRestaurantsButton)
    Button mFindRestaurantsButton;
    @BindView(R.id.locationEditText)
    EditText mLocationEditText;
    @BindView(R.id.appNameTextView)
    TextView mAppNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFindRestaurantsButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view == mFindRestaurantsButton){
        String location = mLocationEditText.getText().toString();
        Intent intent = new Intent(MainActivity.this, RestaurantsListActivity.class);
        intent.putExtra("location", location);
        startActivity(intent);
        }
    }
}

