/*package com.example.sept1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class upload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
    }
}

*/

package com.example.eplatdoc_2;

import android.app.Activity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.app.SearchManager;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class upload extends AppCompatActivity {

    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
   // firebaseAuth = FirebaseAuth.getInstance();
    CardView c1,c2,c3;
    ImageView i1;
    int a = 0;
    Uri uri= null;
    Bitmap bitmap;
    Bitmap bitmap1;
    String str = "jpg";

    FirebaseAutoMLRemoteModel remoteModel =
            new FirebaseAutoMLRemoteModel.Builder("EplantDoc_2").build();


    FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder optionsBuilder;

    FirebaseVisionImageLabeler labeler;
    FirebaseVisionImage image;

    Boolean camera_called = false;
    Boolean aBoolean = false;
    private StorageReference mStorageRef;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        c1 = findViewById(R.id.card1);
        floatingActionButton = findViewById(R.id.fab);
        i1 = findViewById(R.id.image1);
        c2 = findViewById(R.id.upload);

        c3= findViewById(R.id.logout);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
          //          Toast.makeText(MainActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(upload.this, MainActivity.class);
                    startActivity(I);
                }
            }
        };

        Toast.makeText(getApplicationContext(),"entered upload page!!!!!!!!!!",Toast.LENGTH_LONG).show();

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"sucess !!!!!!!",Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isDownloaded) {

                        optionsBuilder = new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(remoteModel);
                        Toast.makeText(getApplicationContext(),"entered ???",Toast.LENGTH_SHORT).show();
                        FirebaseVisionOnDeviceAutoMLImageLabelerOptions options = optionsBuilder
                                .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                                // to determine an appropriate threshold.
                                .build();


                        try {
                            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
                        } catch (FirebaseMLException e) {
                            Toast.makeText(getApplicationContext(),"error occurred !!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        mStorageRef = FirebaseStorage.getInstance().getReference();

        Toast.makeText(getApplicationContext(),"passed ml model buld code!!!!***!!!!!",Toast.LENGTH_LONG).show();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"selected floating action button!!!!***!!!!!",Toast.LENGTH_LONG).show();
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,500);
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"selected load from gallrey!!!!***!!!!!",Toast.LENGTH_LONG).show();
                FileChooser();
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uri== null)
                {
                    // Toast.makeText(getApplicationContext(),"entered uri = null block !!",Toast.LENGTH_LONG).show();


                    if(a == 0)
                    {
                        Toast.makeText(getApplicationContext(),"Select an Image to upload!!",Toast.LENGTH_LONG).show();

                    }
                    else{
                        if(aBoolean == false)
                        {
                            //Toast.makeText(getApplicationContext(),"correct till now :(",Toast.LENGTH_SHORT).show();
                            File2();
                        }

                    }

                }
                else{

                    //Toast.makeText(getApplicationContext(),"entered uri != null block !!",Toast.LENGTH_LONG).show();
                    if(aBoolean){
                        FileUploader();}
                    else if(aBoolean==false){
                        File2();
                    }

                }


            }
        });



        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"logout button is pressed!!!!***!!!!!",Toast.LENGTH_LONG).show();
               // FileChooser();
         //   firebase.auth().signOut;

//                mGoogleSignInClient.signOut();
            firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                       // user.

                    }
                };







            }
        });



    }


    private void FileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null)
        {
            a = 20;
            uri = data.getData();
            i1.setImageURI(uri);
            aBoolean = true;
            try {
                InputStream  pictureinputStream = getContentResolver().openInputStream(uri);
                 bitmap1 = BitmapFactory.decodeStream(pictureinputStream);
                if(bitmap1 != null){
                    Toast.makeText(getApplicationContext(),"bitmap1 is not empty",Toast.LENGTH_SHORT).show();
                }

                if(bitmap1 == null){
                    Toast.makeText(getApplicationContext(),"bitmap1 is  empty",Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
        else if (requestCode == 500 && resultCode == Activity.RESULT_OK )
        {
            a = -20;
            aBoolean = false;
            uri = null;
            camera_called = true;
            assert data != null;
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            i1.setImageBitmap(bitmap);
            Toast.makeText(getApplicationContext(),"correct ???????????",Toast.LENGTH_SHORT).show();
            assert bitmap != null;
        }
        else{
            a = 0;
        }
    }
    private String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));

    }
    void FileUploader()
    {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap1);


        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        Toast.makeText(getApplicationContext(),"task completed sucessfully !!!",Toast.LENGTH_SHORT).show();

                        float max=(float) 0.1;
                        String result = "No disease";
                        for (FirebaseVisionImageLabel label: labels) {
                            String text = label.getText();
                            float confidence = label.getConfidence();
                            if(confidence > max){
                                max = confidence;
                                result = text;
                            }
                        }


                        String a= "Pepper_bell_Bacterial_spot";
                        String b ="Potato_Early_blight";
                        String c = "Tomato_Bacterial_spot";
                        String d ="Tomato_Early_blight";
                        String e ="Tomato_Late_blight";
                        String f="Tomato_Leaf_Mold";
                        String g ="Tomato_Septoria_leaf_spot";
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        if(result.equals(a))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "http://vegetablemdonline.ppath.cornell.edu/factsheets/Pepper_BactSpot.htm");
                            startActivity(intent);

                    //        Toast.makeText(getApplicationContext(),"Pepper_bell_Bacterial_spot",Toast.LENGTH_SHORT).show();
                        }
                        else if(result.equals(b))
                        {
                            Intent intent1 = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent1.putExtra(SearchManager.QUERY, "http://vegetablemdonline.ppath.cornell.edu/factsheets/Potato_EarlyBlt.htm");
                            startActivity(intent1);
                        }
                        else if(result.equals(c))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://tomatodiseasehelp.com/bacterial-spots");
                            startActivity(intent);
                        }
                        else if(result.equals(d))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://www.planetnatural.com/pest-problem-solver/plant-disease/early-blight/");
                            startActivity(intent);
                        }
                        else if(result.equals(e))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://content.ces.ncsu.edu/tomato-late-blight");
                            startActivity(intent);
                        }
                        else if(result.equals(f))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://tomatodiseasehelp.com/leaf-mold");
                            startActivity(intent);
                        }
                        else if(result.equals(g))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://www.thespruce.com/identifying-and-controlling-septoria-leaf-spot-of-tomato-1402974");
                            startActivity(intent);
                        }
                        else
                        {
                            Intent I = new Intent(upload.this, ABC.class);
                            startActivity(I);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"task failed  !!!",Toast.LENGTH_SHORT).show();
                    }
                });




        StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(uri));
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading from storage...");
        progressDialog.show();

        Ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Upload Success :)",Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(getApplicationContext(),ABC.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Check Internet Connection :(",Toast.LENGTH_SHORT).show();
                    }
                });


    }



    void File2()
    {
        Toast.makeText(getApplicationContext(),"entered file 2 block:(",Toast.LENGTH_SHORT).show();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        Toast.makeText(getApplicationContext(),"task completed sucessfully !!!",Toast.LENGTH_SHORT).show();

                        float max=(float) 1;
                        String result = "nothing is there";

                        for (FirebaseVisionImageLabel label: labels) {
                            String text = label.getText();
                            float confidence = label.getConfidence();
                            if(confidence > max){
                                max = confidence;
                                result = text;
                            }
                        }

                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();


                        String a= "Pepper_bell_Bacterial_spot";
                        String b ="Potato_Early_blight";
                        String c = "Tomato_Bacterial_spot";
                        String d ="Tomato_Early_blight";
                        String e ="Tomato_Late_blight";
                        String f="Tomato_Leaf_Mold";
                        String g ="Tomato_Septoria_leaf_spot";
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        if(result.equals(a))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "http://vegetablemdonline.ppath.cornell.edu/factsheets/Pepper_BactSpot.htm");
                            startActivity(intent);

                            //        Toast.makeText(getApplicationContext(),"Pepper_bell_Bacterial_spot",Toast.LENGTH_SHORT).show();
                        }
                        else if(result.equals(b))
                        {
                            Intent intent1 = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent1.putExtra(SearchManager.QUERY, "http://vegetablemdonline.ppath.cornell.edu/factsheets/Potato_EarlyBlt.htm");
                            startActivity(intent1);
                        }
                        else if(result.equals(c))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://tomatodiseasehelp.com/bacterial-spots");
                            startActivity(intent);
                        }
                        else if(result.equals(d))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://www.planetnatural.com/pest-problem-solver/plant-disease/early-blight/");
                            startActivity(intent);
                        }
                        else if(result.equals(e))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://content.ces.ncsu.edu/tomato-late-blight");
                            startActivity(intent);
                        }
                        else if(result.equals(f))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://tomatodiseasehelp.com/leaf-mold");
                            startActivity(intent);
                        }
                        else if(result.equals(g))
                        {
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "https://www.thespruce.com/identifying-and-controlling-septoria-leaf-spot-of-tomato-1402974");
                            startActivity(intent);
                        }
                        else
                        {
                            Intent I = new Intent(upload.this, ABC.class);
                            startActivity(I);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"task failed  !!!",Toast.LENGTH_SHORT).show();
                    }
                });







        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://eplantdoc-2.appspot.com");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image from camera...");
        progressDialog.show();


        StorageReference mountainsRef = storageRef.child(System.currentTimeMillis()+"."+str);

        Toast.makeText(getApplicationContext(),"entered file 2 block  completed:(",Toast.LENGTH_SHORT).show();


        i1.setDrawingCacheEnabled(true);
        i1.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data2 = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data2);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(),"Check Internet Connection :(",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.cancel();
                Toast.makeText(getApplicationContext(),"Upload Success :)",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),ABC.class));

            }
        });


    }
}
