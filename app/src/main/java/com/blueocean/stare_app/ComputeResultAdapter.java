package com.blueocean.stare_app;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BlueOcean_hua
 * Date 2017/6/11
 * Nicename 蓝色海洋
 * Desc 分享犹如大海，互联你我他
 */

public class ComputeResultAdapter extends BaseAdapter {

    private Context mContext;
    private List<PersonBo> dataList;

    public ComputeResultAdapter(Context context, List<PersonBo> personList) {
        mContext = context;
        dataList = personList;
    }

    public List<PersonBo> getAdapterDate(){
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList.size() == 0 ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ResultHolder holder;
        if (convertView == null) {
            holder = new ResultHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout
                    .computer_result_item_layout, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvScore = (TextView) convertView.findViewById(R.id.tv_total_score);
            holder.addScore = (EditText) convertView.findViewById(R.id.et_add_score);
            convertView.setTag(holder);
        } else {
            holder = (ResultHolder) convertView.getTag();
        }
        final PersonBo personBo = dataList.get(position);
        holder.tvName.setText(personBo.getName());
        holder.tvScore.setText(personBo.getScore() + "");

        //切记要初始化待编辑的得分
        personBo.setEditScore("0");
        holder.addScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)) {
                    personBo.setEditScore(s.toString().trim());
                }else {
                    personBo.setEditScore("0");
                }
            }
        });

        return convertView;
    }

    private static class ResultHolder {
        TextView tvName, tvScore;
        EditText addScore;
    }
}
