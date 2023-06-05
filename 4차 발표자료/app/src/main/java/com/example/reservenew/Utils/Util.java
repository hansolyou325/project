package com.example.reservenew.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.EditText;

import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Util {
    public static boolean etTextBlankCK(EditText editText){
        return editText.getText().toString().trim().equals("");
    }

    public static void showMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void saveSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String loadSharedPreference(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public static boolean isDateWithinRange(String date_str, String startDate_str, String endDate_str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate inputDate = LocalDate.parse(date_str, DateTimeFormatter.ISO_LOCAL_DATE);

            LocalDate startDate = LocalDate.parse(startDate_str, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate endDate = LocalDate.parse(endDate_str, DateTimeFormatter.ISO_LOCAL_DATE);

            return !inputDate.isBefore(startDate) && !inputDate.isAfter(endDate);
        }
        return false;
    }
}
