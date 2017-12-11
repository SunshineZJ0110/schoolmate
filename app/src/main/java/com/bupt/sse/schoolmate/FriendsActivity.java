package com.bupt.sse.schoolmate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bupt.sse.schoolmate.http.httpUtils;
import com.bupt.sse.schoolmate.json.jsonTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {
    private Button notice,addFriends;
    private ListView friendsList;
    private MyAdapter adapter;
    private String id;
    private ProgressDialog dialog;
    private List<Map<String,String>> total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Intent hostIntent=getIntent();
        id=hostIntent.getStringExtra("id");//当前用户id，从主界面传过来的

        notice=(Button) this.findViewById(R.id.notice);
        addFriends=(Button) this.findViewById(R.id.addFriends);
        friendsList=(ListView) this.findViewById(R.id.friends_list);
        dialog=new ProgressDialog(FriendsActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("loading......");

        adapter=new MyAdapter();
        new MyTask().execute(Path.FriendsListPath);

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:跳到搜索添加好友界面，该界面填写验证消息提交并返回此界面
                //TODO:该界面调用根据昵称搜索好友接口和提交验证信息接口
                Intent addFriendIntent=new Intent(FriendsActivity.this,searchAddFriends.class);
                addFriendIntent.putExtra("id",id);//传入当前用户ID，提交验证信息所需参数
                startActivity(addFriendIntent);

            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:跳到好友请求界面，添加好友后，添加按钮变不可点，返回刷新好友列表
                //TODO：该界面调用请求列表接口和添加好友接口
            }
        });


        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击item事件
                Map<String,String> item=total.get(i);
                String FriendID=item.get("id");
                Intent intent = new Intent(FriendsActivity.this, FriendDetail.class);
                intent.putExtra("friendId", FriendID);
                intent.putExtra("id",id);
                startActivityForResult(intent,1000);
                //startActivity(intent);
                //Toast.makeText(FriendsActivity.this,"点击item:"+i,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&&resultCode==1001){
            String refresh=data.getStringExtra("refresh");
            if(refresh.equals("true")){
                new MyTask().execute(Path.FriendsListPath);
            }
        }

    }

    class MyTask extends AsyncTask<String,Void,List<Map<String,String>>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<Map<String, String>> doInBackground(String... strings) {
            Map<String,String> data=new HashMap<String,String>();
            data.put("id",id);
            String resultString=httpUtils.sendPost(strings[0],data);
            List<Map<String, String>> result= jsonTools.parseFriendsList(resultString);
            return result;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            total=result;
            adapter.setData(result);
            friendsList.setAdapter(adapter);
        }
    }


    class MyAdapter extends BaseAdapter {

        List<Map<String,String>> list;

        public void setData(List<Map<String,String>> list){
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=null;
            if(convertView==null){
                view= LayoutInflater.from(FriendsActivity.this).inflate(R.layout.frienditem,null);
            }else{
                view=convertView;
            }
            TextView nick=(TextView) view.findViewById(R.id.nick);
            TextView name=(TextView) view.findViewById(R.id.name);
            TextView city=(TextView) view.findViewById(R.id.city);

            Map<String,String> item=list.get(position);
            nick.setText(item.get("username"));
            name.setText(item.get("name"));
            city.setText(item.get("city"));


            return view;
        }
    }

}
