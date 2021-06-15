package com.dell.it.dsc.mtrc.lifecycle.thread;

import java.util.*;
import java.util.concurrent.*;

public class ExecutorServiceInvokeAll {
    public static void main(String[] args) throws InterruptedException {
        Long st = System.currentTimeMillis();
        for (int i = 10; i < 20; i++) {
            Map<String, String> value = callMe(i);
            value.forEach((key, value1) -> System.out.println(value1 + key));
        }
        System.out.println(System.currentTimeMillis() - st);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        List<Map<String, String>> result = new ArrayList<>();
        Set<Callable<Map<String, String>>> callables = new HashSet<>();

        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            callables.add(new Callable<Map<String, String>>() {
                public Map<String, String> call() throws Exception {
                    return callMe(finalI);
                }
            });
        }


        List<Future<Map<String, String>>> futures = executorService.invokeAll(callables);

        boolean isComplete = true;
        while (isComplete) {
            futures.forEach(var -> {
                if (var.isDone()) {
                    try {
                        result.add(var.get());
                        // var.get().forEach((key, value) -> System.out.println(value + key));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (futures.size() == result.size()) isComplete = false;
        }

        System.out.println(System.currentTimeMillis() - startTime);
        executorService.shutdown();
    }

    public static Map<String, String> callMe(int i) throws InterruptedException {
        Thread.sleep(10);
        Map<String, String> articleMapOne = new HashMap<>();
        articleMapOne.put(String.valueOf(i), "Intro to Map");
        articleMapOne.put(String.valueOf(i), "Some article");
        return articleMapOne;

    }

}
