package com.khayah.app.ui.adapter;

import java.util.List;

import com.khayah.app.R;
import com.khayah.app.models.Type;
import com.mikepenz.iconics.view.IconicsImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AbSpinnerAdapter extends BaseAdapter{
	
	private Context ctx;
	private List<Type> item;

    public AbSpinnerAdapter(Context ctx, List<Type> list) {
    	this.ctx = ctx;
       	this.item = list;
    }

	public int getCount() {
		// TODO Auto-generated method stub
		return item.size();
	}

	public Type getItem(int position) {
		// TODO Auto-generated method stub
		return item.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = (TextView) convertView;
        if (tv == null) {
            tv = (TextView) LayoutInflater.from(ctx).inflate(R.layout.list_item_spinner_selected, parent, false);
        }

        tv.setText(getItem(position).getTitle());

        return tv;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
        	convertView = LayoutInflater.from(ctx).inflate(R.layout.spinner_ab_dropdown, null);
        	holder.txt_menu = (TextView) convertView.findViewById(R.id.sp_drop_down_item);
        	        	
        	convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txt_menu.setText(getItem(position).getTitle());
		
		return convertView;
	}
	
	static class ViewHolder {
		IconicsImageView img_icon;
		TextView txt_menu;
		
	}

}