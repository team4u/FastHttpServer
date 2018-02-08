package org.team4u.test;


import cn.hutool.http.HttpUtil;
import org.team4u.kit.core.debug.Benchmark;

public class ServerBenchmark {

    public static void get() {
        System.out.println(HttpUtil.get("http://127.0.0.1:7000/test/rest/1/2"));

        new Benchmark().start(3, new Runnable() {
            @Override
            public void run() {
                if (HttpUtil.get("http://127.0.0.1:7000/test/rest/1/2") == null) {
                    throw new RuntimeException();
                }
            }
        });
    }

    public static void main(String[] args) {
        get();
    }
}
