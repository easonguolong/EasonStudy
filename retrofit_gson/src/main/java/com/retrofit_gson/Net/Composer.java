package com.retrofit_gson.Net;

import com.retrofit_gson.model.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/31.
 */

public class Composer {

    public static <T> ObservableTransformer<BaseResponse<T>,T> commSchedulers(){
        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResponse<T>> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        }).subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Function<BaseResponse<T>, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(BaseResponse<T> response) throws Exception {
                                if (response.isSuccess()){
                                    return createData(response.getData());
                                }else{
                                    return Observable.error(new Exception(response.getMsg()));
                                }
                            }
                        });
            }
        };
    }

    private static <T> Observable<T> createData(final T data){
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(data);
                    emitter.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }
}
