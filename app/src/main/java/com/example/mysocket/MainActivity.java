package com.example.mysocket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    Socket socket;
    FloatingActionButton fab;
    EditText input;
    RecyclerView rv;
    String inputText = "Myyyyyyy";

    ArrayList<ChatMessagePojo> lstSender;
    MyAdapter adapter;
    //ArrayList<ChatMessagePojo> lstReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            socket = IO.socket(getString(R.string.BASE_URL_NEW));
            if(!socket.connected())
            {
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {

                        Log.i("My_inside = ","socket onConnect");
                    }
                });
                socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    public void call(Object... args) {

                        Log.i("My inside = ","socket onDisConnect");
                    }
                });
            }
        }catch (URISyntaxException e) {
            e.printStackTrace();
            Log.i("My Error = ","in meanwhile connecting socket "+e);
        }
        socket.connect();


        rv = (RecyclerView) findViewById(R.id.rv);
        input = (EditText)findViewById(R.id.input);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        lstSender = new ArrayList<>();
        adapter = new MyAdapter(lstSender, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);






        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputText = input.getText().toString();

                messageSentViaSocket(inputText);
                Toast.makeText(MainActivity.this, "Message sent = "+inputText, Toast.LENGTH_SHORT).show();
                input.setText("");
            }
        });
        socket.on("messagereceived", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(args!=null)
                {
                    Log.i("My message = ",args[0]+"");
                    ChatMessagePojo data =new ChatMessagePojo() ; //initialize the constructor
                    Gson gson = new Gson();
                    //String json = gson.toJson(data);
                            /*lstSender.add(args[0].toString());

                            lstSender.add(json.toString());*/
                    //lstSender.clear();
                    lstSender.add(gson.fromJson(args[0].toString(), ChatMessagePojo.class));
                    Log.i("My lstSender = ",lstSender+"");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    //Log.i("My message = ","received");
                }
            }
        });

    }

    public void messageSentViaSocket(String inputText)
    {
        if(socket.connected())
        {
            JSONObject join = new JSONObject();
            JSONObject sender = new JSONObject();
            try {

                /*socket.emit("chat_message",inputText);
                socket.on("chat_message", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        if(args!=null)
                        {
                            Log.i("My chat_message = ",args[0]+"");
                        }
                    }
                });*/
                join.put("_id", "11111111111111111111");
                join.put("name", "sanket");

                sender.put("sender",join);
                sender.put("message",inputText);
                socket.emit("join",join);
                //socket.emit("message",jo2);
                socket.emit("message",sender);

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("My inside = ","catch "+e);
            }
        }
    }



    @Override
    protected void onDestroy() {
        if(socket!=null && socket.connected())
        {
            socket.disconnect();
        }
        super.onDestroy();
    }

}
