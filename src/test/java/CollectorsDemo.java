import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectorsDemo implements PrintableTest {

     class Property {
         public int id;
         public String type;
         public int price;

         @Override
         public String toString() {
             return "Property{" +
                     "id=" + id +
                     ", type='" + type + '\'' +
                     ", price=" + price +
                     '}';
         }

         public Property(int id, String type, int price) {
             this.id = id;
             this.type = type;
             this.price = price;
         }

         public String getType() {
             return type;
         }

         public int getPrice() {
             return price;
         }
     }

    List<Property> properties = Arrays.asList(
            new Property(0, "TOWNHOUSE", 80),
            new Property(1, "CONDO", 20),
            new Property(2, "DETACHED", 40),
            new Property(3, "CONDO", 40),
            new Property(4, "IGLOO", 60),
            new Property(5, "TOWNHOUSE", 80),
            new Property(6, "TREEHOUSE", 80),
            new Property(7, "TEEPEE", 90),
            new Property(8, "TEEPEE", 90),
            new Property(9, "DETACHED", 40)
    );


    @Test
    public void collection1() {
        /**
         * You probably notices all the operatins are being outputted to sdt::out but what if we want to
         * reuse the results.
         */
        List<Property> condo = properties.stream()
                .filter(it -> it.type.equals("CONDO"))
                .collect(Collectors.toList());
        print(condo);
    }

    @Test
    public void collections2() {
        //Collect to map, collecting to map supports a merger function as well
        Map<Integer, String> condoById = properties.stream()
                .filter(it -> it.type.equals("CONDO"))
                .collect(Collectors.toMap((Property p) -> p.id,
                        (Property p) -> p.toString()));

        print(condoById.entrySet());
    }

    @Test
    public void groupings1() {
        //Groupings
        //we can create a grouping by a manual classifier define as a lambda
        Map<String, List<Property>> all = properties.stream()
                .collect(Collectors.groupingBy(
                        (Property p) -> p.type.equals("CONDO") ? "CONDO" : "OTHER"));
        print(all.entrySet());
    }

    @Test
    public void groupings2() {
        //Or use a method of the class as classifier, notice the sumarizing function can vary
        Map<String, Long> countByType = properties.stream()
                .collect(Collectors.groupingBy(Property::getType, Collectors.counting()));
        print(countByType);

        //I can use a custom sumarizing function
        Map<String, Integer> sumByPrice = properties.stream()
                .collect(Collectors .groupingBy(
                                Property::getType,
                                Collectors.summingInt(Property::getPrice)));
        print(sumByPrice);

    }

    @Test
    public void partitioning() {

        Map<Boolean, List<Property>> partitioned1 = properties.stream()
                .collect(Collectors.partitioningBy((Property p) -> p.getPrice() > 50));
        print(partitioned1);

        Map<Boolean, Double> part2 = properties.stream()
                .collect(Collectors.partitioningBy(
                        (Property p) -> p.getPrice() > 50,
                        Collectors.averagingInt(Property::getPrice)));
        print(part2);
        //The collectors api has more functions to manually reduce a stream
        //If that doesn't work we can always roll in a child of Collector

    }



}
