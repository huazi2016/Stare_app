package com.blueocean.stare_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.blueocean.stare_app.db.MyDataBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.listview)
    SwipeMenuListView mListview;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.activity_main)
    LinearLayout mActivityMain;
    @BindView(R.id.tv_best_score)
    TextView mTvBestScore;
    @BindView(R.id.listview_container)
    RelativeLayout mListviewContainer;
    @BindView(R.id.no_content_container)
    RelativeLayout mNoContentContainer;
    @BindView(R.id.iv_left_icon)
    ImageView mIvLeftIcon;
    @BindView(R.id.iv_right_icon)
    ImageView mIvRightIcon;
    @BindView(R.id.tv_win)
    TextView mTvWin;
    @BindView(R.id.tv_transport)
    TextView mTvTransport;
    @BindView(R.id.iv_history_icon)
    ImageView mIvHistoryIcon;

    private MainAdapter mMainAdapter;
    private List<PersonBo> mPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadData();
        initView();
        initlistener();
    }

    private void loadData() {
        mPersonList = new ArrayList<>();
        mPersonList = MyDataBase.getInstances(this).queryPersonInfo();
        setNoContextView();
        compareSrore();
        setWinAndTransportScore(mPersonList);
        mMainAdapter = new MainAdapter(this, mPersonList);
        mListview.setAdapter(mMainAdapter);
    }

    private void setNoContextView() {
        if (mPersonList == null || mPersonList.size() <= 0) {
            mNoContentContainer.setVisibility(View.VISIBLE);
            mListviewContainer.setVisibility(View.GONE);
        } else {
            mNoContentContainer.setVisibility(View.GONE);
            mListviewContainer.setVisibility(View.VISIBLE);
        }
    }

    private void compareSrore() {
        if (mPersonList != null && mPersonList.size() > 0) {

            Collections.sort(mPersonList, new Comparator<PersonBo>() {
                @Override
                public int compare(PersonBo o1, PersonBo o2) {
                    if (o1.getScore() < o2.getScore()) {
                        return 1;
                    }
                    return -1;
                }
            });
        }
    }

    private void initView() {
        mTvTitle.setText("记分板");
        setBsetScore();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                //SwipeMenuItem openItem = new SwipeMenuItem(
                //        getApplicationContext());
                //openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                //        0xCE)));
                //openItem.setWidth(SizeUtils.Dp2Px(MainActivity.this,90));
                //openItem.setTitle("Open");
                //openItem.setTitleSize(18);
                //openItem.setTitleColor(Color.WHITE);
                //menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(SizeUtils.Dp2Px(MainActivity.this, 90));
                //deleteItem.setIcon(R.drawable.head);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        mListview.setMenuCreator(creator);
    }

    /**
     * 设置手气最佳
     */
    private void setBsetScore() {
        if (mMainAdapter != null) {
            String personName = mMainAdapter.getPersonName();
            if (personName != null) {
                mTvBestScore.setText(personName);
            } else {
                mTvBestScore.setText("激烈争夺当中.....");
            }
        }
    }

    private void setWinAndTransportScore(List<PersonBo> personList) {
        if (personList == null || personList.size() < 0)
            return;
        int winNum = 0;
        int transportNum = 0;
        for (int i = 0; i < personList.size(); i++) {
            PersonBo personBo = personList.get(i);
            int score = personBo.getScore();
            if (score >= 0) {
                winNum += score;
            } else {
                transportNum += score;
            }
        }

        mTvWin.setText("赢家： " + winNum + "分");
        mTvTransport.setText("输家： " + transportNum + "分");
    }

    private void initlistener() {

        mListview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        PersonBo personBo = (PersonBo) mMainAdapter.getItem(position);
                        Log.d("lhx", "name: " + personBo.getName());
                        mPersonList.remove(personBo);
                        MyDataBase.getInstances(MainActivity.this).deletePersonInfo(personBo
                                .getName());
                        mMainAdapter.notifyDataSetChanged();
                        setNoContextView();
                        setWinAndTransportScore(mPersonList);
                        setBsetScore();
                        break;
                    default:
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    @OnClick({R.id.iv_right_icon, R.id.iv_add, R.id.iv_left_icon,R.id.iv_history_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right_icon:
                setEditTextDialog(this);
                break;
            case R.id.iv_add:
                Intent intent = new Intent(this, ComputeResultActivity.class);
                intent.putExtra("personList", (Serializable) mPersonList);
                startActivityForResult(intent, 10000);
                break;
            case R.id.iv_left_icon:
                if (mPersonList == null || mPersonList.size() <= 0) {
                    Toast.makeText(MainActivity.this, "暂无数据，不能清空", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    setShowDialog("温馨提示", "确认抹掉所有数据吗?", 0);
                }
                break;
            case R.id.iv_history_icon:
                intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("personList", (Serializable) mPersonList);
                startActivity(intent);
                break;
        }
    }

    private void setEditTextDialog(Context context) {
        final MaterialDialog materialDialog = new MaterialDialog(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        materialDialog.setContentView(view);
        materialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = (EditText) view.findViewById(R.id.et_name);
                String name = etName.getText().toString().trim();
                boolean isSameName = false;
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "大名为空，无法完成创建", Toast.LENGTH_SHORT).show();
                } else {
                    //mPersonList.clear();
                    //mPersonList = MyDataBase.getInstances(MainActivity.this).queryPersonInfo();
                    if (mPersonList != null && mPersonList.size() > 0) {
                        for (int i = 0; i < mPersonList.size(); i++) {
                            PersonBo personBo = mPersonList.get(i);
                            if (name.equalsIgnoreCase(personBo.getName())) {
                                isSameName = true;
                            } else {
                                isSameName = false;
                            }
                        }
                    }

                    if (isSameName) {
                        Toast.makeText(MainActivity.this, "不能重复录入相同的大名!", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        PersonBo personBo = new PersonBo();
                        personBo.setName(name);
                        personBo.setScore(0);
                        int num = (int) (Math.random() * 11);
                        personBo.setHeadId(num);
                        mPersonList.add(personBo);
                        MyDataBase.getInstances(MainActivity.this).insertPersonInfo(name, 0, num);
                        compareSrore();
                        //setBsetScore();
                        mListviewContainer.setVisibility(View.VISIBLE);
                        mNoContentContainer.setVisibility(View.GONE);
                        mMainAdapter.notifyDataSetChanged();
                        materialDialog.dismiss();
                    }
                }
            }
        });
        materialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainAdapter.notifyDataSetChanged();
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
        materialDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 清空所有数据
     *
     * @param title
     * @param content
     * @param type
     */
    private void setShowDialog(String title, String content, final int type) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle(title);
        materialDialog.setMessage(content);
        materialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    mPersonList.clear();
                    MyDataBase.getInstances(MainActivity.this).deleteAllPersonInfo();
                    mMainAdapter.notifyDataSetChanged();
                    mListviewContainer.setVisibility(View.GONE);
                    mNoContentContainer.setVisibility(View.VISIBLE);
                    mTvBestScore.setText("激烈争夺当中...");
                    mTvWin.setText("赢家： " + 0 + "分");
                    mTvTransport.setText("输家： " + 0 + "分");
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 500) {
            mPersonList.clear();
            mPersonList = (List<PersonBo>) data.getSerializableExtra("resultList");
            Log.d("lhx", "resultList size=" + mPersonList.size());
            compareSrore();
            setWinAndTransportScore(mPersonList);
            mMainAdapter.setAdapterData(mPersonList);
            setBsetScore();
        }
    }

    long oldBackTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - oldBackTime > 2000) {
            oldBackTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次将退出应用", Toast.LENGTH_SHORT).show();
        } else {
            //android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(0);
            //ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            //manager.killBackgroundProcesses(getPackageName());
            //finish();
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
