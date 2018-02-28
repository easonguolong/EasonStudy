package com.retrofitstudy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String str = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = this.getPackageManager();
        try{
            PackageInfo info = pm.getPackageInfo(this.getPackageName(),0);
            String ver = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        //login();
        checkUpdate();
       /// getUserInfo();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getUserInfo(){
        NetService service = RetrofitManager.createService(NetService.class);
        Observable<Object> observable = service.getUserinfo("127");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {



                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("dsds",e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        String result = String.valueOf(o);
                        try {
                            JSONObject JS = new JSONObject(result);
                            String id  = JS.getString("id");
                            String ph = JS.getString("phone");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkUpdate(){
         NetService service = RetrofitManager.createService(NetService.class);
         Observable<Object> observable = service.CheckUpdate("check",4);
         observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("dsds",e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        String result = String.valueOf(o);
                        try{
                            JSONObject updateJSon = new JSONObject(result);
                            String new_ver = updateJSon.getString("version");
                          if (updateJSon.getBoolean("success")){

                          }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }



    private void login(){
       NetService service =RetrofitManager.createService(NetService.class);
        Observable<Object> observable = service.LoginTask("15527062493","12345678");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        str = o.toString();
                        try{
                            JSONObject js = new JSONObject(str);
                            String res = js.getString("success");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });





//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.thingtill.com/index.php/home/android/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//        NetService service = retrofit.create(NetService.class);
//        Observable<Object> observable = service.LoginTask("88888888888","12345678");
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onCompleted() {
//                        ToastUtils.warning("test",2*5000,true).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//                            str = o.toString();
//                    }
//                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
