package ru.marinakristerson.myfirstapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

//Базовый класс (Activity) для одного фрагмента - используется когда нужно использовать 1 фрагмент в качестве основного
public abstract class SingleFragmentActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Устанавливает содержимое Activity из layout-файла
        setContentView(R.layout.ac_single_fragment);

        //Проверка состояния фрагмента. Существовал ли он уже и есть ли по нему какие-то сохраненнеы состояния
        //По умолчанию система использует состояние экземпляра Bundle для сохранения информации о каждом объекте View
        if (savedInstanceState == null) {
            //Получаем доступ к менеджеру фрагментов
            FragmentManager fragmentManager = getSupportFragmentManager();

            //Старт транзакции по запуску фрагмента
            fragmentManager.beginTransaction()
                    //1. куда добавляем фрагмент, 2. (название фрагмента) т.к. это базовый класс, у нас нет конкретного фрагмента. Создаем абстрактный метод
                    .replace(R.id.fragmentContainer, getFragment())
                    .commit();
        }
    }

    protected abstract Fragment getFragment();

    //Переопределяем метод при нажатии на кнопку "Назад"
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        } else {
            fragmentManager.popBackStack();
        }
    }
}
