package edu.northeastern.group33webapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    LinkAdapter adapter;
    List<Cloud> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_api);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);
        adapter = new LinkAdapter(postList);
        recyclerView.setAdapter(adapter);

        search = findViewById(R.id.search);
        tempText = findViewById(R.id.tempText);
        descText = findViewById(R.id.descText);
        humidityText = findViewById(R.id.humidityText);
        textField = findViewById(R.id.textField);


        String temp, humidity, pressure, temp_min, temp_max;



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call API
                getWeatherData(textField.getText().toString().trim());
                fetchPosts(textField.getText().toString().trim());
            }
        });
    }

    private void getWeatherData(String name) {
        progressBar.setVisibility(View.VISIBLE);
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
//                linkList.add(new Link(response.body().getMain().getTemp(), response.body().getMain().getPressure(),
//                        response.body().getMain().getHumidity(), response.body().getMain().getTemp_min(), response.body().getMain().getTemp_max()));

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