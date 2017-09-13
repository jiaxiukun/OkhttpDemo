package com.example.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = App.okHttpClient();
    }


    //同步的get
    public void get(View view) {

        //request 设置url
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //通过newCall方法将request转换成call ,如果用execute()是同步执行
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {

                        //通过response.body().string()拿到服务器给我们的json
//                        String json = response.body().string();

                        //通过response.body().inputStream拿到服务器给我们的输入流 -- 主要用在大文件
//                        InputStream inputStream = response.body().byteStream();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "get成功", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    //异步,所以的回调方法里面都是分线程.不能更新ui
    public void getAsync(View view) {
        //request 设置url
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    //用Gson解析
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "get成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //提示用户
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }


    //Post异步请求
    public void postAsync(View view) {
        FormBody formBody = new FormBody.Builder()
                .add("type", "yuantong")
                .add("postid", "11111111111")
                .build();
        final Request request = new Request.Builder()
                .url(" http://www.kuaidi100.com/query")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "post失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    //用Gson解析
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "post成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //提示用户
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
