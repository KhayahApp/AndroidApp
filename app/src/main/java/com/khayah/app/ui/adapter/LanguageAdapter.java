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
import com.khayah.app.models.Lang;
import com.khayah.app.util.StorageDriver;
import com.khayah.app.vo.Settings;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

public class LanguageAdapter extends BaseAdapter {

	private Context ctx;
	private List<Lang> list;
	private LayoutInflater mInflater;

	public LanguageAdapter(Context ctx, List<Lang> list){
		this.ctx = ctx;
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Lang getItem(int arg0) {
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
        	convertView = mInflater.inflate(R.layout.list_item_language, null);
        	holder.txt_name = (TextView) convertView.findViewById(R.id.txt_language);
        	holder.img_lang = (ImageView) convertView.findViewById(R.id.img_language);
        	convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txt_name.setText(getItem(arg0).getName());
		holder.img_lang.setImageResource(getItem(arg0).getImage());
		Settings settings = StorageDriver.getInstance().selectFrom(Constant.settingsKey);
		if (settings != null) {
			if(settings.getLocale().equals(getItem(arg0).getShortName())){
				holder.txt_name.setCompoundDrawablesWithIntrinsicBounds(null,null,getIconicDrawable(CommunityMaterial.Icon.cmd_check.toString(), R.color.colorPrimary,18),null);
			}else{
				holder.txt_name.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
			}
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
		TextView txt_name;
		ImageView img_lang;

	}

}
