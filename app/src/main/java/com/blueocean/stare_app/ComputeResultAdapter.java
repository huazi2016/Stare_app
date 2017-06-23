package com.blueocean.stare_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
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
    private List<PersonBo> clickList;

    public ComputeResultAdapter(Context context, List<PersonBo> personList) {
        mContext = context;
        dataList = personList;

        clickList = new ArrayList<>();
    }

    private void setClickList(List<PersonBo> clickList){
        dataList = clickList;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ResultHolder holder;
        if (convertView == null) {
            holder = new ResultHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout
                    .computer_result_item_layout, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvScore = (TextView) convertView.findViewById(R.id.tv_total_score);
            holder.addScore = (EditText) convertView.findViewById(R.id.et_add_score);
            holder.winButton = (TextView) convertView.findViewById(R.id.tv_win_button);
            convertView.setTag(holder);
        } else {
            holder = (ResultHolder) convertView.getTag();
        }
        final PersonBo personBo = dataList.get(position);
        holder.tvName.setText(personBo.getName());
        holder.tvScore.setText(personBo.getScore() + "");

        if(personBo.isClick()){
            personBo.setEditScore("Win");
            holder.winButton.setTextColor(mContext.getResources().getColor(R.color.win_select));
            setTextviewBg(holder,R.drawable.win_selected);
            holder.addScore.setEnabled(false);
            holder.winButton.setHint("");
            holder.addScore.setText("Win");
        }else {
            personBo.setEditScore("0");
            holder.addScore.setEnabled(true);
            holder.winButton.setTextColor(mContext.getResources().getColor(R.color.win_normal));
            setTextviewBg(holder,R.drawable.win_normal);
            holder.winButton.setHint("input");
            holder.addScore.setText("");
        }

        holder.winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (PersonBo bo : dataList) {
                    bo.setClick(false);
                }

                personBo.setClick(true);
                notifyDataSetChanged();
            }
        });

        holder.addScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s)) {
                    String str = s.toString().trim();
                    if (!str.equalsIgnoreCase("Win")) {
                        personBo.setEditScore(s.toString().trim());
                    } else {
                        personBo.setEditScore("Win");
                    }
                } else {
                    personBo.setEditScore("0");
                }
            }
        });

        return convertView;
    }

    private void setTextviewBg(ResultHolder holder,int viewId) {
        Drawable dra = mContext.getResources().getDrawable(viewId);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        holder.winButton.setCompoundDrawables(dra, null, null, null);
        //holder.winButton.setPadding(0,2,0,0);
    }

    private static class ResultHolder {
        TextView tvName, tvScore, winButton;
        EditText addScore;
    }
}
