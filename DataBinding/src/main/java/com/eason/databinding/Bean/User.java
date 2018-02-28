package com.eason.databinding.Bean;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.eason.databinding.BR;


/**
 * Created by Administrator on 2017/12/1.
 */

public  class User extends BaseObservable{
    private String firstName;
    private String lastName;

    @Bindable
    public String getFirstName() {
        return this.firstName;
    }

    @Bindable
    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}
