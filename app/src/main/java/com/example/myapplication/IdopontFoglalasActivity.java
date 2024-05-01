package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IdopontFoglalasActivity extends AppCompatActivity {
    private static final String LOG_TAG = IdopontFoglalasActivity.class.getName();
    private FirebaseUser user;
    Button logoutButton;
    Button foglalasButton;
    Animation rotateAnimation;
    Animation blinkAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idopont_foglalas);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG, "Authenticated user");
        }else{
            Log.d(LOG_TAG, "Unauthenticated user");
            finish();
        }

        logoutButton = findViewById(R.id.logoutBT);
        foglalasButton = findViewById(R.id.foglalasBT);

        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.porges);
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.villogas);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButton.startAnimation(rotateAnimation);

                finish();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(IdopontFoglalasActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(IdopontFoglalasActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        foglalasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foglalasButton.startAnimation(blinkAnimation);
                makeNotification();
            }
        });

    }

    public void makeNotification(){
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelID);
        builder.setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Masszázs app")
                .setContentText("Sikeres foglalás")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data","valami ertek");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID,"Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }




}