package io.intrepid.androidlogin.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.androidlogin.R;
import io.reactivex.Observable;

public class ExampleLoginActivity extends AppCompatActivity implements ExampleLoginContract.View {

    @BindView(R.id.username_field)
    EditText usernameField;
    @BindView(R.id.password_field)
    EditText passwordField;
    @BindView(R.id.login_button)
    Button loginButton;

    private ExampleLoginContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        ButterKnife.bind(this);
        presenter = new ExampleLoginPresenter(this);
        loginButton.setEnabled(false);
    }

    @NonNull
    @Override
    public Observable<TextViewTextChangeEvent> getUsernameTextFieldObservable() {
        return RxTextView.textChangeEvents(usernameField);
    }

    @NonNull
    @Override
    public Observable<TextViewTextChangeEvent> getPasswordTextFieldObservable() {
        return RxTextView.textChangeEvents(passwordField);
    }

    @Override
    public Observable<Object> getLoginButtonObservable() {
        return RxView.clicks(loginButton);
    }

    @Override
    public void disableLoginButton() {
        loginButton.setEnabled(false);
    }

    @Override
    public void enableLoginButton() {
        loginButton.setEnabled(true);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
