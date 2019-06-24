package com.sjk.zcmu.score.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sjk.zcmu.score.R;
import com.sjk.zcmu.score.model.About;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AboutAdapter extends ArrayAdapter<About> {
    private int resource;

    public AboutAdapter(Context context, int resource, List<About> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        About info = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.infoTitle = view.findViewById(R.id.info_title);
            viewHolder.detailInfo = view.findViewById(R.id.detail_info);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.infoTitle.setText(info.getInfoTitle());
        viewHolder.detailInfo.setText(info.getDetailInfo());
        return view;
    }

    class ViewHolder {
        TextView infoTitle, detailInfo;
    }
}
