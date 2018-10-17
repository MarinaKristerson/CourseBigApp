package ru.marinakristerson.myfirstapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationFragment extends Fragment {

    private EditText mLogin;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;

    private SharedPreferencesHelper mSharedPreferencesHelper;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    //Вся логика происходит при нажатии на кнопку регистрации
    //Все пользователи хранятся в SharedPreferences - класс SharedPreferencesHelper
    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Если все данные были введены верно, то
            if (isInputValid()) {
                //добавляем юзера в SharedPreferences
                boolean isAdded = mSharedPreferencesHelper.addUser(new User(
                        mLogin.getText().toString(),
                        mPassword.getText().toString()
                ));

                if (isAdded) {
                    showMessage(R.string.login_register_success);
                    //Переход к предыдущему экрану из BackStack
                    getFragmentManager().popBackStack();
                } else {
                    showMessage(R.string.login_register_error);
                }
            } else {
                showMessage(R.string.input_error);
            }
        }
    };

    //Стандартная инициализация View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        mLogin = view.findViewById(R.id.etLogin);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegistration);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    //Проверка ввода данных (совокупность e-mail и пароля)
    private boolean isInputValid() {
        String email = mLogin.getText().toString();
        if (isEmailValid(email) && isPasswordsValid()) {
            return true;
        }
        return false;
    }

    //Проверка ввода данных (корректность ввода e-mail)
    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Проверка ввода данных (правильность ввода паролей / совпадение полей)
    private boolean isPasswordsValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

    //Показ тостов - сообщений
    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
