package com.rxpermisssions

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

import com.tbruyelle.rxpermissions2.RxPermissions

import java.util.Arrays

import me.kareluo.ui.OptionMenu
import me.kareluo.ui.PopupMenuView
import me.kareluo.ui.PopupView

class MainActivity : AppCompatActivity() {

    private var btn_camera: Button? = null
    private var popupMenuView: PopupMenuView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        btn_camera = findViewById<View>(R.id.camera) as Button
        btn_camera!!.setOnClickListener {
            //                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
            //                rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
            //                    @Override
            //                    public void onSubscribe(Disposable d) {
            //
            //                    }
            //
            //                    @Override
            //                    public void onNext(Boolean value) {
            //                        if (value){
            //                            Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
            //                        }else
            //                            Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
            //                    }
            //
            //                    @Override
            //                    public void onError(Throwable e) {
            //
            //                    }
            //
            //                    @Override
            //                    public void onComplete() {
            //
            //                    }
            //                });
            //                if (isWeixinAvilible(MainActivity.this)){
            //                    new Thread(){
            //                        @Override
            //                        public void run() {
            //                            Intent intent = new Intent();
            //                            ComponentName cmp = new ComponentName(" com.tencent.mm ","com.tencent.mm.ui.LauncherUI");
            //                            intent.setAction(Intent.ACTION_MAIN);
            //                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            //                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //                            intent.setComponent(cmp);
            //                            startActivity(intent);
            //                        }
            //                    }.start();
            //                }else
            //                    Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
            popupMenuView = PopupMenuView(this@MainActivity)
            popupMenuView!!.menuItems = Arrays.asList(OptionMenu("copy"), OptionMenu("votie"), OptionMenu("test"))
            popupMenuView!!.setSites(PopupView.SITE_LEFT, PopupView.SITE_TOP, PopupView.SITE_RIGHT, PopupView.SITE_BOTTOM)
            popupMenuView!!.orientation = LinearLayout.VERTICAL
            popupMenuView!!.show(btn_camera)
        }
    }

    companion object {

        fun isWeixinAvilible(context: Context): Boolean {
            val packageManager = context.packageManager// 获取packagemanager
            val pinfo = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (i in pinfo.indices) {
                    val pn = pinfo[i].packageName
                    if (pn == "com.tencent.mm") {
                        return true
                    }
                }
            }
            return false
        }
    }


}
