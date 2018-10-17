package ru.marinakristerson.myfirstapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView mLogin;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    private ArrayAdapter<String> mLoginedUsersAdapter;


    public static AuthFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if (isEmailValid() && isPasswordValid()) {
                User user = mSharedPreferencesHelper.login(
                        mLogin.getText().toString(),
                        mPassword.getText().toString());
                if (user != null) {
                    Intent startProfileIntent =
                            new Intent(getActivity(), ProfileActivity.class);
                    startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
                    startActivity(startProfileIntent);
                    getActivity().finish();
                } else {
                    showMessage(R.string.login_error);
                }
            } else {
                showMessage(R.string.input_error);
            }

            //Проверка email и пароль
            for (User user : mSharedPreferencesHelper.getUsers()) {
                if (user.getLogin().equalsIgnoreCase(mLogin.getText().toString())
                        && user.getPassword().equals(mPassword.getText().toString())) {
                    break;
                }
            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
                    //при нажатии на кнопку регистрации формируем BackStack
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();
        }
    };


    private View.OnFocusChangeListener mOnLoginFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
               mLogin.showDropDown();
            }
        }
    };

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mLogin.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mLogin.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //вместо setContentView из Activity используется для фрагмента
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        mLogin = v.findViewById(R.id.etLogin);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.btnEnter);
        mRegister = v.findViewById(R.id.btnRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
        //mLogin.setOnFocusChangeListener(mOnLoginFocusChangeListener);

        mLoginedUsersAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mSharedPreferencesHelper.getSuccessLogins()
        );
        mLogin.setAdapter(mLoginedUsersAdapter);

        return v;
    }
}
