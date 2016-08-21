package com.zillow;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import okhttp3.Request;

public class BrainDump {

    static boolean  myDebugFlag =  true;


    public static void main(String[] args) {
        lambdaExpressions();
        functionInterfaces();
        methodReference();
        optional();
        streams();
        //collectors();

    }



    public static void lambdaExpressions() {
        /**
        Lambda expression are a new way to express anonymous functions and or closures
         */

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

        /**
        Lambas are powerful because, they allow us to
        - Receive functions as arguments
        - Return a function
        - A combination of both

        But before we can use this power we have to be able to declare functions
         */


    }


    private static void functionInterfaces() {


        /**
         * Enter functional interfaces
         - Java already had such interfaces, however by using the @FunctioInterface annotation, they ca be created with a lambda
         */

       //Predicate
        Predicate<Integer> isEven  = i -> i % 2 == 0;
        System.out.println(isEven.test(2));


        //Function
        Function<String, String> politely = s -> s.concat(", Please.");
        System.out.println(politely.apply("Pass me the salt"));


        //Simple Supplier
        AtomicInteger state = new AtomicInteger(0);
        Supplier<Integer> inc = () -> state.incrementAndGet();
        System.out.println(inc.get());
        System.out.println(inc.get());

        //Simple Consumer
        Consumer<Supplier<Integer>> printIfEven =  s -> {
            Integer n = s.get();
            if(isEven.test(n)) {
                System.out.println(n);
            }

        };

        printIfEven.accept(inc);
        printIfEven.accept(inc);
        printIfEven.accept(inc);
        printIfEven.accept(inc);




        //functions can be combined to create more powerful and higher abstractions
        BiConsumer<Supplier<Integer>, Predicate<Integer>> printConditionally = (s, p) -> {
            Integer n = s.get();
            if(p.test(n)) {
                System.out.println(n.intValue());
            }
        };
        printConditionally.accept(inc, isEven);
        printConditionally.accept(inc, isEven);


        printConditionally.accept(inc, i -> i % 2 != 0);
        printConditionally.accept(inc, i -> i % 3 != 0);

        //That's nice but I am always 'printing'...
        //Let's define a method to extract out the operation
        //The consumer  can be anything that needs an integer
        proccessConditionally(inc, isEven, i -> System.out.println(i));
        proccessConditionally(inc, isEven, i -> {
            long  time = System.currentTimeMillis() * i;
            System.err.println(time);
        });

        //Another example a little more concrete
        //Let's say our app has to check some internal state before performing logging tasks
        Logger logger = Logger.getAnonymousLogger();

        //we end up with code like  this sprinkled around the app
        if ((logger.getLevel() == Level.FINE || logger.getLevel() ==Level.INFO)  && myDebugFlag) {
            logger.fine("done");
        }

        // in some other occasions we want to time things
        final long start = System.currentTimeMillis();
        if ((logger.getLevel() == Level.FINE || logger.getLevel() ==Level.INFO)  && myDebugFlag) {
            long end = System.currentTimeMillis();
            logger.fine("done it took: " +  (start - end));
        }

        //If we were to use lambdas, we coudn then abstract out the operations
        //from the predicates
        logIfAllowed(logger, l -> {
            l.fine("done");
        });

        logIfAllowed(logger, l -> {
            long end = System.currentTimeMillis();
            logger.fine("done it took: " +  (start - end));
        });
    }

    private static void proccessConditionally(Supplier<Integer> source, Predicate<Integer> predicate, Consumer<Integer> consumer) {
        Integer n = source.get();
        if(predicate.test(n)) {
            consumer.accept(n);
        }
    }

    private static void logIfAllowed(Logger logger, Consumer<Logger> c) {
        if ((logger.getLevel() == Level.FINE || logger.getLevel() ==Level.INFO)  && myDebugFlag) {
            c.accept(logger);
        }
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


    public static void optional() {
        Optional<String> o = Optional.of("String");
        Optional<String> p = Optional.ofNullable(null);

        if(o.isPresent()) {
            System.out.println(o);
        }

        //Or more succinctly
        o.ifPresent(System.out::println);
        p.ifPresent(System.out::println);


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


    public static List<Map> createData() {
        Request request = new Request.Builder()
                .url("https://raw.github.com/square/okhttp/master/README.md")
                .build();
        return null;
    }

}
