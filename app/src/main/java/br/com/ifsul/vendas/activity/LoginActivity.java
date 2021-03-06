package br.com.ifsul.vendas.activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.model.User;
import br.com.ifsul.vendas.setup.AppSetup;

import static br.com.ifsul.vendas.setup.AppSetup.user;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "loginActivity";
    private FirebaseAuth mAuth;
    private EditText etEmail, etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //obtém a instância do serviço de autenticação
        mAuth = FirebaseAuth.getInstance();
        AppSetup.mAuth = mAuth;
        //mapeia os campos de input
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);

        //trata o evento onClick do button
        findViewById(R.id.bt_sigin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();
                if(!email.isEmpty() && !senha.isEmpty()) {

                    signin(email,senha);
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
                    etEmail.setError(getString(R.string.input_error_invalido));
                    etSenha.setError(getString(R.string.input_error_invalido));
                }
            }


        });

//        //trata o evento onClick do button
//        findViewById(R.id.bt_sigup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = etEmail.getText().toString();
//                String senha = etSenha.getText().toString();
//                if(!email.isEmpty() && !senha.isEmpty()) {
//                    signup(email,senha);
//                }else{
//                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
//                    etEmail.setError(getString(R.string.input_error_invalido));
//                    etSenha.setError(getString(R.string.input_error_invalido));
//                }
//            }
//        });

        //trata o evento onClick do textview (reset de senha)
        findViewById(R.id.tvEsqueceuSenha_tela_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                if(!email.isEmpty()){
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Reset pass email sent to " + email);
                                        Toast.makeText(LoginActivity.this, "Reset da senha enviado para " + email, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Log.d(TAG, "Reset pass falhou." + task.getException());
                                        Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
                                        etEmail.setError(getString(R.string.input_error_invalido));
                                    }
                                }
                            });
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), R.string.snack_insira_email, Snackbar.LENGTH_LONG).show();
                    etEmail.setError(getString(R.string.input_error_invalido));
                }
            }
        });
    }

    private void signup(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            cadastrarUser();
                            sendEmailVerification();
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if(Objects.requireNonNull(task.getException()).getMessage().contains("email")){
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_already, Snackbar.LENGTH_LONG).show();
                                etEmail.setError(getString(R.string.input_error_invalido));
                            }else {
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

    private void cadastrarUser() {
        User user = new User();
        user.setFirebaseUser(mAuth.getCurrentUser());
        user.setFuncao("vendedor");
        user.setEmail(mAuth.getCurrentUser().getEmail());
        FirebaseDatabase.getInstance().getReference().child("vendas/users")
                .child(user.getFirebaseUser().getUid())
                .setValue(user);
        AppSetup.user = user;
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Email de verificação enviado para " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Envio de email para verifiacão falhou.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Log.d(TAG, "signInWithEmail:success");
                                setUserSessao(mAuth.getCurrentUser());
                            }else{
                                Snackbar.make(findViewById(R.id.container_activity_login), "Valide seu email para o singin.", Snackbar.LENGTH_LONG).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure ",  task.getException());
                            if(Objects.requireNonNull(task.getException()).getMessage().contains("password")){
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.password_fail, Snackbar.LENGTH_LONG).show();
                                etSenha.setError(getString(R.string.input_error_invalido));
                            }else{
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_fail, Snackbar.LENGTH_LONG).show();
                                etEmail.setError(getString(R.string.input_error_invalido));
                            }
                        }
                    }
                });
    }

    private void setUserSessao(final FirebaseUser firebaseUser) {

        FirebaseDatabase.getInstance().getReference()
                .child("vendas/users").child(firebaseUser.getUid())
                .addListenerForSingleValueEvent (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        user.setFirebaseUser(firebaseUser);
                        startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_problemas_signin), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
