package edu.northeastern.group33webapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.group33webapi.Retrofit.ApiClient;
import edu.northeastern.group33webapi.Retrofit.HttpService;
import edu.northeastern.group33webapi.Retrofit.JsonResponded;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.widget.Toast;

import edu.northeastern.group33webapi.Retrofit.LoadingDialog;



public class WeatherActivity extends AppCompatActivity {

    ImageView search;
    TextView  weatherText, tempText, descText, humidityText;
    EditText textField;
    int imgID;

    // Clover
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    LinkAdapter adapter;
    List<Cloud> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_api);

        // Recycler View
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LinkAdapter(postList);
        recyclerView.setAdapter(adapter);


        search = findViewById(R.id.search);
        weatherText = findViewById(R.id.weatherText);
        tempText = findViewById(R.id.tempText);
        descText = findViewById(R.id.descText);
        humidityText = findViewById(R.id.humidityText);
        textField = findViewById(R.id.textField);
        LoadingDialog loadingDialog = new LoadingDialog(WeatherActivity.this);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call API
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },500);

                getWeatherData(textField.getText().toString().trim());
                fetchPosts(textField.getText().toString().trim());

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
                    String weatherType = response.body().getWeather().getMain();
                    View temp = findViewById(R.id.weatherActivity);
                    if (weatherType.equals("Clear") | weatherType.equals("Sunny")) {
                        temp.setBackground(getResources().getDrawable(R.drawable.sunny));
                    } else if (weatherType.equals("Clouds")) {
                        temp.setBackground(getResources().getDrawable(R.drawable.cloudy));
                    } else if (weatherType.equals("Rain")) {
                        temp.setBackground(getResources().getDrawable(R.drawable.rainy));
                    } else {
                        temp.setBackground(getResources().getDrawable(R.drawable.background));
                    }


                    weatherText.setText(weatherType);
                    tempText.setText("Temp" + " " + response.body().getMain().getTemp() + "Celsius");
                    descText.setText("Pressure" + " " + response.body().getMain().getPressure() + "hPa");
                    humidityText.setText("Humidity" + " " + response.body().getMain().getHumidity() + "%");
                } catch (Exception e) {
                    e.printStackTrace();
                    weatherText.setText("");
                    tempText.setText("Please type the valid name of the city");
                    descText.setText("Pressure : N/A");
                    humidityText.setText("Humidity: N/A");
                }
            }
            @Override
            public void onFailure(Call<JsonResponded> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchPosts(String name) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getRetrofitClient().getListData(name).enqueue(new Callback <ExampleList>() {
            @Override
            public void onResponse(Call <ExampleList> call, Response <ExampleList> response) {
                if(response.isSuccessful() && response.body() != null){
                    ExampleList temp = response.body();
                    List<Example> tempList = temp.getExamples();
                    for(int i = 0; i < tempList.size(); i++){
                        postList.add(new Cloud(tempList.get(i).getCloud().getCloudlevel()));
                    }

                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call <ExampleList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(WeatherActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}