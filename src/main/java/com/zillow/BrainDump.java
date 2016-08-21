package com.zillow;

import java.awt.font.NumericShaper;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BrainDump {


    public static void lambdaExpressionsAndFunctionInterfaces() {

        //Anonymous function
        Thread n = new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                System.out.println(String.format("Thread is %s", name));
            }
        });
        n.start();

        //Lambda
        Thread m = new Thread( () -> {
            String name = Thread.currentThread().getName();
            System.out.println(String.format("Thread is %s", name));
        });
        m.start();

        List<Integer>  l = Arrays.asList(1,2,3,4,5);
        l.sort((Integer n1, Integer n2) -> { return n2 -n1;});

        //Omit return and { } on a single statement
        l.sort((Integer n1, Integer n2) ->  n2 - n1);

        //Inferred type no return and no parenthesis
        l.sort((n1, n2) -> n2 - n1);



    }

    private static void methodReference() {
        List<String> l = Arrays.asList("a,1", "b,1", "c,3", "d,4", "e,5");

        //Reference to static method
        //l.stream().map(String::toUpperCase).forEach((it) -> System.out.println(it));
        l.stream().map(String::toUpperCase).forEach(System.out::println);



        //Reference a instance method
        l.stream().map("*****"::concat).forEach(System.out::println);


        //Reference to an bound instance method
        Arrays.asList("a", "b", "").stream().map(String::isEmpty).forEach(System.out::print);

        //Constructors
        Arrays.asList("1", "2", "3").stream().map(Float::new).forEach(System.out::println);
    }



    private static void streams() {
        List<Integer> l = Arrays.asList(1, 2, 3, 4, 5);

        //Consumable
        //import java.util.stream.*
        Stream<Integer> stream = Stream.of(1,2,3,4,5);
        stream.map(i -> i+1).forEach(System.out::println);
        stream.map(i -> i*2).forEach(System.out::println);

        //Filter
        l.stream().filter(it -> it >= 4).forEach(System.out::println);

        //Map
        l.stream().map(it -> Math.pow(2,it)).forEach(System.out::println);

        //Reduce
        Integer sum = l.stream().reduce(0, (a,b) -> a+b);


        //Built-in aggregate functions on concrete streams
        Stream.of(1,1,2,2,3,4,4,5).distinct().forEach(System.out::println);
        double avg = IntStream.of(1, 1, 2, 2, 3, 4, 4, 5).average().getAsDouble();
        IntSummaryStatistics stats = IntStream.of(1, 1, 2, 2, 3, 4, 4, 5).summaryStatistics();

        //All operations are composable
        //import java.io.File;
        List<File> files = Arrays.asList(new File("/tmp").listFiles());
        files.stream() .filter(f -> f.length() > 100) .map(File::getName) .forEach(System.out::println);
        files.stream() .filter(f -> f.length() > 100).filter(f -> f.getName().startsWith("m")).map(File::getAbsolutePath).forEach(System.out::println);


        //Ranges and stream generators
        IntStream.range(0, 10).forEach(System.out::println);

        //Streams need not to be finite
        IntStream.iterate(0, i->i+10).limit(10).forEach(System.out::println);
        IntStream.iterate(0, i->i+10).filter(i-> i>5000).findFirst().ifPresent(System.out::println);




    }


    public static void main(String[] args) {
        lambdaExpressionsAndFunctionInterfaces();
        methodReference();
        streams();
    }



    public static List<Map> createData() {
        Request request = new Request.Builder()
                .url("https://raw.github.com/square/okhttp/master/README.md")
                .build();
        return null;
    }


    public static void collectors() {
        //Sort properties by type given a list of properties group them by type
    }

}
