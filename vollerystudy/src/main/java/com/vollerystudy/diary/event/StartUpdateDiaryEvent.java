package com.vollerystudy.diary.event;

import com.vollerystudy.diary.bean.DiaryBean;

/**
 * Created by Administrator on 2017/6/30.
 */

public class StartUpdateDiaryEvent {
    private DiaryBean mDiaryBean;
    public StartUpdateDiaryEvent(DiaryBean diaryBean){
        mDiaryBean = diaryBean;
    }
    public DiaryBean getDiaryBean(){
        return mDiaryBean;
    }
}
