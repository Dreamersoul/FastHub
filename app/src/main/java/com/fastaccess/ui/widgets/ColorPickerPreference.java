package com.fastaccess.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fastaccess.R;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.helper.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * Created by Hamad on 6/11/17.
 */

public class ColorPickerPreference extends Preference implements ColorPicker.OnChooseColorListener {

    private ColorPicker colorPicker;

    public ColorPickerPreference(Context context) {
        super(context);
        init();
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setWidgetLayoutResource(R.layout.preference_widget_color);
    }

    @Override
    protected void onClick() {
        super.onClick();

        int selected_color = getSelected_color();
        String title = String.format("Accent Color: (Selected: %s)", getSelected_color_name());
        colorPicker = new ColorPicker(getContext());
        colorPicker.setRoundColorButton(true);
        colorPicker.setColors(R.array.theme_colors_hex);
        colorPicker.setDefaultColorButton(selected_color);
        colorPicker.setTitle(title);
        colorPicker.getPositiveButton().setTextColor(ViewHelper.getAccentColor(getContext()));
        colorPicker.getNegativeButton().setTextColor(ViewHelper.getPrimaryTextColor(getContext()));
        colorPicker.setOnChooseColorListener(this);
        colorPicker.show();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final Button colorButton = (Button) holder.findViewById(R.id.color);
        colorButton.setBackgroundResource(R.drawable.circle_shape);
        colorButton.getBackground().setColorFilter(getSelected_color(), PorterDuff.Mode.SRC_IN);
    }

    private int getSelected_color() {
        TypedArray colorTypedArray = getContext().getResources().obtainTypedArray(R.array.theme_colors_hex);
        String[] colorNames = getContext().getResources().getStringArray(R.array.theme_colors);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < colorTypedArray.length(); i++) {
            colors.add(colorTypedArray.getColor(i, 0));
        }
        colorTypedArray.recycle();
        HashMap<Integer, Integer> preferenceValueToColor = new HashMap<>();

        for(int i=0; i<colorNames.length; i++){
            preferenceValueToColor.put(PrefGetter.getThemeColor(getContext().getResources(), colorNames[i]), colors.get(i));
        }
        return preferenceValueToColor.get(PrefGetter.getThemeColor(getContext()));
    }

    @Override
    public void onChooseColor(int position, int color) {
        // put code
        //getOnPreferenceChangeListener().onPreferenceChange(ColorPickerPreference.this, color);
        persistString(getContext().getResources().getStringArray(R.array.theme_colors)[position]);
        getOnPreferenceChangeListener().onPreferenceChange(this, getContext().getResources().getStringArray(R.array.theme_colors)[position]);
    }

    @Override
    public void onCancel() {

    }

    public String getSelected_color_name() {
        String[] colorNames = getContext().getResources().getStringArray(R.array.theme_colors);
        return colorNames[PrefGetter.getThemeColor(getContext()) - 1];
    }
}