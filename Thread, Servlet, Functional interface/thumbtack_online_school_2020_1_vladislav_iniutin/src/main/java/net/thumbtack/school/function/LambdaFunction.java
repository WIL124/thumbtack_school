package net.thumbtack.school.function;

import lombok.Data;

import java.util.*;
import java.util.function.*;

@Data
class Person {
    private String name;
    private Person mother;
    private Person father;


    Person(String name, Person mother, Person father) {
        this.name = name;
        this.father = father;
        this.mother = mother;
    }

    Person(String name) {
        this(name, null, null);
    }

    Person getMothersMotherFather() {
        Person mother = this.getMother();
        if (mother != null) {
            return this.getMother().getFather();
        } else return null;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}

@Data
class PersonWithOptional {
    private String name;
    private Optional<PersonWithOptional> mother;
    private Optional<PersonWithOptional> father;

    public Optional<PersonWithOptional> getMothersMotherFather() {
        return this.getMother().flatMap(PersonWithOptional::getFather);
    }

    public PersonWithOptional(String name, PersonWithOptional mother, PersonWithOptional father) {
        this.setMother(Optional.ofNullable(mother));
        this.setFather(Optional.ofNullable(father));
        this.name = name;
    }

}

@FunctionalInterface
interface MyFunction<T, K> {
    K apply(T arg);

    default <V> MyFunction<T, V> andThen(MyFunction<? super K, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    default <V> MyFunction<V, K> compose(MyFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

//    K apply(T arg1, T arg2);  can't compile with 2 methods
}

public class LambdaFunction {
    public static void main(String[] args) {
        String string = "Лямбда-выражения пришли в Java из функционального программирования, а туда — из математики";
//
        //use lambda-expression
        MyFunction<String, List<String>> split = s -> Arrays.asList(s.split(" "));
//        Function<List<?>, Integer> count = x -> x.size();

        //use  method reference
        MyFunction<List<?>, Integer> count = List::size;
        System.out.println(count.apply(split.apply(string)));

        //splitAndCount function by andThen
        MyFunction<String, Integer> splitAndCount = split.andThen(count);
        System.out.println(splitAndCount.apply(string));

        //splitAndCount function by compose
        MyFunction<String, Integer> splitAndCountSecond = count.compose(split);
        System.out.println(splitAndCountSecond.apply(string));

        //function create
        MyFunction<String, Person> create = Person::new;
        System.out.println(create.apply("Петя").toString());

        //function get Max
        DoubleBinaryOperator getMax = Math::max;
        System.out.println(getMax.applyAsDouble(111, 75));

        //currentDateFunction
        Supplier<Date> getCurrentDate = Date::new;
        System.out.println(getCurrentDate.get().toString());

        //isEvenFunction
        Predicate<Integer> isEvenFun = integer -> integer % 2 == 0;
        System.out.println(isEvenFun.test(12));
        System.out.println(isEvenFun.test(13));

        //areEqualFunction
        BiPredicate<Integer, Integer> areEqual = Integer::equals;
        System.out.println(areEqual.test(3, 5));

        // Function -> MyFunction


        //CreatePersonWithOptional and modified Person

        //test Person
        Person grandstanded = new Person("Прадедушка", null, null);
        Person grandma = new Person("Бабушка", null, grandstanded);
        Person mother = new Person("Мама", grandma, null);
        System.out.println("Person mother: " + mother.getMothersMotherFather());
        System.out.println("Person grandma " + grandma.getMothersMotherFather());

        if (grandstanded.equals(mother.getMothersMotherFather())) System.out.println("Работает!");
        else System.out.println("Что-то не так");

        //test PersonWithOptional
        PersonWithOptional grandstandedOpt = new PersonWithOptional("Прадед",null, null);
        PersonWithOptional grandmaOpt = new PersonWithOptional("Бабушка",grandstandedOpt, grandstandedOpt);
        PersonWithOptional motherOpt = new PersonWithOptional("Мама", grandmaOpt, grandstandedOpt);
        System.out.println("PersonOptional mother  " + motherOpt.getMothersMotherFather());
        System.out.println("PersonOptional grandma  " + grandmaOpt.getMothersMotherFather());

        if (grandstandedOpt.equals(motherOpt.getMothersMotherFather().get())) System.out.println("Работает!");
        else System.out.println("Что-то не так");
    }
}
