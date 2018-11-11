package com.example.user.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    LocationManager locationManager;
    double latitude;
    double longitude;
    ImageView imageVIew;
    TextView latText, lonText, temptext, maintext, humiditytext, windspeedtext, cloudstext, countrytext;
    Button button;
    Retrofit retrofit;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        imageVIew = findViewById(R.id.image);
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.BASEURL).build();
        apiService = retrofit.create(ApiService.class);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void initView() {
        //뷰세팅
        latText = findViewById(R.id.latitude);
        lonText = findViewById(R.id.longitude);
        temptext = findViewById(R.id.tempresult);
        maintext = findViewById(R.id.maintext);
        humiditytext = findViewById(R.id.humiditytext);
        windspeedtext = findViewById(R.id.windspeedtext);
        countrytext = findViewById(R.id.countrytext);
        cloudstext = findViewById(R.id.cloudstext);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
          /*현재 위치에서 위도경도 값을 받아온뒤 우리는 지속해서 위도 경도를 읽어올것이 아니니
        날씨 api에 위도경도 값을 넘겨주고 위치 정보 모니터링을 제거한다.*/
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //위도 경도 텍스트뷰에 보여주기
        String lat = String.format("%.2f", latitude);
        String lon = String.format("%.2f", longitude);
        latText.setText(String.valueOf(lat) + "°");
        lonText.setText(String.valueOf(lon) + "°");
        //날씨 가져오기 통신
        getWeather(latitude, longitude);
        //위치정보 모니터링 제거
        locationManager.removeUpdates(MainActivity.this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (locationManager != null) {
                    requestLocation();
                }
        }
    }

    private void requestLocation() {
        //사용자로 부터 위치정보 권한체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, this);
        }
    }

    private void getWeather(double latitude, double longitude) {

        Call<WeatherResult> call = apiService.getHourly(latitude, longitude, ApiService.APPKEY);
        call.enqueue(new Callback<WeatherResult>() {

            @Override
            public void onResponse(@NonNull Call<WeatherResult> call, Response<WeatherResult> response) {
                if (response.isSuccessful()) {
                    WeatherResult result = response.body();
                    Weather weather = result.getWeathers().get(0);
                    Main mains = result.getMains();
                    String country = result.getSys().getCountry();
                    Clouds cloud = result.getCloud();

                    double temperature = result.getMains().getTemp() - 273.15; //온도
                    double speed = result.getWinds().getWind();

                    //String temp = String.valueOf(temperature);
                    String temp = String.format("%.1f", temperature);
                    String wind = String.format("%.1f", speed);
                    String clouds = String.valueOf(cloud.getClouds());
                    String humidity = String.valueOf(mains.getHumidity());

                    temptext.setText(temp + "℃");
                    maintext.setText(weather.getMain());
                    humiditytext.setText(humidity + "%");
                    windspeedtext.setText(wind + "meter");
                    cloudstext.setText(clouds);
                    countrytext.setText(country);
                    String icon = weather.getIcon();
                    Glide
                            .with(MainActivity.this)
                            .load(apiService.iconUrl + icon +".png")
                            .into(imageVIew);

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT);
            }
        });

    }


}
