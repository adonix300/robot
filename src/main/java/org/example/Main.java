package org.example;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                char targetChar = 'R';
                int countR = charCounter(route, targetChar);
                synchronized (sizeToFreq) {
                    sizeToFreq.put(countR, sizeToFreq.getOrDefault(countR, 0) + 1);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        analyzeResults();

    }

    public static void analyzeResults() {
        int maxFrequency = 0;
        Map<Integer, Integer> otherSizes = new HashMap<>();

        for (int size : sizeToFreq.keySet()) {
            int freq = sizeToFreq.get(size);
            if (freq > maxFrequency) {
                maxFrequency = freq;
            }

            if (freq > 1) {
                otherSizes.put(size, freq);
            }
        }

        Integer mostFrequentSize = getKeyByValue(sizeToFreq, maxFrequency);

        System.out.println("Самое частое количество повторений " + mostFrequentSize
                + " (встретилось " + maxFrequency + " раз)");

        if (!otherSizes.isEmpty()) {
            System.out.println("Другие размеры:");
            for (Map.Entry<Integer, Integer> entry : otherSizes.entrySet()) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }

    public static Integer getKeyByValue(Map<Integer, Integer> map, Integer value) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int charCounter(String text, char letter) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == letter) {
                count++;
            }
        }
        return count;
    }

}