package com.example.andres.firebasedatabases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Uz9m0OKQtYCC3nNHAHNFzT5x7";
    private static final String TWITTER_SECRET = "haX57zc1It84ccfKUDkFLH5DQzeHtcsg0pD2ipU6uXwD28iZeP";


    private Button addToDataBase;
    private Button uploadImage;
    private Button downloadImage;
    private TextView mTxtContent;
    private EditText mEdtName;
    private ImageView imageView;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mMyRef = mDatabase.getReference("persons");

    FirebaseStorage mStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        addToDataBase = (Button) findViewById(R.id.btnSi);
        uploadImage = (Button) findViewById(R.id.btnNo);
        downloadImage = (Button) findViewById(R.id.btnDownload);
        findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInActivity.show(MainActivity.this);
            }
        });

        mTxtContent = (TextView) findViewById(R.id.txtContent);

        mEdtName = (EditText) findViewById(R.id.edtName);

        imageView = (ImageView) findViewById(R.id.imageView);


        addToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("first", "Fred");
                updates.put("last", "Swanson");
                mMyRef.child("test").updateChildren(updates);
                updates.clear();
                updates.put("first", "Fred1");
                updates.put("last", "Swanson2");
                mMyRef.child("test2").updateChildren(updates);

            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                imageView.setDrawingCacheEnabled(false);
                byte[] data = baos.toByteArray();

                String path = "firebasetest/" + UUID.randomUUID() + ".png";
                StorageReference storageReference = mStorage.getReference(path);

                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this, " UPLOADED!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final long ONE_MEGABYTE = 1024 * 1024;

                mStorage.getReference().child("firebasetest/preview.gif")
                        .getBytes(ONE_MEGABYTE)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Toast.makeText(MainActivity.this, " Downloaded!", Toast.LENGTH_LONG).show();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                imageView.setImageBitmap(bitmap);
                            }
                        });
            }
        });

        findViewById(R.id.btnAuth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.show(MainActivity.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //mTxtContent.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
