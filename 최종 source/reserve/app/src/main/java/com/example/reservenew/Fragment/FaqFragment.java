package com.example.reservenew.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservenew.Adapter.ChatAdapter;
import com.example.reservenew.Model.ChatbotAnswer;
import com.example.reservenew.Model.Message;
import com.example.reservenew.R;
import com.example.reservenew.Utils.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class FaqFragment extends Fragment {
    RecyclerView chatView;
    ChatAdapter chatAdapter;
    List<Message> messageList = new ArrayList<>();
    EditText editMessage;
    Button btnSend;
    String chatbotResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_faq, container, false);

        chatView = (RecyclerView) rootView.findViewById(R.id.chatView);
        editMessage = (EditText) rootView.findViewById(R.id.et_faq_chat_msg_box);
        btnSend = (Button) rootView.findViewById(R.id.btn_faq_chat_send);

        chatAdapter = new ChatAdapter(messageList, getActivity());
        chatView.setAdapter(chatAdapter);

        // Retrofit 불러오기
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://16.171.177.54").addConverterFactory(GsonConverterFactory.create()).build();
        ApiService chatbotApi = retrofit.create(ApiService.class);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                if (!message.isEmpty()) {
                    messageList.add(new Message(message, false));
                    editMessage.setText("");
                    
                    // chatbot 서버에 api 요청 보내기
                    Call<ChatbotAnswer> call = chatbotApi.getStringWithAppendedText(message);
                    call.enqueue(new Callback<ChatbotAnswer>() {
                        @Override
                        public void onResponse(Call<ChatbotAnswer> call, Response<ChatbotAnswer> response) {
                            if (response.isSuccessful()) {
                                // 응답을 chatbotResponse 에 저장
                                ChatbotAnswer answer = response.body();
                                chatbotResponse = answer.getAnswer();
                                callback(chatbotResponse);
                            } else {
                                chatbotResponse = "챗봇 서버에서 응답을 받아오지 못했습니다.";
                            }
                        }

                        @Override
                        public void onFailure(Call<ChatbotAnswer> call, Throwable t) {
                            Log.d("요청 실패", "서버에 요청 실패");
                        }
                    });
                    Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(getActivity(), "문의 사항을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    // response 를 받아 데이터에 추가하고 화면 변경을 알림
    public void callback(String response){
        if(!response.isEmpty()){
            messageList.add(new Message(response, true));
            chatAdapter.notifyDataSetChanged();
            Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
        }else {
            Toast.makeText(getActivity(), "답변 전송에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }


}
