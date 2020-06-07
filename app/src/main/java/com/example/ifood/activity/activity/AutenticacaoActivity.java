package com.example.ifood.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.activity.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoEntrar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();

        inicializaComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();

        verificarUsuarioLogado();

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()){
                    if (!senha.isEmpty()){

                        //Verificar o switch
                        if (tipoAcesso.isChecked()){
                            //no caso de cadastro

                            autenticacao.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){ //caso o cadastro for sucedido
                                        Toast.makeText(AutenticacaoActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();

                                        irTelaPrincipal();

                                    }else{ //caso ocorra um erro no cadastro
                                        String erroExcecao = "";

                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e){
                                            erroExcecao = "Digite uma senha mais forte!";
                                        } catch (FirebaseAuthInvalidCredentialsException e){
                                            erroExcecao = "Favor digitar um email válido";
                                        } catch (FirebaseAuthUserCollisionException e){
                                            erroExcecao = "O email já foi cadastro";
                                        } catch (Exception e){
                                            erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(AutenticacaoActivity.this, "Erro: "+erroExcecao, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }else {
                            //no caso de login

                            autenticacao.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        //no caso de login com sucesso
                                        Toast.makeText(AutenticacaoActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();

                                        irTelaPrincipal();

                                    } else {
                                        //no caso de login sem sucesso
                                        Toast.makeText(AutenticacaoActivity.this, "Erro ao fazer login: "+task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
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
            irTelaPrincipal();
        }

    }

    private void irTelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void inicializaComponentes(){
        campoEmail=findViewById(R.id.editTextEmail);
        campoSenha=findViewById(R.id.editTextSenha);
        botaoEntrar=findViewById(R.id.buttonEntra);
        tipoAcesso=findViewById(R.id.switchEntrar);
    }
}
