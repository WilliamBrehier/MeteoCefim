package fr.axelc.cefimmeteo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.adapters.FavoriteAdapter;
import fr.axelc.cefimmeteo.clients.ClientSingletonWeather;
import fr.axelc.cefimmeteo.databinding.ActivityFavoriteBinding;
import fr.axelc.cefimmeteo.interrfaces.WeatherService;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.models.CityApi;
import fr.axelc.cefimmeteo.utils.Util;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Request;
//import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private ArrayList<City> mCities;
    private ArrayList<CityApi> mCitiesApi;
    private ActivityFavoriteBinding binding;
    private FavoriteAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setTitle(getTitle());
        mContext = this;

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null);
                final EditText editTextCity = (EditText) v.findViewById(R.id.edit_text_dialog_city);
                builder.setView(v);

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editTextCity.getText().toString().length() > 0) {
                            updateWeatherDataCityName(editTextCity.getText().toString());
                        }
                    }
                });
                builder.create().show();
            }
        });


//        mCities = new ArrayList<City>();

//        City city1 = new City("Tours", "Il fait chaud", "39°C", R.drawable.weather_sunny_white);
//        City city2 = new City("Paris", "Il fait chaud", "41°C", R.drawable.weather_sunny_white);
//        City city3 = new City("Toulouse", "Il fait très chaud", "43°C", R.drawable.weather_sunny_white);
//        City city4 = new City("Marseille", "Il fait vraiment très chaud", "46°C", R.drawable.weather_sunny_white);
//
//        mCities.add(city1);
//        mCities.add(city2);
//        mCities.add(city3);
//        mCities.add(city4);

        mCitiesApi = new ArrayList<CityApi>();


        binding.included.recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteAdapter(mContext, mCitiesApi);
        binding.included.recyclerViewCities.setAdapter(mAdapter);
    }

//    public void updateWeatherDataCityName(final String cityName) {
//        City city = new City(cityName, "Ensoleillé", "28°C", R.drawable.weather_sunny_grey);
//        mCities.add(city);
//        mAdapter.notifyDataSetChanged();
//    }

    public void updateWeatherDataCityName(String name) {
        if (Util.isActiveNetwork(mContext)) {
            Log.d("TAG", "Je suis connecté2");

            Call<CityApi> call = ClientSingletonWeather.getClient().getCityApi(name, ClientSingletonWeather.API_KEY);

            call.enqueue(new Callback<CityApi>() {
                @Override
                public void onResponse(Call<CityApi> call, Response<CityApi> response) {
                    if (response.isSuccessful()) {
                        CityApi cityApi = response.body();
                        mCitiesApi.add(cityApi);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        // Traitement des erreurs
                    }
                }

                @Override
                public void onFailure(Call<CityApi> call, Throwable t) {
                    Log.d("test", "ça marche pas 2");
                }
            });
        } else {
            Log.d("TAG", "Je ne suis pas connecté");
        }
    }
}