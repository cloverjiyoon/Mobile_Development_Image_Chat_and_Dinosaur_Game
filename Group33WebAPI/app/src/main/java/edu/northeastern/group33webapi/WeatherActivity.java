package edu.northeastern.group33webapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.northeastern.group33webapi.Retrofit.ApiClient;
import edu.northeastern.group33webapi.Retrofit.HttpService;
import edu.northeastern.group33webapi.Retrofit.JsonResponded;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    ImageView search;
    TextView tempText, descText, humidityText;
    EditText textField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_api);

        search = findViewById(R.id.search);
        tempText = findViewById(R.id.tempText);
        descText = findViewById(R.id.descText);
        humidityText = findViewById(R.id.humidityText);
        textField = findViewById(R.id.textField);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call API
                getWeatherData(textField.getText().toString().trim());

            }
        });
    }

    private void getWeatherData(String name) {

        HttpService httpService = ApiClient.getClient().create(HttpService.class);

        Call<JsonResponded> call = httpService.getWeatherData(name);

        call.enqueue(new Callback<JsonResponded>() {
            @Override
            public void onResponse(Call<JsonResponded> call, Response<JsonResponded> response) {

                try {
                    tempText.setText("Temp" + " " + response.body().getMain().getTemp() + "Celsius");
                    descText.setText("Pressure" + " " + response.body().getMain().getPressure() + "hPa");
                    humidityText.setText("Humidity" + " " + response.body().getMain().getHumidity() + "%");
                } catch (Exception e) {
                    e.printStackTrace();
                    tempText.setText("Please type the valid name of the city");
                    descText.setText("Pressure : N/A");
                    humidityText.setText("Humidity: N/A");
                }
            }
            @Override
            public void onFailure(Call<JsonResponded> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}