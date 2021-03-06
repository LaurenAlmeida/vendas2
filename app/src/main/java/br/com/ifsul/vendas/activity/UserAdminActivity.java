package br.com.ifsul.vendas.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class UserAdminActivity extends AppCompatActivity {
    private static final String TAG = "useradm";
    private FirebaseAuth mAuth;
    private EditText etEmailUser, etPasswordUser, etNomeUser, etSobrenomeUser , etFuncao;
    private Button cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        //ativa o botão home na actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //obtém a instância do serviço de autenticação
        mAuth = FirebaseAuth.getInstance();
        AppSetup.mAuth = mAuth;

        //mapeia os campos de input
        etNomeUser = findViewById(R.id.etNomeUser);
        etSobrenomeUser = findViewById(R.id.etSobrenomeUser);
        etEmailUser = findViewById(R.id.etEmailUser);
        etPasswordUser = findViewById(R.id.etPasswordUser);
        etFuncao = findViewById(R.id.etFuncao);
        cadastrar = findViewById(R.id.btCadastrarUser);


        //trata o evento onClick do button
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailUser.getText().toString();
                String senha = etPasswordUser.getText().toString();

                if(!email.isEmpty() && !senha.isEmpty()) {
                    signup(email,senha);
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
                    etEmailUser.setError(getString(R.string.input_error_invalido));
                    etPasswordUser.setError(getString(R.string.input_error_invalido));
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
//                            sendEmailVerification();
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (Objects.requireNonNull(task.getException()).getMessage().contains("email")) {
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_already, Snackbar.LENGTH_LONG).show();
                                etEmailUser.setError(getString(R.string.input_error_invalido));
                            } else {
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

    private void cadastrarUser() {
        User user = new User();
        user.setFirebaseUser(mAuth.getCurrentUser());
        user.setNome(etNomeUser.getText().toString());
        user.setSobrenome(etSobrenomeUser.getText().toString());
        user.setFuncao(etFuncao.getText().toString());
        user.setEmail(mAuth.getCurrentUser().getEmail());
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(user.getFirebaseUser().getUid())
                .setValue(user);
        AppSetup.user = user;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}