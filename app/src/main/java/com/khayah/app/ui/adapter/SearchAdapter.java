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
import com.khayah.app.util.CircleTransform;
import com.khayah.app.util.StorageDriver;
import com.khayah.app.vo.Building;
import com.khayah.app.vo.Settings;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;


import java.util.List;

public class SearchAdapter extends BaseAdapter {

	private Context ctx;
	private List<Building> list;
	private LayoutInflater mInflater;

	public SearchAdapter(Context ctx, List<Building> list){
		this.ctx = ctx;
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Building getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
        	convertView = mInflater.inflate(R.layout.list_item_search, null);
        	holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        	holder.txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
        	holder.img_building = (ImageView) convertView.findViewById(R.id.img_building);
        	convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(getItem(position).getImages() != null) {
			String[] images = getItem(position).getImages().split(",");
			if(images.length > 0) Picasso.with(ctx).load(images[0]+"?w=80")
					.transform(new CircleTransform()).into(holder.img_building);
		}
		if(this.getSettings().getLocale().equals("mm")) {
			holder.txt_name.setText(getItem(position).getNameMm() != null ?
					getItem(position).getNameMm() : getItem(position).getName());
			holder.txt_desc.setText(getItem(position).getDescriptionMm() != null ?
					getItem(position).getDescriptionMm() : getItem(position).getDescription());
		}else{
			holder.txt_name.setText(getItem(position).getName());
			holder.txt_desc.setText(getItem(position).getDescription());
		}
		return convertView;
	}

	public IconicsDrawable getIconicDrawable(String icon, int color, int size){
		return new IconicsDrawable(ctx)
				.icon(icon)
				.color(ctx.getResources().getColor(color))
				.sizeDp(size);
	}

	static class ViewHolder{
		ImageView img_building;
		TextView txt_name;
		TextView txt_desc;
	}

	public Settings getSettings(){
		Settings settings = StorageDriver.getInstance().selectFrom(Constant.settings);
		if(settings != null){
			return settings;
		}
		Settings setting = new Settings();
		setting.save();
		return setting;
	}

}
