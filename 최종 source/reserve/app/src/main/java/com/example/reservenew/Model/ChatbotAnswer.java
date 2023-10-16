package com.example.reservenew.Model;

import com.google.gson.annotations.SerializedName;

public class ChatbotAnswer {
    @SerializedName("answer")
    private String answer;

    @SerializedName("testing")
    private String testing;

    public String getAnswer(){
        return answer;
    }

    public String getTesting(){
        return testing;
    }
}
