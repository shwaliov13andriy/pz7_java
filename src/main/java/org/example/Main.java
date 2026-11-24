package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        List<Title> titles = Arrays.asList(
                new Title(1, "Оппенгеймер", 2023),
                new Title(2, "Матриця", 1999),
                new Title(3, "Дивні дива", 2016),
                new Title(4, "Назад у майбутнє", 1985),
                new Title(5, "Дюна", 2021),
                new Title(6, "Початок", 2010),
                new Title(7, "Чорнобиль", 2019),
                new Title(8, "Шрек", 2001),
                new Title(9, "Темний лицар", 2008)
        );

        System.out.println("==================================================");
        System.out.println("ДОСЛІД 1: Сортування трьома способами та лямбди");
        System.out.println("==================================================");
        runExperiment1(titles);

        System.out.println("\n==================================================");
        System.out.println("ДОСЛІД 2: Цикл проти Stream API та лінивість peek");
        System.out.println("==================================================");
        runExperiment2(titles);

        System.out.println("\n==================================================");
        System.out.println("ДОСЛІД 3: Optional, get() та orElse()");
        System.out.println("==================================================");
        runExperiment3(titles);
    }

    private static void runExperiment1(List<Title> original) {
        List<Title> list1 = new ArrayList<>(original);
        list1.sort(new Comparator<Title>() {
            @Override
            public int compare(Title a, Title b) {
                return Integer.compare(a.getYear(), b.getYear());
            }
        });
        System.out.println("1. Анонімний клас: " + list1);

        List<Title> list2 = new ArrayList<>(original);
        list2.sort((a, b) -> Integer.compare(a.getYear(), b.getYear()));
        System.out.println("2. Лямбда-вираз:   " + list2);

        List<Title> list3 = new ArrayList<>(original);
        list3.sort(Comparator.comparingInt(Title::getYear));
        System.out.println("3. Посилання на метод: " + list3);


//        int calls = 0;
//        list2.sort((a, b) -> {
//            calls++; // java: local variables referenced from a lambda expression must be final or effectively final
//            return Integer.compare(a.getYear(), b.getYear());
//        });
}

    private static void runExperiment2(List<Title> titles) {
        List<String> loopResult = getTitlesAfter2015Loop(titles);

        List<String> streamResult = titles.stream()
                .filter(t -> t.getYear() > 2015)
                .map(Title::getName)
                .toList();

        System.out.println("Цикл for-each: " + loopResult);
        System.out.println("Stream API:    " + streamResult);
        System.out.println("Результати збігаються: " + loopResult.equals(streamResult));

        System.out.println("\n--- Запуск peek З термінальною операцією toList() ---");
        List<String> withTerminal = titles.stream()
                .peek(t -> System.out.println("peek (до фільтра): " + t))
                .filter(t -> t.getYear() > 2015)
                .map(Title::getName)
                .toList();
        System.out.println("Зібрано елементів: " + withTerminal.size());

        System.out.println("\n--- Запуск peek БЕЗ термінальної операції ---");
        Stream<String> lazyStream = titles.stream()
                .peek(t -> System.out.println("peek (не виконається): " + t))
                .filter(t -> t.getYear() > 2015)
                .map(Title::getName);
        System.out.println("Кінець блоку без термінальної операції (вище нічого не надруковано).");
    }

    private static List<String> getTitlesAfter2015Loop(List<Title> titles) {
        List<String> result = new ArrayList<>();
        for (Title t : titles) {
            if (t.getYear() > 2015) {
                result.add(t.getName());
            }
        }
        return result;
    }

    private static void runExperiment3(List<Title> titles) {
        Optional<Title> oldTitle = titles.stream()
                .filter(t -> t.getYear() < 1950)
                .findFirst();

        System.out.println("Пошук до 1950 року (Optional): " + oldTitle);

        System.out.println("Спроба викликати get() на порожньому Optional:");
        try {
            Title t = oldTitle.get();
            System.out.println(t);
        } catch (NoSuchElementException e) {
            e.printStackTrace(System.out);
        }

        String notFoundName = oldTitle
                .map(Title::getName)
                .orElse("не знайдено");
        System.out.println("Пошук без результату (map + orElse): " + notFoundName);

        Optional<Title> modernTitle = titles.stream()
                .filter(t -> t.getYear() > 2015)
                .findFirst();

        System.out.println("\nПошук після 2015 року (Optional): " + modernTitle);
        String foundName = modernTitle
                .map(Title::getName)
                .orElse("не знайдено");
        System.out.println("Пошук за умовою досліду 2 (map + orElse): " + foundName);
    }
}