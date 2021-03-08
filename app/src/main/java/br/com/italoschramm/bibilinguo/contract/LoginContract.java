package br.com.italoschramm.bibilinguo.contract;

import android.provider.BaseColumns;

public final class LoginContract {

    private LoginContract() {}

    /* Inner class that defines the table contents */
    public static class LoginEntry implements BaseColumns {
        public static final String TABLE_NAME = "LOGIN";
        public static final String COLUMN_NAME_USERNAME = "USERNAME";
        public static final String COLUMN_NAME_PASSWORD = "PASSWORD";
    }


}
