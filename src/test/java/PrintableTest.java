import java.util.Collection;
import java.util.Map;

/**
 * Created by estebanginez on 8/21/16.
 */
public interface PrintableTest {
    default void print(Collection c) {
        c.forEach(System.out::println);
        System.out.println("===================");
    }

    default void print(Map c) {
        print(c.entrySet());
    }

    default void print(Object[] c) {
        for(Object o : c) {
            this.print(o);
        }
    }

    default void print(Object c) {
        this.print(c.toString());
    }

    default void print(String s) {
        System.out.println(s);
        System.out.println("===================");

    }

}
