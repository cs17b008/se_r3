/*package com.example.sept1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }
}
*/
package com.example.eplatdoc_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    public EditText emailId, passwd,passwd2;
    CardView btnSignUp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.gmail);
        passwd = findViewById(R.id.password);
        passwd2=findViewById(R.id.password2);
        btnSignUp = findViewById(R.id.button2);





        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                String paswd2=passwd2.getText().toString();
                if (emailID.isEmpty())
                {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                }
                else if (paswd.isEmpty())
                {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                }
                else if (!paswd.equals(paswd2))
                {
                    passwd2.setError("Enter same password ");
                }


                else if (!(emailID.isEmpty() && paswd.isEmpty()))
                {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(signup.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(signup.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(signup.this, MainActivity.class));
                            }
                        }
                    });
                }

                else {
                    Toast.makeText(signup.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
