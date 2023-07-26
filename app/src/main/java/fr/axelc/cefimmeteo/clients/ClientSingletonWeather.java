package fr.axelc.cefimmeteo.clients;

import fr.axelc.cefimmeteo.interrfaces.WeatherService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientSingletonWeather {
    private final static String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public final static String API_KEY = "01897e497239c8aff78d9b8538fb24ea";
    private static Retrofit retrofit;

    public static WeatherService getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit.create(WeatherService.class);
    }
}
