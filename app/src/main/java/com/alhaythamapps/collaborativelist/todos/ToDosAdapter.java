package com.alhaythamapps.collaborativelist.todos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author Alhaytham Alfeel on 19/10/2016.
 */

public class ToDosAdapter extends ArrayAdapter<ToDo> {
    public ToDosAdapter(Context context, List<ToDo> toDos) {
        super(context, android.R.layout.simple_list_item_2, toDos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            holder = new ViewHolder();

            holder.tvName = (TextView) convertView.findViewById(android.R.id.text1);
            holder.tvEmail = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ToDo item = getItem(position);

        holder.tvName.setText(item.getName());
        holder.tvEmail.setText(item.getEmail());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvEmail;
    }
}
