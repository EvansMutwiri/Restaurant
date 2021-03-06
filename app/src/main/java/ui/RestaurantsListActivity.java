package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapters.RestaurantListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.evans.restaurant.Constants;
import com.evans.restaurant.R;
import models.Business;
import models.YelpBusinessesSearchResponse;
import network.YelpApi;
import network.YelpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsListActivity extends AppCompatActivity {
    private static final String TAG = RestaurantsListActivity.class.getSimpleName();
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

//    private SharedPreferences mSharedPreferences;
//    private String mRecentAddress;

    public List<Business> restaurants;

    private RestaurantListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String restaurant = ((TextView)view).getText().toString();
//                Toast.makeText(RestaurantsListActivity.this, restaurant, Toast.LENGTH_LONG).show();
//            }
//        });

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        YelpApi client = YelpClient.getClient();
        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");

        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
            @Override
            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
//                    List<Business> restaurantsList = response.body().getBusinesses();
//                    String[] restaurants = new String[restaurantsList.size()];
//                    String[] categories = new String[restaurantsList.size()];
//
//                    for (int i = 0; i < restaurants.length; i++){
//                        restaurants[i] = restaurantsList.get(i).getName();
//                    }
//
//                    for (int i = 0; i < categories.length; i++) {
//                        Category category = restaurantsList.get(i).getCategories().get(0);
//                        categories[i] = category.getTitle();
//                    }
//
//                    ArrayAdapter adapter
//                            = new MyRestaurantsArrayAdapter(RestaurantsListActivity.this, android.R.layout.simple_list_item_1, restaurants, categories);
//                    mListView.setAdapter(adapter);

                    restaurants = response.body().getBusinesses();
                    mAdapter = new RestaurantListAdapter(RestaurantsListActivity.this, restaurants);
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(RestaurantsListActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                    showRestaurants();
                } else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
                hideProgressBar();
                showFailureMessage();
                Toast.makeText(RestaurantsListActivity.this, "enter a valid search", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
//        mLocationTextView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}