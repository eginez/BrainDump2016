import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by estebanginez on 8/21/16.
 */
public class MethodReferenceDemo {
    List<String> l = Arrays.asList("a,1", "b,1", "c,3", "d,4", "e,5");
    @Test
    public void mr1() {

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
}
