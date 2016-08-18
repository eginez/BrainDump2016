package com.zillow;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import static java.lang.System.out;

public class BrainDump {


    public static void lambdaExpressions() {
        List<Integer>  l = Arrays.asList(1,2,3,4,5);

        //Simple lambda statement without { }
        l.forEach( (e) -> System.out.println(e));

        //Multiline lambda statement with {}
        l.forEach( (e) ->  {
            long ts = System.currentTimeMillis();
            out.println(String.format("Element %d and time: %d", e, ts));
        });

    }




    public static void main(String[] args) {
        lambdaExpressions();
    }


    public static List<Map> createData() {
        Request request = new Request.Builder()
                .url("https://raw.github.com/square/okhttp/master/README.md")
                .build();
        return null;
    }

}
