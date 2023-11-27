package com.example.admin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.Model.NotificationData;
import com.example.admin.Model.PushNotification;
import com.example.admin.R;
import com.example.admin.api.ApiUtilities;

import retrofit2.Call;
import retrofit2.Callback;

public class SendOffersActivity extends AppCompatActivity {

    Button send;
    EditText enterMessage,enterTitle;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_offers);

        send=findViewById(R.id.sendMessage);
        enterMessage=findViewById(R.id.enterOfferMessage);
        enterTitle=findViewById(R.id.enterTitle);






        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message=enterMessage.getText().toString().trim();
                String title=enterTitle.getText().toString().trim();
                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(title,message), "/topics/sendOffers");
                sendNotification(pushNotification);

            }
        });
    }

    private void sendNotification(PushNotification pushNotification) {
        ApiUtilities.getClient().sendNotification(pushNotification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, retrofit2.Response<PushNotification> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SendOffersActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(SendOffersActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(SendOffersActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}