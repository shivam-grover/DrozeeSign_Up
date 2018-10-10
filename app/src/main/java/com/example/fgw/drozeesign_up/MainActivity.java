package com.example.fgw.drozeesign_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.imageButton)
    Button imageButton;
    private FirebaseAuth mAuth;
    public String email,password,confirm;
    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance ();

    }

    @OnClick(R.id.imageButton)
    public void onViewClicked() {
        setSignupa();


    }

    private void setSignupa() {
        email =  editText.getText().toString();
        password =  editText2.getText().toString();
        confirm =  editText3.getText().toString();
        if(email.isEmpty ())
        {
            editText.setError ("Email is required");
            editText.requestFocus ();
            return;
        }
        if(password.isEmpty ())
        {
            editText2.setError ("Password is required");
            editText2.requestFocus ();
            return;
        }
        if(password.length ()<6)
        {
            editText2.setError ("Minimum length of password required is 6");
            editText2.requestFocus ();
            return;
        }
        if(!(password.equals(confirm)))
        {
            Toast.makeText(this, "Password does match can't", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword (email , password).addOnCompleteListener (new OnCompleteListener<AuthResult>( ) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressBar.setVisibility (View.GONE);
                if (task.isSuccessful()) {
                    String UserID = mAuth.getCurrentUser().getUid();
                    Toast.makeText(getApplicationContext(), "You are registered", Toast.LENGTH_SHORT).show();
                    user = mAuth.getCurrentUser();
                    user.sendEmailVerification()
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    // Re-enable button
//                                    findViewById(R.id.verify_email_button).setEnabled(true);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this,
                                                "Verification email sent to " + user.getEmail(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
//                                        Log.e(TAG, "sendEmailVerification", task.getException());
//                                        Toast.makeText(EmailPasswordActivity.this,
//                                                "Failed to send verification email.",
//                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
//                    Users users = new Users(email,false,UserID,false);
//                    databaseReference.child(UserID).setValue (users);

//                    startActivity(new Intent(SignUp.this,MapsActivity.class));}
                }

                else{
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        });


    }
}
