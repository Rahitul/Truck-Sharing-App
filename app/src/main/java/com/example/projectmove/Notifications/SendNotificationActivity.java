package com.example.projectmove.Notifications;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.projectmove.R;

public class SendNotificationActivity extends AppCompatActivity {

    //private TextView textView;
    //private EditText editTextTitle, editTextBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        /*final TestUser user = (TestUser) getIntent().getSerializableExtra("users");


        textView = findViewById(R.id.textViewUser);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextBody = findViewById(R.id.editTextBody);


        findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(user);
            }
        });
    }

    private  void sendNotification(TestUser user){
        String title = editTextTitle.getText().toString().trim();
        String body = editTextBody.getText().toString().trim();

        if(title.isEmpty()){
            editTextTitle.setError("Title required");
            editTextTitle.requestFocus();
            return;
        }

        if(body.isEmpty()){
            editTextBody.setError("Body required");
            editTextBody.requestFocus();
            return;
        }*/


    }
}
