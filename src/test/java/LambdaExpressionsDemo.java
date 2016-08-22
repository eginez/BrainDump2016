import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LambdaExpressionsDemo {
    @Test
    public void lambda1() {
        /**
         Lambda expression are a new way to express anonymous functions and or closures
         */
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

    }

    @Test
    public void lambda2() {
        //Anonymous function
        List<Integer> l = Arrays.asList(1,2,3,4,5);
        l.sort((Integer n1, Integer n2) -> { return n2 -n1;});

        //Omit return anddd { } on a single statement
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
}
