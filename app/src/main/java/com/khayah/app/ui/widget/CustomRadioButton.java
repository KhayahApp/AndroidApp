package com.khayah.app.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.khayah.app.Constant;
import com.khayah.app.util.FontConverter;
import com.khayah.app.util.StorageDriver;
import com.khayah.app.vo.Settings;


@SuppressLint("AppCompatCustomView")
public class CustomRadioButton extends RadioButton {
	public CustomRadioButton(Context context){
		super(context);
		if(!isInEditMode()){
			Settings settings = StorageDriver.getInstance().selectFrom(Constant.settingsKey);
			if(settings != null){
				if( settings.getFontStyle().equals("zawgyi")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyi.ttf"));
				}else if( settings.getFontStyle().equals("myanmar3")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/myanmar3.ttf"));
				}
			}
		}

	}
	public CustomRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()){
			Settings settings = StorageDriver.getInstance().selectFrom(Constant.settingsKey);
			if(settings != null){
				if( settings.getFontStyle().equals("zawgyi")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyi.ttf"));
				}else if( settings.getFontStyle().equals("myanmar3")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/myanmar3.ttf"));
				}
			}
		}
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		Settings settings = StorageDriver.getInstance().selectFrom(Constant.settingsKey);
		if(settings != null){
			if(settings.getFontStyle().equals("myanmar3")){
				if(text != null){
					text = FontConverter.zg12uni51(text.toString());
				}
			}
		}
		super.setText(text, type);
	}
	
	

}
