package com.wangzhu.controller;

import com.wangzhu.service.impl.IHello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Created by wang.zhu on 2020-05-22 20:23.
 **/
@Slf4j
@RestController
public class HelloController {

    private ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> null);

    @Autowired
    private IHello hello;

    @RequestMapping("/b")
    public String index() {
        hello.print();
        return "b_" + System.currentTimeMillis();
    }

    @RequestMapping("/a")
    public String aa() {
        return "a_" + System.currentTimeMillis();
    }

    @GetMapping("/get")
    public Map<String, Object> get(@RequestParam("id") Integer id) {
        final Map<String, Object> ret = new HashMap<>();
        try {
            final String before = Thread.currentThread().getName() + ":" + threadLocal.get();
            threadLocal.set(id);
            final String after = Thread.currentThread().getName() + ":" + threadLocal.get();
            ret.put("before", before);
            ret.put("after", after);
        } finally {
            threadLocal.remove();
        }
        return ret;
    }


    private static int thread_count = 10;
    private static int item_count = 1000;

    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new));
    }


    @GetMapping("/getData")
    public String getData() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(item_count - 100);
        log.info("init size:{}", concurrentHashMap.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool(thread_count);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, thread_count).parallel().forEach(i -> {
            synchronized (concurrentHashMap) {
                int gap = item_count - concurrentHashMap.size();
                log.info("gap size:{}", gap);
                concurrentHashMap.putAll(getData(gap));
            }
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        log.info("finish size:{}", concurrentHashMap.size());
        return "ok";
    }


    private static int loop_count = 10000000;

    private Map<String, Long> normaluse() throws InterruptedException {
        ConcurrentHashMap<String, Long> freqs = new ConcurrentHashMap<>(item_count);
        ForkJoinPool forkJoinPool = new ForkJoinPool(thread_count);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, loop_count).parallel().forEach(i -> {
            String key = "item" + ThreadLocalRandom.current().nextInt(item_count);
            synchronized (freqs) {
                if (freqs.containsKey(key)) {
                    freqs.put(key, freqs.get(key) + 1);
                } else {
                    freqs.put(key, 1L);
                }
            }
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs;
    }

    private Map<String, Long> gooduse() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> freqs = new ConcurrentHashMap<>(item_count);
        ForkJoinPool forkJoinPool = new ForkJoinPool(thread_count);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, loop_count).parallel().forEach(i -> {
            String key = "item" + ThreadLocalRandom.current().nextInt(item_count);
            freqs.computeIfAbsent(key, k -> {
                log.info("key|{}|i|{}", key, i);
                return new LongAdder();
            }).increment();
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().longValue()));
    }

    @GetMapping("/getDataV2")
    public String getDataV2() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("normaluse");
        Map<String, Long> normaluse = normaluse();
        stopWatch.stop();
        Assert.isTrue(normaluse.size() == item_count, "normaluse size error");
        Assert.isTrue(normaluse.entrySet().stream().mapToLong(item -> item.getValue()).reduce(0, Long::sum) == loop_count, "normaluse count error");

        stopWatch.start("gooduse");
        Map<String, Long> gooduse = gooduse();
        stopWatch.stop();
        Assert.isTrue(gooduse.size() == item_count, "gooduse size error");
        Assert.isTrue(gooduse.entrySet().stream().mapToLong(item -> item.getValue()).reduce(0, Long::sum) == loop_count, "gooduse count error");


        log.info(stopWatch.prettyPrint());
        return "ok";
    }

}
