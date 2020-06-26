package com.example.ifood.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ifood.R;
import com.example.ifood.activity.helper.ConfiguracaoFirebase;
import com.example.ifood.activity.model.Empresa;
import com.example.ifood.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadatroActivity extends AppCompatActivity {

    private Button botaoCadastrar;
    private EditText campoNome, campoEmail, campoSenha;
    private Switch switchTipo;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private Empresa empresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadatro);
        getSupportActionBar().hide();

        iniciaComponentes();

        autenticacao= ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = campoNome.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();


                if (!nome.isEmpty()){
                    if (!email.isEmpty()){
                        if (!senha.isEmpty()){

                            if (!switchTipo.isChecked()){   //caso o switch estiver em Usuario
                                usuario=new Usuario();
                                usuario.setNome(nome);
                                usuario.setEmail(email);
                                usuario.setSenha(senha);

                                cadastrarUsuario();

                            }
                            else {                //caso o switch estiver em Empresa
                                empresa=new Empresa();
                                empresa.setNome(nome);
                                empresa.setEmail(email);
                                empresa.setSenha(senha);

                                cadastrarEmpresa();
                            }



                        }
                        else {
                            Toast.makeText(CadatroActivity.this, "Favor preencher a senha", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(CadatroActivity.this, "Favor preencher o email", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(CadatroActivity.this, "Favor preencher o nome", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void cadastrarUsuario(){
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { //caso o cadastro for sucedido

                    String idUsuario = task.getResult().getUser().getUid();
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    Toast.makeText(CadatroActivity.this, "Cadastro de usuario realizado com sucesso", Toast.LENGTH_LONG).show();
                }
                else{ //caso ocorra um erro no cadastro
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "Favor digitar um email válido";
                    } catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "O email já foi cadastroado";
                    } catch (Exception e){
                        erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadatroActivity.this, "Erro: "+erroExcecao, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void cadastrarEmpresa(){
        autenticacao.createUserWithEmailAndPassword(empresa.getEmail(),empresa.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { //caso o cadastro for sucedido

                    String idEmpresa = task.getResult().getUser().getUid();
                    empresa.setId(idEmpresa);
                    empresa.salvar();

                    Toast.makeText(CadatroActivity.this, "Cadastro de empresa realizado com sucesso", Toast.LENGTH_LONG).show();
                }
                else{ //caso ocorra um erro no cadastro
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "Favor digitar um email válido";
                    } catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "O email já foi cadastrado";
                    } catch (Exception e){
                        erroExcecao = "ao cadastrar empresa: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadatroActivity.this, "Erro: "+erroExcecao, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }





    private void iniciaComponentes(){
        botaoCadastrar=findViewById(R.id.buttonCadastrar);
        campoNome=findViewById(R.id.editTextNome);
        campoEmail=findViewById(R.id.editTextEmail);
        campoSenha=findViewById(R.id.editTextSenha);
        switchTipo=findViewById(R.id.switchTipo);
    }
}