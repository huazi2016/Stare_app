package com.blueocean.stare_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueocean.stare_app.db.MyDataBase;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

public class ComputeResultActivity extends Activity {

    @BindView(R.id.compute_listview)
    ListView mComputeListview;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_done)
    Button mBtnDone;
    @BindView(R.id.title_container)
    LinearLayout mTitleContainer;
    @BindView(R.id.btn_container)
    LinearLayout mBtnContainer;
    @BindView(R.id.no_content_container)
    RelativeLayout mNoContentContainer;

    private List<PersonBo> mPersonList;
    private ComputeResultAdapter mResultAdapter;
    boolean isEdit = false;
    boolean isWin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉标题栏
        setContentView(R.layout.activity_compute_result);

        /*设置窗口样式activity宽高start*/
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.55);   //高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.7
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置窗口外黑暗度
        getWindow().setAttributes(p);
        /*设置窗口样式activity宽高end*/

        setFinishOnTouchOutside(false); //点击窗口阴影部分不做处理

        ButterKnife.bind(this);
        loadData();
        initView();
        initListener();
    }

    private void loadData() {
        if (getIntent() != null) {
            mPersonList = (List<PersonBo>) getIntent().getSerializableExtra("personList");

            Log.d("lhx", "size: " + mPersonList.size());
        }
    }

    private void initView() {
        if (mPersonList == null || mPersonList.size() <= 0) {
            mNoContentContainer.setVisibility(View.VISIBLE);
            mComputeListview.setVisibility(View.GONE);
            mBtnContainer.setVisibility(View.GONE);
        }else {
            mNoContentContainer.setVisibility(View.GONE);
            mComputeListview.setVisibility(View.VISIBLE);
            mBtnContainer.setVisibility(View.VISIBLE);
            mResultAdapter = new ComputeResultAdapter(this, mPersonList);
            mComputeListview.setAdapter(mResultAdapter);
            mComputeListview.setDivider(null);
        }
    }

    private void initListener() {
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowDialog("温馨提示","清空所有已输入的分数");
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PersonBo> adapterDate = mResultAdapter.getAdapterDate();
                int finalResult = 0;
                int index = 0;

                for (int i = 0; i < adapterDate.size(); i++) {
                    PersonBo personBo = adapterDate.get(i);

                    String strNum = personBo.getEditScore();
                    if(!strNum.equalsIgnoreCase("0")){
                        isEdit = true;
                    }

                    if(strNum.equalsIgnoreCase("Win")){
                        index = i;
                        isWin = true;
                        strNum = "0";
                    }

                    Log.d("lhx", "strNum: " + strNum);
                    int result = personBo.getScore() - Integer.parseInt(strNum);
                    finalResult += Integer.parseInt(strNum);
                    Log.d("lhx", "result: " + result);

                    personBo.setScore(result);

                    MyDataBase.getInstances(ComputeResultActivity.this).updatePersonInfo(personBo
                            .getName(), personBo.getScore());
                }

                int sum = adapterDate.get(index).getScore() + Math.abs(finalResult);
                adapterDate.get(index).setScore(sum);
                MyDataBase.getInstances(ComputeResultActivity.this).updatePersonInfo(adapterDate.get(index)
                        .getName(), adapterDate.get(index).getScore());

                if(isEdit && isWin){
                    Log.d("lhx", "adapter size: " + adapterDate.size());
                    Intent intent = new Intent(ComputeResultActivity.this, MainActivity.class);
                    intent.putExtra("resultList", (Serializable) adapterDate);
                    setResult(500, intent);

                    finish();
                }else {
                    Toast.makeText(ComputeResultActivity.this, "全部为空值或未选胜利一方", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setShowDialog(String title, String content) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle(title);
        materialDialog.setMessage(content);
        materialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                materialDialog.dismiss();
            }
        });
        materialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
        materialDialog.setCanceledOnTouchOutside(true);
    }
}
