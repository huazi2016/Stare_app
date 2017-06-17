package com.blueocean.stare_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BlueOcean_hua
 * Date 2017/6/10
 * Nicename 蓝色海洋
 * Desc 分享犹如大海，互联你我他
 */

public class MainAdapter extends BaseAdapter {

    private Context mContext;
    private List<PersonBo> DataList;
    int[] headArr = {R.drawable.head01, R.drawable.head02, R.drawable.head03, R.drawable.head04,
            R.drawable.head05, R.drawable.head06, R.drawable.head07, R.drawable.head08,
            R.drawable.head09, R.drawable.head10, R.drawable.head11, R.drawable.head12};
    int headId = 0;

    public MainAdapter(Context context, List<PersonBo> testList) {
        mContext = context;
        DataList = testList;
    }

    public String getPersonName() {
        if (DataList != null && DataList.size() > 0) {
            PersonBo personBo = DataList.get(0);
            if (personBo.getName() != null) {
                return personBo.getName();
            }
        }
        return null;
    }

    public void setAdapterData(List<PersonBo> DataList) {
        this.DataList = DataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return DataList.size() == 0 ? 0 : DataList.size();
    }

    @Override
    public Object getItem(int position) {
        return DataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainHolder holder;
        if (convertView == null) {
            holder = new MainHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_item_layout, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvScore = (TextView) convertView.findViewById(R.id.tv_total_score);
            holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(holder);
        } else {
            holder = (MainHolder) convertView.getTag();
        }
        PersonBo personBo = DataList.get(position);
        holder.tvName.setText(personBo.getName());
        holder.tvScore.setText(personBo.getScore() + "");
        holder.ivHead.setImageResource(headArr[personBo.getHeadId()]);

        return convertView;
    }

    private static class MainHolder {
        TextView tvName, tvScore;
        ImageView ivHead;
    }
}
