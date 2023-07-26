package fr.axelc.cefimmeteo.interrfaces;

import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.models.CityApi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather?units=metric&lang=fr")
    Call<CityApi> getCityApi(@Query("q") String cityName, @Query("appid") String apiKey);

}
