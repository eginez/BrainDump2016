import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamsDemo {

    static boolean  myDebugFlag =  true;
    List<Integer> l = Arrays.asList(1, 2, 3, 4, 5);


    @Test
    public  void consumable() {

        //Consumable
        //import java.util.stream.*
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
        stream.map(i -> i + 1).forEach(System.out::println);
        stream.map(i -> i * 2).forEach(System.out::println);
    }

    @Test
    public  void filter() {
        //Filter
        l.stream().filter(it -> it >= 4).forEach(System.out::println);
    }

    @Test
    public   void map() {

        //Map
        l.stream().map(it -> Math.pow(2, it)).forEach(System.out::println);
    }

    @Test
    public   void reduce() {

        //Reduce
        Integer sum = l.stream().reduce(0, (a, b) -> a + b);
    }

    @Test
    public  void builtin() {


        //Built-in aggregate functions on concrete streams
        Stream.of(1, 1, 2, 2, 3, 4, 4, 5).distinct().forEach(System.out::println);
        double avg = IntStream.of(1, 1, 2, 2, 3, 4, 4, 5).average().getAsDouble();
        IntSummaryStatistics stats = IntStream.of(1, 1, 2, 2, 3, 4, 4, 5).summaryStatistics();
    }

    @Test
    public  void composable() {

        //All operations are composable
        List<File> files = Arrays.asList(new File("/tmp").listFiles());
        files.stream().filter(f -> f.length() > 100).map(File::getName).forEach(System.out::println);
        files.stream().filter(f -> f.length() > 100).filter(f -> f.getName().startsWith("m")).map(File::getAbsolutePath).forEach(System.out::println);
    }

    @Test
    public  void generators() {
        //Ranges and stream generators
        IntStream.range(0, 10).forEach(System.out::println);

        //Streams need not to be finite
        IntStream.iterate(0, i->i+10).limit(10).forEach(System.out::println);
        IntStream.iterate(0, i->i+10).filter(i-> i>5000).findFirst().ifPresent(System.out::println);

    }
}
