package ru.marinakristerson.myfirstapplication;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Хранение всех пользователей, которые используются в приложении
public class SharedPreferencesHelper {
    //Имя SharedPreferences файла
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    //Ключ для получения пользователей Gson
    public static final String USERS_KEY = "USERS_KEY";
    //TYPE ??? + Generic
    public static final Type USERS_TYPE = new TypeToken<List<User>>() {
    }.getType();

    //Сам SharedPreferences
    private SharedPreferences mSharedPreferences;
    //Gson для сохранения пользователей (строковый формат) в SharedPreferences
    private Gson mGson = new Gson();

    //Инициализация (создание, авктивация, подготовка к работе) в конструкторе
    //Внутрь передадим контекст и инициализируемся из контекста
    public SharedPreferencesHelper(Context context) {
        //1. Название самого файла 2. Режим, в котором будет работать SharedPreferences
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    //Логика добавление и сохранение пользователей в SharedPreferences
    //Метод возвращает список юзеров
    public List<User> getUsers() {
        //Для SharedPreferences нужно использовать формат Gson, т.к. нет возможности сохранять сериалайзебл или парсебл объекты
        //Но можно сохранять строки
        //1. Строка, из которой создаем список пользователей 2.TYPE ???
        //getString (1. Ключ для получения пользователей, 2. Дефолтное значение параметра)
        List<User> users = mGson.fromJson(mSharedPreferences.getString(USERS_KEY, ""), USERS_TYPE);
        //Проверка на наличие чего-либо в SharedPreferences, иначе будет ошибка
        return users == null ? new ArrayList<User>() : users;
    }

    //Добавляем пользователя в SharedPreferences. Boolean, чтоб определить был добавлен ранее или нет
    public boolean addUser(User user) {
        //Получаем список всех юзеров из SharedPreferences
        List<User> users = getUsers();

        //Проверяем наличие пользователя, которого мы хотим создать
        for (User u : users) {
            //если пользователь уже есть, то false
            if (u.getLogin().equalsIgnoreCase(user.getLogin())) {
                return false;
            }
        }
        //Пользователя нет (false не вернулся), значит добавляем в полученный список
        users.add(user);
        //И сохраняем все это в SharedPreferences 1. Ключ 2. Само значение
        //в toJson 1. наш список пользователей 2. TYPE, что все правильно перевелось
        mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
        return true;
    }

    public List<String> getSuccessLogins() {
        List<String> successLogins = new ArrayList<>();
        List<User> allUsers = getUsers();
        for (User user : allUsers) {
            if (user.hasSuccessLogin()) {
                successLogins.add(user.getLogin());
            }
        }
        return successLogins;
    }

    public User login(String login, String password) {
        List<User> users = getUsers();
        for (User u : users) {
            if (login.equalsIgnoreCase(u.getLogin())
                    && password.equals(u.getPassword())) {
                u.setHasSuccessLogin(true);
                mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
                return u;
            }
        }
        return null;
    }
}
