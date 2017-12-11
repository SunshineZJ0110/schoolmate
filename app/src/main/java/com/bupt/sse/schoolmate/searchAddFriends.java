package com.bupt.sse.schoolmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sai on 2017/12/11.
 */

public class searchAddFriends extends AppCompatActivity {
    private Button back,search;
    private EditText searchContent;
    private ListView searchList;
    private ProgressDialog dialog;
    private String id;//当前用户ID
    private String content;//搜索内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_add_friends);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");//当前用户ID

        back=(Button)this.findViewById(R.id.search_back);
        search=(Button)this.findViewById(R.id.search);
        searchContent=(EditText)this.findViewById(R.id.search_content);
        searchList=(ListView)this.findViewById(R.id.searchList);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:返回好友列表页面，不刷新列表
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content=searchContent.getText().toString();
                if(content.equals("")){
                    Toast.makeText(searchAddFriends.this,"请填写搜索内容",Toast.LENGTH_SHORT).show();
                }else{
                    //TODO:调用昵称搜索接口，返回搜索列表
                }

            }
        });

    }

    class searchTask extends AsyncTask<String,Void,List<Map<String,String>>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<Map<String, String>> doInBackground(String... strings) {
            String content=searchContent.getText().toString();
            Map<String,String> data=new HashMap<String,String>();
            data.put("content",content);
            return null;
        }
    }

}
