import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by estebanginez on 8/21/16.
 */
public class FunctionalnterfacesDemo {

    boolean myDebugFlag = true;
    AtomicInteger state = new AtomicInteger(0);
    Predicate<Integer> isEven  = i -> i % 2 == 0;
    Supplier<Integer> inc = () -> state.incrementAndGet();

    @Test
    public void lambda1() {
        /**
         * Enter functional interfaces
         - Java already had such interfaces, however by using the @FunctioInterface annotation, they ca be created with a lambda
         */
        //Predicate
        Predicate<Integer> isEven  = i -> i % 2 == 0;
        System.out.println(isEven.test(2));

    }

    @Test
    public void lambda2() {
        //Function
        Function<String, String> politely = s -> s.concat(", Please.");
        System.out.println(politely.apply("Pass me the salt"));

    }

    @Test
    public void lambda3() {
        //Simple Supplier
        AtomicInteger state = new AtomicInteger(0);
        Supplier<Integer> inc = () -> state.incrementAndGet();
        System.out.println(inc.get());
        System.out.println(inc.get());

    }

    @Test
    public void lambda4() {

        //Simple Consumer
        Consumer<Supplier<Integer>> printIfEven = s -> {
            Integer n = s.get();
            if (isEven.test(n)) {
                System.out.println(n);
            }

        };

        printIfEven.accept(inc);
        printIfEven.accept(inc);
        printIfEven.accept(inc);
        printIfEven.accept(inc);
    }

    @Test
    public void lambda5() {

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
    }

    @Test
    public void lambda6() {

        //That's nice but I am always 'printing'...
        //Let's define a method to extract out the operation
        //The consumer  can be anything that needs an integer

        proccessConditionally(inc, isEven, i -> System.out.println(i));
        proccessConditionally(inc, isEven, i -> {
            long time = System.currentTimeMillis() * i;
            System.err.println(time);
        });
    }

    private  void proccessConditionally(Supplier<Integer> source, Predicate<Integer> predicate, Consumer<Integer> consumer) {
        Integer n = source.get();
        if(predicate.test(n)) {
            consumer.accept(n);
        }
    }

    @Test
    public void loggingExample() {

        //Another example
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


    private  void logIfAllowed(Logger logger, Consumer<Logger> c) {
        if ((logger.getLevel() == Level.FINE || logger.getLevel() ==Level.INFO)  && myDebugFlag) {
            c.accept(logger);
        }
    }

}
