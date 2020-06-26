package com.example.ifood.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.activity.helper.ConfiguracaoFirebase;
import com.example.ifood.activity.helper.UsuarioFirebase;
import com.example.ifood.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoEntrar, botaoCadastrar;
    private EditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;
    private LinearLayout linearUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();

        inicializaComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();

        verificarUsuarioLogado();


        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CadatroActivity.class);

                startActivity(i);
            }
        });



        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()){
                    if (!senha.isEmpty()){

                        autenticacao.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){//no caso de login com sucesso

                                    Toast.makeText(AutenticacaoActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();

                                    //Verifica o tipo do cadastro
                                    UsuarioFirebase.redirecionaTipoCadastroLogado(AutenticacaoActivity.this);


                                } else {
                                    //no caso de login sem sucesso
                                    Toast.makeText(AutenticacaoActivity.this, "Erro ao fazer login: "+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else{
                        Toast.makeText(AutenticacaoActivity.this, "Favor preencher a senha", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AutenticacaoActivity.this, "Favor preencher o e-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificarUsuarioLogado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();

        if (usuarioAtual!=null){
            UsuarioFirebase.redirecionaTipoCadastroLogado(AutenticacaoActivity.this);
        }

    }


    private void inicializaComponentes(){
        campoEmail=findViewById(R.id.editTextEmail);
        campoSenha=findViewById(R.id.editTextSenha);
        botaoEntrar=findViewById(R.id.buttonEntra);
        botaoCadastrar=findViewById(R.id.buttonCadastrar);
    }
}
