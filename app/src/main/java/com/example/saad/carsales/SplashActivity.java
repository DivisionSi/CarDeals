package com.example.saad.carsales;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.gospelware.liquidbutton.LiquidButton;

import java.io.File;


public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String name,email,contact;
    TrackGPS GPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        name = "Default";
        contact="000";

        GPS = new TrackGPS(SplashActivity.this);

        mAuth= FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        Firebase ref=new Firebase("https://car-sales-f4f9c.firebaseio.com/").child("Users");



        if (mAuth.getCurrentUser() !=null) {
            email = mAuth.getCurrentUser().getEmail();
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.child("Email").getValue().toString().equals(email))
                    {name = dataSnapshot.child("Name").getValue().toString();
                        contact = dataSnapshot.child("Contact info").getValue().toString();}
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
      //  final ImageView img_logo=(ImageView) findViewById(R.id.imageView);
        LiquidButton liquidButton = (LiquidButton) findViewById(R.id.button);

        final TextView progress_txt= (TextView) findViewById(R.id.progress_txt);
        liquidButton.startPour();
        liquidButton.setAutoPlay(true);

        liquidButton.setPourFinishListener(new LiquidButton.PourFinishListener() {
            @Override
            public void onPourFinish() {
                //Signed in user
                if (mAuth.getCurrentUser() !=null) {
                    GPS.getLocation();
                   if(GPS.canGetLocation) {
                       Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                       Bundle b = new Bundle();
                       b.putString("Name", name);
                       b.putString("Contact Info", contact);
                       mainIntent.putExtras(b);

                       startActivity(mainIntent);
                       finish();
                   }
                   else{
                       /*GPS.showSettingsAlert();
                       finish();*/
                       AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
                       ab.setTitle("Please enable your GPS");
                       ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               finish();
                                dialog.cancel();
                           }
                       });
                       ab.show();
                   }
                }
                else
                {

                    Intent mainIntent = new Intent(SplashActivity.this, Sign_Signup.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }

            @Override
            public void onProgressUpdate(float progress)
            {
                progress_txt.setText(String.format("%.1f", progress * 100) + "%");
            }
        });
        //liquidButton.startPour();
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//
//                //Logo Visibility toggle
//                Animation fadeIn = new AlphaAnimation(0, 1);
//                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
//                fadeIn.setDuration(500);
//
//
//                new Handler().postDelayed(new Runnable(){
//                    @Override
//                    public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//
//                    }
//                }, 1000);
//
//            }
//        }, 500);
    }
}
