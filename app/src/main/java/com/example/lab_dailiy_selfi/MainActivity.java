package com.example.lab_dailiy_selfi;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.example.lab_dailiy_selfi.databinding.ActivityMainBinding;
import com.example.lab_dailiy_selfi.helper.SharedPreferenceHelper;
import com.example.lab_dailiy_selfi.model.PictureModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;


    //for use
    List<Bitmap> bitmaps = new ArrayList<>();
    List<String> textList = new ArrayList<>();
    List<String> bitmapTextList = new ArrayList<>();
    MutableLiveData<PictureModel> pmData = new MutableLiveData<>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mBinding.mListView.setLayoutManager(linearLayoutManager);
        getDATA();


        RecyclerView.Adapter adapter = new RvAdapter(bitmaps, textList);
        mBinding.mListView.setAdapter(adapter);

        pmData.observe(this, model -> {
            List<Bitmap> bm_list = stringImagesToBitmapImagesList(model.getImages());
            List<String> txt_list = model.getTexts();
            bitmaps.clear();
            bitmaps.addAll(bm_list);
            textList.clear();
            textList.addAll(txt_list);
            mBinding.mListView.setAdapter(adapter);

        });

        mBinding.notificationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //notification test
                Log.d("NOTIFICATION", "test108");

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                mBuilder.setSmallIcon(R.drawable.camera_foreground);
                mBuilder.setContentTitle("Notification Alert, Click Me!");
                mBuilder.setContentText("Hi, This is Android Notification Detail!");

                Intent resultIntent = new Intent(getApplicationContext(), MyNotificationActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MyNotificationActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                mNotificationManager.notify(1, mBuilder.build());


            }
        });

        mBinding.openCamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camIntent, 100);
            }
        });

        mBinding.saveDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDATAstate();
            }
        });


    }


    public PictureModel stringToModelTest(String str) {
        ObjectMapper mapper = new ObjectMapper();
        PictureModel pm = null;
        try {
            pm = mapper.readValue(str, PictureModel.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pm;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String currentTime = String.valueOf(System.currentTimeMillis());
            textList.add(currentTime);
            bitmapTextList.add(bitmapToString(photo));

            PictureModel currentModel = pmData.getValue();
            if (currentModel != null) {
                currentModel.getTexts().add(currentTime);
                currentModel.getImages().add(bitmapToString(photo));
                pmData.postValue(currentModel);
            } else {
                PictureModel model = new PictureModel();
                model.getImages().add(bitmapToString(photo));
                model.getTexts().add(currentTime);
                pmData.postValue(model);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String bitmapToString(Bitmap bitmap) {
        Bitmap myBitmap = bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodeImage = Base64.getEncoder().encodeToString(b);
        return encodeImage;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected Bitmap stringToBitmap(String s) {
        byte[] b = Base64.getDecoder().decode(s);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected List<Bitmap> stringImagesToBitmapImagesList(List<String> list) {

        List<Bitmap> bitmaps = new ArrayList<>();
        for (String list_item : list) {
            bitmaps.add(stringToBitmap(list_item));
        }
        return bitmaps;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDATA() {
        String s = SharedPreferenceHelper.getINSTANCE(MainActivity.this).readDATA(String.valueOf(R.string.myDATA));
        // Log.d("SPDATA", "82:" + s);

        if (s != "") {
            PictureModel pictureModel = stringToModelTest(s);
            pmData.postValue(pictureModel);
            textList.addAll(pictureModel.getTexts());
            bitmapTextList.addAll(pictureModel.getImages());
            bitmaps.addAll(stringImagesToBitmapImagesList(pictureModel.getImages()));
        }

    }

    private void saveDATAstate() {
        PictureModel pictureModel = new PictureModel();
        pictureModel.setImages(bitmapTextList);
        pictureModel.setTexts(textList);
        SharedPreferenceHelper.getINSTANCE(MainActivity.this).writeDATA(String.valueOf(R.string.myDATA),
                pictureModel.modelToString());
    }


}