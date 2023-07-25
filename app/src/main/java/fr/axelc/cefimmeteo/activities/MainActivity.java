package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.adapters.FavoriteAdapter;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.utils.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton mFloatingButtonFavorite;
    private EditText mEditTextMessage;
    private LinearLayout mLinearLayoutMain;
    private TextView mTextViewNoConnection;
    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private TextView mTextViewCityName;
    private TextView mTextViewCityDesc;
    private TextView mTextViewCityTemp;
    private ImageView mImageViewCityIcon;
    private City mCurrentCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mOkHttpClient = new OkHttpClient();

        mLinearLayoutMain = findViewById(R.id.linear_layout_current_city);
        mTextViewNoConnection = findViewById(R.id.text_view_no_connection);
        mFloatingButtonFavorite = findViewById(R.id.floating_action_button_favorite);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        mTextViewCityName = findViewById(R.id.text_view_city_name);
        mTextViewCityDesc = findViewById(R.id.text_view_city_desc);
        mTextViewCityTemp = findViewById(R.id.text_view_city_temp);
        mImageViewCityIcon = findViewById(R.id.image_view_city_weather);

        mFloatingButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FavoriteActivity.class);
                intent.putExtra(Util.KEY_MESSAGE, mEditTextMessage.getText().toString());
                startActivity(intent);
            }
        });

        if (Util.isActiveNetwork(mContext)) {
            Log.d("TAG", "Je suis connecté");

            Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?lat=47.390026&lon=0.688891&appid=01897e4\n" +
                    "97239c8aff78d9b8538fb24ea&units=metric&lang=fr").build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("TAG", "ça marche");
                    if (response.isSuccessful()) {
                        final String stringJson = response.body().string();
                        Log.d("TAG", stringJson);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    updateUi(stringJson);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TAG", e.toString());
                }
            });
        } else {
            Log.d("TAG", "Je ne suis pas connecté");
            updateViewNoConnection();
        }
    }

    public void updateViewNoConnection() {
        mLinearLayoutMain.setVisibility(View.INVISIBLE);
        mFloatingButtonFavorite.setVisibility(View.INVISIBLE);
        mTextViewNoConnection.setVisibility(View.VISIBLE);
    }

    public void updateUi(String stringJson) throws JSONException {
        City city = new City(stringJson);
        mTextViewCityName.setText(city.getmName());
        mTextViewCityDesc.setText(city.getmDescription());
        mTextViewCityTemp.setText(city.getmTemperature());
        mImageViewCityIcon.setImageResource(city.getmWeatherIcon());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}