package com.begin.diana.inkainternship.spinner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.begin.diana.inkainternship.R;

import java.util.List;

public class AdapterSpinner extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<PilihSpinnerModel> item;

    public AdapterSpinner(Activity activity, List<PilihSpinnerModel> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_spinner, null);

        TextView pilih = (TextView) convertView.findViewById(R.id.pilihLembaga);

        PilihSpinnerModel data;
        data = item.get(position);

        pilih.setText(data.getNama());

        return convertView;
    }


}

