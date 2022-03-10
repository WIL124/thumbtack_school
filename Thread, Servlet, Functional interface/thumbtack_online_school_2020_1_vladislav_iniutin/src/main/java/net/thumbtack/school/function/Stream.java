package net.thumbtack.school.function;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
class PersonWithAge {
    private String name;
    private int age;
}

public class Stream {


    IntStream transform(IntStream stream, IntUnaryOperator op) {
        stream.map(op).forEach(System.out::println);
        return stream;
    }

    IntStream transformToParallelStream(IntStream stream, IntUnaryOperator op) {
        stream.parallel().map(op).forEach(System.out::println);
        return stream;
    }
    Optional<Integer> sum(List<Integer> list){
        return list.stream().reduce(Integer::sum);
    }
    Optional<Integer> product(List<Integer> list){
        return list.stream().reduce((a,b) -> a*b);
    }
    public static void main(String[] args) {
        Stream stream = new Stream();
        IntStream intStream = IntStream.of(1, 2, 3, 4, 7);
        System.out.println("Transform func by stream");
        stream.transform(intStream, p -> p * p);
        IntStream intStream1 = IntStream.of(1, 2, 3, 4, 7);
        System.out.println("Transform func by parallel stream");
        stream.transformToParallelStream(intStream1, p -> p * p);

        PersonWithAge p1 = new PersonWithAge("Иван", 37);
        PersonWithAge p2 = new PersonWithAge("Сидор", 40);
        PersonWithAge p3 = new PersonWithAge("Пётр", 18);
        PersonWithAge p4 = new PersonWithAge("Влад", 23);
        PersonWithAge p5 = new PersonWithAge("Иван", 31);
        PersonWithAge p6 = new PersonWithAge("Иван", 31);

        //Имея список List<Person>, при помощи Stream API необходимо вернуть уникальные имена для всех людей старше 30 лет,
        //отсортированные по количеству людей с одинаковым именем. Используйте Collectors.groupingBy
        System.out.println("Persons names: ");
        List<PersonWithAge> list = List.of(p1, p2, p3, p4, p5, p6);
        list.stream()
                .filter(p -> p.getAge() > 30)
                .collect(Collectors.groupingBy(PersonWithAge::getName, Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .forEach(System.out::println);


//      Реализуйте sum(List<Integer> list) (сумма элементов списка) и product(List<Integer> list) (их произведение) через Stream.reduce
        List<Integer> integerList = List.of(1,2,3,4,5,6,7,8,9,10);
        System.out.println("Sum: " + stream.sum(integerList).get());

        System.out.println("Product: " + stream.product(integerList).get());
    }
}
