package ru.marinakristerson.myfirstapplication;

import android.support.v4.app.Fragment;


public class AuthActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        //Передаем инстанс фрагмента, который должен показываться на экране
        return AuthFragment.newInstance();
    }
}
