package com.bupt.sse.schoolmate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bupt.sse.schoolmate.http.httpUtils;
import com.bupt.sse.schoolmate.json.jsonTools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sai on 2017/12/10.
 */

public class FriendDetail extends AppCompatActivity {
    private String friendID;
    private String id;//当前用户ID
    private Button back,delete,phone,message;
    private TextView nick,name,phoneNumber,sex,city,school;
    private ProgressDialog dialog;
    private ProgressDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail);
        Intent intent=getIntent();
        friendID=intent.getStringExtra("friendId");
        id=intent.getStringExtra("id");//当前用户ID

        back=(Button)this.findViewById(R.id.friend_detail_back);
        delete=(Button)this.findViewById(R.id.delete);
        phone=(Button)this.findViewById(R.id.phone);
        message=(Button)this.findViewById(R.id.message);

        nick=(TextView)this.findViewById(R.id.nick);
        name=(TextView)this.findViewById(R.id.name);
        phoneNumber=(TextView)this.findViewById(R.id.PhoneNumber);
        sex=(TextView)this.findViewById(R.id.sex);
        city=(TextView)this.findViewById(R.id.city);
        school=(TextView)this.findViewById(R.id.school);

        dialog=new ProgressDialog(FriendDetail.this);
        dialog.setTitle("提示");
        dialog.setMessage("loading......");

        dialog1=new ProgressDialog(FriendDetail.this);
        dialog1.setTitle("提示");
        dialog1.setMessage("正在删除，请稍后...");

        new DetailTask().execute(Path.FriendDetailPath);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //回传参数，是否刷新列表
                Intent backIntent=new Intent();
                backIntent.putExtra("refresh","false");
                setResult(1001,backIntent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(FriendDetail.this);
                builder.setTitle("提示框");
                builder.setMessage("确定删除好友么？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //删除好友，并返回好友列表，列表刷新
                        new deleteTask().execute(Path.FriendDeletePath);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });


    }

    class DetailTask extends AsyncTask<String,Void,Map<String,String>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Map<String, String> doInBackground(String... strings) {
            Map<String,String> data=new HashMap<String,String>();
            data.put("friendId",friendID);
            String resultString= httpUtils.sendPost(strings[0],data);
            Map<String,String> result= jsonTools.parseFriendDetail(resultString);
            return result;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            super.onPostExecute(result);
            nick.setText(result.get("username"));
            name.setText(result.get("name"));
            phoneNumber.setText(result.get("phone"));
            sex.setText(result.get("sex"));
            city.setText(result.get("city"));
            school.setText(result.get("location"));
            dialog.dismiss();
        }
    }

    class deleteTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Map<String,String> data=new HashMap<String,String>();
            data.put("id",id);
            data.put("friendId",friendID);
            String resultString=httpUtils.sendPost(strings[0],data);
            return jsonTools.parseDelete(resultString);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("false")){
                dialog1.dismiss();
                Toast.makeText(FriendDetail.this,"删除失败！",Toast.LENGTH_LONG).show();
            }else{
                //删除成功，返回并刷新好友列表
                Intent deleteIntent=new Intent();
                deleteIntent.putExtra("refresh","true");
                setResult(1001,deleteIntent);
                finish();
            }
        }
    }


}
