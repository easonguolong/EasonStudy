package com.vollerystudy.diary.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vollerystudy.R;
import com.vollerystudy.diary.db.DiaryDatabaseHelper;
import com.vollerystudy.diary.utils.GetDate;
import com.vollerystudy.diary.widget.LinedEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by Administrator on 2017/6/30.
 */

public class AddDiaryActivity extends AppCompatActivity {

    @Bind(R.id.add_diary_tv_date)
    TextView mAddDiaryTvDate;
    @Bind(R.id.add_diary_et_title)
    EditText mAddDiaryEtTitle;
    @Bind(R.id.add_diary_et_content)
    LinedEditText mAddDiaryEtContent;
    @Bind(R.id.add_diary_fab_back)
    FloatingActionButton mAddDiaryFabBack;
    @Bind(R.id.add_diary_fab_add)
    FloatingActionButton mAddDiaryFabAdd;
    @Bind(R.id.right_labels)
    FloatingActionsMenu mRightLabels;
    @Bind(R.id.home_iv_draw)
    ImageView mIvDraw;
    @Bind(R.id.home_tv_title_normal)
    TextView mTvTitle;
    @Bind(R.id.home_iv_menu)
    ImageView mIvMenu;
    @Bind(R.id.contacts_tab_rl)
    LinearLayout mContactsTabRl;

    private DiaryDatabaseHelper mHelper;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        initToolbar();
        initView(intent);
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
    }

    private void initToolbar(){
        mIvDraw.setImageResource(R.mipmap.app_back);
        mTvTitle.setText("添加日记");
        mIvMenu.setVisibility(View.GONE);
    }

    private void initView(Intent intent){
        mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
        mAddDiaryTvDate.setText("今天,"+ GetDate.getDate());
        mAddDiaryEtContent.setText(intent.getStringExtra("content"));
    }

    @OnClick({R.id.home_iv_draw,R.id.add_diary_et_title,R.id.add_diary_et_content,R.id.add_diary_fab_back,R.id.add_diary_fab_add})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.home_iv_draw:
                backToDiaryFragment();
                break;
            case R.id.add_diary_et_title:
                break;
            case R.id.add_diary_et_content:
                break;
            case R.id.add_diary_fab_back:
                String date = GetDate.getDate().toString();
                String tag = String.valueOf(System.currentTimeMillis());
                String title = mAddDiaryEtTitle.getText().toString() + "";
                String content = mAddDiaryEtContent.getText().toString() + "";
                if (!title.equals("") || !content.equals("")) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", date);
                    values.put("title", title);
                    values.put("content", content);
                    values.put("tag", tag);
                    db.insert("Diary", null, values);
                    values.clear();
                }
                finish();
                break;
            case R.id.add_diary_fab_add:
                backToDiaryFragment();
                break;
        }
    }


    private void backToDiaryFragment(){
        final String dateBack = GetDate.getDate().toString();
        final String titleBack = mAddDiaryEtTitle.getText().toString();
        final String contentBack = mAddDiaryEtContent.getText().toString();
        if(!titleBack.isEmpty() || !contentBack.isEmpty()){
            AlertDialog.Builder builder =new  AlertDialog.Builder(this);
            builder.setMessage("是否保存日记?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", dateBack);
                    values.put("title", titleBack);
                    values.put("content", contentBack);
                    db.insert("Diary",null,values);
                    values.clear();
                    finish();
                }
            }).setNegativeButton("取消",null).create().show();
        }else{
            finish();
        }
    }

}
