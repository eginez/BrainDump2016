import org.junit.Test;

import java.util.Optional;

/**
 * Created by estebanginez on 8/21/16.
 */
public class OptionalDemo {

    @Test
    public  void optional() {
        Optional<String> o = Optional.of("String");
        Optional<String> p = Optional.ofNullable(null);

        if(o.isPresent()) {
            System.out.println(o);
        }

        //Or more succinctly
        o.ifPresent(System.out::println);
        p.ifPresent(System.out::println);
    }
}
