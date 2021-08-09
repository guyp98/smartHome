package com.example.realproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Integer> images;
    ArrayList<String> progName;
    ArrayList<String> programDesc;


    public ProgramAdapter(Context context, ArrayList<String> progName, ArrayList<Integer> images, ArrayList<String> programDesc) {
        super(context, R.layout.listview_items,R.id.textView1, progName);
        this.context = context;
        this.images=images;
        this.progName=progName;
        this.programDesc = programDesc;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View singleItem = convertView;
        ProgramViewHolder holder =null;
        if(singleItem==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.listview_items,parent,false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        }
        else
            holder = (ProgramViewHolder) singleItem.getTag();

        holder.itemImage.setImageResource(images.get(position));
        holder.progTitle.setText(progName.get(position));
        holder.progDesc.setText(programDesc.get(position));
        return singleItem;
    }
}
