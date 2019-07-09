package com.example.cipso;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contacts> contactsArrayList;
    public ContactsAdapter(Context context, ArrayList<Contacts> contactsArrayList) {
        this.context = context;
        this.contactsArrayList = contactsArrayList;
    }

    @Override
    public int getCount() {
        return contactsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // inflate the layout for each list row
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_people,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // get current item to be displayed
        Contacts currentContacts = (Contacts) getItem(position);

        // set background color of even rows to LightGray
       /**if(position%2==0) {
           viewHolder.linearLayoutPeople.setBackgroundColor(ContextCompat.getColor(context,R.color.colorLighterGray));
       }*/

        // Set the text for name, surname and phone number
        String name_Surname = currentContacts.getName() + " "+ currentContacts.getSurname();
        String phoneNumber = currentContacts.getPhoneNum();
        viewHolder.textViewNameSurname.setText(name_Surname);
        viewHolder.textViewPhoneNumber.setText(phoneNumber);
        return convertView;
    }

    private class ViewHolder{
        /** Get android items id*/
        LinearLayout linearLayoutPeople;
        TextView textViewNameSurname;
        TextView textViewPhoneNumber;

        public  ViewHolder(View view){
            // get the TextView for Contact name, Contact Surname and Contact Phone
            textViewNameSurname = view.findViewById(R.id.textView_HomeNameSurname);
            textViewPhoneNumber = view.findViewById(R.id.textView_HomePhoneNumber);
            linearLayoutPeople = view.findViewById(R.id.linearLayout_rowPeople);
        }
    }

}
