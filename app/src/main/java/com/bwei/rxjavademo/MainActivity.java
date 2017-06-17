package com.bwei.rxjavademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import org.reactivestreams.Subscription;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        just();


    }
     Disposable disposable ;

    public void test1(){

        //如果观察者 接收到  onError  onComplete 这两个事件，后面的被观察者所发送的事件无法接受
//        disposable.dispose(); 让被观察者和观察者之间 取消订阅


//        Observable.c
        //被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {

                System.out.println("observable   " + Thread.currentThread().getName());

//                ObservableEmitter 发射器
                e.onNext("1");
                e.onNext("2");
//                e.onError(new NullPointerException());
//                System.out.println("observable  3 4 ");
//                e.onNext("3");
//                e.onNext("4");
            }
        }) ;
        //观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {


//                disposable = d ;

            }

            @Override
            public void onNext(@NonNull String o) {

                System.out.println("observer onNext = " + o + " " + Thread.currentThread().getName());


            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("observer onError = " );

            }

            @Override
            public void onComplete() {
                System.out.println("observer onComplete = ");

                //事件完成了

            }
        } ;


        //通过订阅 让被观察者和观察者 产生关联
//        subscribeOn 指定被观察者所在的线程
//        observeOn 指定观察者所在的线程
//        Schedulers 调度器
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

//        observable.subscribe(observer);
    }


    public void test2(){


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);

            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {

                System.out.println("integer = " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }




//    map 变换操作符
    public void map(){


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                e.onNext("1");

            }
//            String 第一个参数  onNext 发送数据的一个类型
//            Integer  第二个参数  经过map变换 所产生的数据类型
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(@NonNull String s) throws Exception {

                System.out.println("map apply = " + s);


                return Integer.valueOf(s);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {


                System.out.println("subscribe onNext = " + integer);


            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });



//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                e.onNext("http://www.aaaaa.jpg");
//            }
//        }).map(new Function<String, Bitmap>() {
//            @Override
//            public Bitmap apply(@NonNull String s) throws Exception {
//
//
//                return BitmapFactory.decodeResource(getResources(),1);
//            }
//        }).subscribe(new Observer<Bitmap>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@NonNull Bitmap bitmap) {
//
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });


    }




//    flatMap 无序的，产生数据源  或产生一个集合 变换操作符
    public void flatmap() {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {


                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onNext("4");
                e.onNext("5");

            }
        }).flatMap(new Function<String, ObservableSource<ArrayList>>() {
            @Override
            public ObservableSource<ArrayList> apply(@NonNull String s) throws Exception {

                ArrayList list = new ArrayList();
                for (int i = 0; i < 5; i++) {
                    list.add(s + "  " + i);
                }
                return Observable.fromArray(list).delay(1, TimeUnit.SECONDS);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ArrayList o) {

                for (int i = 0; i < o.size(); i++) {
                    System.out.println("o = " + o.get(i));
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

//    concatMap 有序的
    public void concatmap(){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {


                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onNext("4");
                e.onNext("5");
            }
        }).concatMap(new Function<String, ObservableSource<ArrayList>>() {
            @Override
            public ObservableSource<ArrayList> apply(@NonNull String s) throws Exception {
                ArrayList list = new ArrayList();
                for (int i = 0; i < 5; i++) {
                    list.add(s + "  " + i);
                }
                return Observable.fromArray(list).delay(1, TimeUnit.SECONDS);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ArrayList o) {
                for (int i = 0; i < o.size(); i++) {
                    System.out.println("o = " + o.get(i));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }



    public void map1(){



//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//
//            }
//        }).map(new Function<String, ObservableSource<ArrayList>>() {
//            @Override
//            public ObservableSource<ArrayList> apply(@NonNull String s) throws Exception {
//                return null;
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ObservableSource<ArrayList>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ObservableSource<ArrayList> arrayListObservableSource) {
//
//                        arrayListObservableSource.subscribe(new Observer<ArrayList>() {
//                            @Override
//                            public void onSubscribe(@NonNull Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(@NonNull ArrayList arrayList) {
//
//                            }
//
//                            @Override
//                            public void onError(@NonNull Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });



        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onNext("4");
                e.onNext("5");
//                e.onComplete();
            }
        }).map(new Function<String, ArrayList>() {
            @Override
            public ArrayList apply(@NonNull String s) throws Exception {
                ArrayList list = new ArrayList();
                for (int i = 0; i < 5; i++) {
                    list.add(s + "  " + i);
                }
                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList o) {
                        for (int i = 0; i < o.size(); i++) {
                            System.out.println("o = " + o.get(i));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    //压缩合并

    public void zip(){


        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onNext("4");

            }
        })  ;


        Observable observable1 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
                e.onNext("D");
                e.onNext("E");
            }
        }) ;

        Observable.zip(observable, observable1, new BiFunction<String,String,String>() {
            @Override
            public String apply(@NonNull String o, @NonNull String o2) throws Exception {
                return o + " ----- " + o2;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {

                        System.out.println("o = " + o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


//过滤
    public void fliter(){


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                for(int i=0;i<100;i++){

                    e.onNext(i);
                }

            }
        }).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
//过滤
                //true  表示数据是我们需要的
//                false 不需要的
                return integer % 10 == 0;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {

                System.out.println("integer = " + integer);

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }




    public void consumer(){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                for(int i=0;i<10;i++){
                    e.onNext(i+" ");
                }

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {

                System.out.println("s = " + s);
            }
        });

    }


    public void consumer1(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                for(int i=0;i<10;i++){
                    e.onNext(i+" ");
                }

                e.onError(new NullPointerException());

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {

                System.out.println("accept onnext = " + s);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("accept onerror = ");

            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("accept oncomplete = " );

            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                System.out.println("accept Disposable = " );

            }
        });
    }



    public void memory(){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                for(int i=0;i<10000000;i++){

                    e.onNext(i);

                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {

                System.out.println("integer = " + integer);
            }
        });



    }




//    Flowable 支持背压
    public void flowable(){

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {

                for(int i=0;i<140;i++){
                    e.onNext(i + "");
                }
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {

                System.out.println("s = " + s);
            }
        });


    }



    public void flowable1(){

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {

                e.onNext(1);

                e.onNext(2);

            }
        },BackpressureStrategy.ERROR).subscribe(new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {


                //观察者 可以 接受 被观察者 发送事件 的次数   Integer.MAX_VALUE
                s.request(Integer.MAX_VALUE);

            }

            @Override
            public void onNext(Integer integer) {


                System.out.println("integer = " + integer);

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });


    }




    public void just(){

//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//
//                e.onNext("1");
//
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(@NonNull String s) throws Exception {
//
//            }
//        });

        Observable.just("1","2","3").subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.println("s = " + s);
            }
        });



    }



    public void total(){


        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                 e.onNext("1");

            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                e.onNext("A");

            }
        });


        Observable.zip(observable1, observable2, new BiFunction<String, String, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull String s2) throws Exception {
                return null;
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(@NonNull String s) throws Exception {
                return null;
            }
        }).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String s) throws Exception {
                return null;
            }
        }).filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                return false;
            }
        }).concatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String s) throws Exception {
                return null;
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {

            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {

            }
        });




    }



}
