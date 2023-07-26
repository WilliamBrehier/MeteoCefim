package fr.axelc.cefimmeteo.adapters;

import static fr.axelc.cefimmeteo.utils.Util.setWeatherIcon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.models.CityApi;
import fr.axelc.cefimmeteo.utils.Util;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CityApi> mCitiesApi;

    public FavoriteAdapter(Context mContext, ArrayList<CityApi> mCitiesApi) {
        this.mContext = mContext;
        this.mCitiesApi = mCitiesApi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_city, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityApi cityApi = mCitiesApi.get(position);
        holder.mCityApi = cityApi;
        holder.mTextViewCityName.setText(cityApi.getName());
        holder.mTextViewCityDescription.setText(cityApi.getWeather().get(0).getDescription());
        holder.mTextViewCityTemperature.setText(Double.toString(cityApi.getMain().getTemp()));
        int actualId = cityApi.getWeather().get(0).getId();
        long sunrise = cityApi.getSys().getSunrise();
        long sunset = cityApi.getSys().getSunset();
        holder.mImageViewCityIcon.setImageResource(Util.setWeatherIcon(actualId));
    }

    @Override
    public int getItemCount() {
        return mCitiesApi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView mTextViewCityName;
        private TextView mTextViewCityDescription;
        private TextView mTextViewCityTemperature;
        private ImageView mImageViewCityIcon;
        public CityApi mCityApi;
        public ViewHolder(View view) {
            super(view);
            mTextViewCityName = view.findViewById(R.id.city_name);
            mTextViewCityDescription = view.findViewById(R.id.city_description);
            mTextViewCityTemperature = view.findViewById(R.id.city_temperature);
            mImageViewCityIcon = view.findViewById(R.id.city_icon);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Supprimer " + mCityApi.getName() + " ?");

            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mCitiesApi.remove(mCityApi);
                    notifyDataSetChanged();
                }
            });
            builder.create().show();
            return false;
        }
    }
}