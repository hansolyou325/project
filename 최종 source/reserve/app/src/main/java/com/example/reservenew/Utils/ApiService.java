package com.example.reservenew.Utils;

import com.example.reservenew.Model.ChatbotAnswer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import okhttp3.ResponseBody;

public interface ApiService {
    @GET("/chatbot") // 서버의 엔드포인트 경로를 입력하세요
    Call<ChatbotAnswer> getStringWithAppendedText(@Query("input") String input);
}