package com.khayah.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khayah.app.Constant;
import com.khayah.app.R;
import com.khayah.app.models.FcmMessage;
import com.khayah.app.models.Lang;
import com.khayah.app.util.StorageDriver;
import com.khayah.app.vo.Settings;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {

	private Context ctx;
	private List<FcmMessage> list;
	private LayoutInflater mInflater;

	public NotificationAdapter(Context ctx, List<FcmMessage> list){
		this.ctx = ctx;
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public FcmMessage getItem(int arg0) {
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
        	convertView = mInflater.inflate(R.layout.list_item_notifications, null);
        	holder.txt_name = (TextView) convertView.findViewById(R.id.txt_language);
        	holder.txt_message = (TextView) convertView.findViewById(R.id.txt_message);
        	convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(getItem(arg0).getUser() != null)
			holder.txt_name.setText(getItem(arg0).getUser().getFirstName() +' '+getItem(arg0).getUser().getLastName());
		holder.txt_message.setText(getItem(arg0).getMessage());
		return convertView;
	}

	static class ViewHolder{
		TextView txt_name;
		TextView txt_message;

	}

}
