package com.khayah.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.khayah.app.R;
import com.khayah.app.models.FcmMessage;
import com.khayah.app.models.UserGroup;
import com.khayah.app.util.TimeAgo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TrustedListAdapter extends BaseAdapter {

	private Context ctx;
	private List<UserGroup> list;
	private LayoutInflater mInflater;

	public TrustedListAdapter(Context ctx, List<UserGroup> list){
		this.ctx = ctx;
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public UserGroup getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("deprecation")
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
        	convertView = mInflater.inflate(R.layout.list_item_trusted, null);
			holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
        	holder.txt_phone = (TextView) convertView.findViewById(R.id.txt_phone);
        	convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_name.setText(getItem(arg0).getName());
		holder.txt_phone.setText("+"+getItem(arg0).getPhone());
		return convertView;
	}

	static class ViewHolder{
		TextView txt_name;
		TextView txt_date;
		TextView txt_phone;

	}

}
