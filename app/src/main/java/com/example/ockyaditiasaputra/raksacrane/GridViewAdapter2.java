package com.example.ockyaditiasaputra.raksacrane;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ocky Aditia Saputra on 01/03/2016.
 */

public class GridViewAdapter2 extends ArrayAdapter<ImageItem2> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem2> data = new ArrayList<ImageItem2>();

    public GridViewAdapter2(Context context, int layoutResourceId, ArrayList<ImageItem2> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.id = (TextView) row.findViewById(R.id.id);
            holder.statusP = (TextView) row.findViewById(R.id.statusP);
            holder.changeBg = (LinearLayout) row.findViewById(R.id.changeBg);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem2 item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        holder.id.setText(item.getId());
        holder.statusP.setText(item.getStatusP());

        if (item.getStatusP().equals("Success")) {
            holder.changeBg.setBackground(row.getResources().getDrawable(R.drawable.grid_color_selector2));
        }

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        TextView id;
        TextView statusP;
        ImageView image;
        LinearLayout changeBg;
    }
}
