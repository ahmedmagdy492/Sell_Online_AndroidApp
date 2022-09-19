package com.magdyradwan.sellonline;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.magdyradwan.sellonline.adapters.CityAdapter;
import com.magdyradwan.sellonline.adapters.CountryAdapter;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.helpers.FileReaderHelper;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.IAuthRepo;
import com.magdyradwan.sellonline.repository.AuthRepo;
import com.magdyradwan.sellonline.responsemodels.RegistreResponseModel;
import com.magdyradwan.sellonline.viewmodels.RegisterViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, displayName, password, district, phoneNumber;
    private Spinner country, city;
    private ImageView imageView;
    private Uri imageProfile;
    private final ArrayList<String> countries;
    private final ArrayList<String> cities;
    private ActivityResultLauncher<String> imageChooser = registerForActivityResult(
      new ActivityResultContracts.GetContent(),
      result -> {
          if(result != null) {
              imageProfile = result;
              imageView.setImageURI(result);
          }
      }
    );

    public RegisterActivity() {
        countries = new ArrayList<>();
        countries.add("Egypt");

        cities = new ArrayList<>();
        cities.add("Alexandria");
        cities.add("Cairo");
        cities.add("Qalybia");
        cities.add("Deqhalia");
        cities.add("Mansoura");
        cities.add("El Bahira");
        cities.add("Benha");
        cities.add("El Monofia");
        cities.add("Sohag");
        cities.add("El Menia");
        cities.add("El Wady el Gadid");
        cities.add("Port Said");
        cities.add("Sina");
        cities.add("El Giza");
    }

    private void initViews() {
        email = findViewById(R.id.email_field);
        password = findViewById(R.id.password_field);
        displayName = findViewById(R.id.display_name);
        district = findViewById(R.id.district_field);
        phoneNumber = findViewById(R.id.phoneNumber_field);
        imageView = findViewById(R.id.profile_img);

        country = findViewById(R.id.country_field);
        city = findViewById(R.id.city_field);

        country.setAdapter(new CountryAdapter(this, R.layout.spinner_item, countries));
        city.setAdapter(new CityAdapter(this, R.layout.spinner_item, cities));
    }

    private boolean validateInput() {
        boolean isValid = true;
        StringBuilder stringBuilder = new StringBuilder();
        if(displayName.getText().toString().equals("")) {
            stringBuilder.append("Please Enter Display Name\n");
            isValid = false;
        }

        if(email.getText().toString().equals("")) {
            stringBuilder.append("Please Enter Email\n");
            isValid = false;
        }

        if(password.getText().toString().equals("")) {
            stringBuilder.append("Please Enter Password\n");
            isValid = false;
        }

        if(password.getText().toString().length() < 8 || password.getText().toString().length() > 16){
            stringBuilder.append("Please Enter from 8 to 16 digits password\n");
            isValid = false;
        }

        if(country.getSelectedItem().toString().equals("")) {
            stringBuilder.append("Please Select Country\n");
            isValid = false;
        }

        if(city.getSelectedItem().toString().equals("")) {
            stringBuilder.append("Please Select City\n");
            isValid = false;
        }

        if(phoneNumber.getText().toString().equals("")) {
            stringBuilder.append("Please Enter Phone number\n");
            isValid = false;
        }

        if(phoneNumber.getText().toString().length() != 11) {
            stringBuilder.append("Phone number must be Exactly 11 digits\n");
            isValid = false;
        }

        if(district.getText().toString().equals("")) {
            stringBuilder.append("Please Enter District\n");
            isValid = false;
        }

        if(imageProfile == null) {
            stringBuilder.append("Please Add a Profile Image\n");
            isValid = false;
        }

        if(!isValid) {
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    private RegisterViewModel mapToModel() throws IOException {
        return new RegisterViewModel(
                displayName.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                country.getSelectedItem().toString(),
                city.getSelectedItem().toString(),
                district.getText().toString(),
                phoneNumber.getText().toString(),
                Base64Converter.convertFromByteArrToBase64(FileReaderHelper.readUri(this, imageProfile)),
                "png"
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.create_new_account));
        }

        initViews();
        Button btnUploadImage = findViewById(R.id.upload_profile_img);
        btnUploadImage.setOnClickListener(v -> {
            imageChooser.launch("image/*");
        });

        Button btnSubmit = findViewById(R.id.btn_create_account);
        btnSubmit.setOnClickListener(v -> {
           if(validateInput()) {
               btnSubmit.setEnabled(false);
               btnSubmit.setText(getString(R.string.loading));
               try {
                   RegisterViewModel model = mapToModel();
                   ExecutorService executorService = Executors.newSingleThreadExecutor();
                   executorService.execute(() -> {
                       try {
                           HttpClient httpClient = new HttpClient(
                                   RegisterActivity.this,
                                   getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE)
                           );

                           IAuthRepo authRepo = new AuthRepo(httpClient);
                           RegistreResponseModel response = authRepo.register(model);
                           runOnUiThread(() -> {
                               this.finish();
                               Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                           });
                       }
                       catch (IOException | UnAuthorizedException | JSONException e) {
                           runOnUiThread(() -> {
                               Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                               btnSubmit.setEnabled(true);
                               btnSubmit.setText(getString(R.string.create_new_account));
                           });
                       }
                       catch (NoInternetException e) {
                           runOnUiThread(() -> {
                               Intent intent = new Intent(RegisterActivity.this, NoInternetActivity.class);
                               startActivity(intent);
                           });
                       }
                   });
               }
               catch (IOException e) {
                   Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   btnSubmit.setEnabled(true);
                   btnSubmit.setText(getString(R.string.create_new_account));
               }
           }
        });
    }
}