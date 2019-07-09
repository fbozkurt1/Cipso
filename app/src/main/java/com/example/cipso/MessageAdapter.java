package com.example.cipso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

    private ArrayList<Message> messageList;
    private Context context;
    private String uid;

    public MessageAdapter(Context context, ArrayList<Message> messageList, String uid) {
        this.context = context;
        this.messageList = messageList;
        this.uid=uid;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder messageHolder;
        Message message = (Message) getItem(position);

        if (message.getUid().equals(this.uid)) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_message, parent, false);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.friends_message, parent, false);
        }
        messageHolder = new MessageHolder();
        messageHolder.messageBody = convertView.findViewById(R.id.message_body);
        messageHolder.messageTime = convertView.findViewById(R.id.message_time);
        convertView.setTag(messageHolder);

        messageHolder.messageBody.setText(message.getTextMessage());
        messageHolder.messageTime.setText(message.getDate());
        return convertView;
    }

    public class MessageHolder {
        public TextView messageBody;
        public TextView messageTime;
    }
}
