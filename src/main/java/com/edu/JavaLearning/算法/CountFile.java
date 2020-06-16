package com.edu.JavaLearning.算法;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.transform.AbstractClassFilterTransformer;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/20 6:17 PM
 * 给定一个目录，目录可能有多层子目录，各层目录中，可能有java代码文件，以及其他文件（比如，xml文件）。
 * 要求统计目录中，所有java代码文件的总行数，总注释行数，空行数。
 *
 * 线程池核心线程数设置依据
 * NThreads = NCpu * UCpu * (1+w/c)
 * NCpu:cpu个数
 * UCpu:cpu使用率 0～1
 * w/c = 等待时间 / 计算时间
 *
 * 计算密集型: NThreads = NCpu 即 需要占用cpu计算,cpu使用率很高,认为UCpu = 1 w/c中c无限大。结论就是 NCpu * 1 * 1
 * IO密集型: NThreads = 2*NCpu 需要 > NCpu. 因为若 = NCpu,IO阻塞cpu应该需要切换到到其他线程执行,此时线程数等于NCpu会导致cpu无线程可调度
 * 但是线程数过多又会有内存限制。所以保守估计一般给 2*NCpu
 **/
public class CountFile {

    private static ExecutorService executorService;
    private static BlockingQueue<String> queue = new LinkedBlockingDeque<>();
    private static volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {

        int nCpu = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(nCpu * 2, new ThreadFactory() {

            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"FileReadTask_" + this.index.incrementAndGet());
            }
        });

        File file = new File("/Users/tangzh/git-space/Demo/src/main/java/com/edu/JavaLearning");

        long begin = System.currentTimeMillis();
        LinkedList<Future<Map<String, Integer>>> futureLinkedList = new LinkedList<>();
        List<Map<String, Integer>> result = new ArrayList<>();

        Thread consumer = new Thread(() -> {

            //未生产完或者未消费完的,继续循环
            while ((flag || !queue.isEmpty())) {
                String fileName = queue.poll();
                System.out.println("消费 " + fileName);
                if (StringUtils.isNotBlank(fileName)) {
                    Future<Map<String, Integer>> future = executorService.submit(new FileReadTask(fileName));
                    futureLinkedList.add(future);
                }
            }
        });

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                listFile(file);
                //设置生产完毕标识
                flag = false;
            }

            //文件遍历添加消费元素
            private void listFile(File file) {
                //文件过滤
                File[] files = file.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(".java"));
                if (files == null) {
                    return;
                }
                for (File item : files) {
                    if (item.isDirectory()) {
                        listFile(item);
                    }
                    //添加文件
                    if (item.isFile()) {
                        queue.add(item.getAbsolutePath());
                    }
                }
            }
        });
        producer.start();
        consumer.start();

        //Task全部添加以后才能进行取值操作
        consumer.join();

        while (!futureLinkedList.isEmpty()) {
            Future<Map<String, Integer>> future = futureLinkedList.peek();
            try {
                Map<String, Integer> map = future.get(1000, TimeUnit.MILLISECONDS);
                result.add(map);
                futureLinkedList.removeFirst();
            } catch (Exception e) {
            }
        }

        //包装分组求和
        Set<Node> entrySet = new HashSet<>();
        result.forEach(item -> item.forEach((key, value) -> entrySet.add(new Node(key, value))));
        Map<String, Integer> map = entrySet.stream()
                .collect(Collectors.groupingBy(Node::getKey, Collectors.summingInt(Node::getCount)));

        System.out.println(map.toString());

        System.out.println("耗时: "+ (System.currentTimeMillis() - begin) + "ms");
        executorService.shutdown();
    }

}

class FileReadTask implements Callable<Map<String, Integer>> {

    private String fileName;

    public FileReadTask(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        Map<String, Integer> returnData = new HashMap<>();
        int a = 0, n = 0, s = 0;

        System.out.println(Thread.currentThread().getName() + "读取: " + fileName);

        FileReader reader = new FileReader(fileName);
        BufferedReader buffer = new BufferedReader(reader);
        String line;
        while ((line = buffer.readLine()) != null) {
            a++;
            if (StringUtils.isBlank(line)) {
                s++;
                continue;
            }
            if ((line = line.trim()).startsWith("/*") || line.startsWith("//") || line.startsWith("*")) {
                n++;
                continue;
            }
        }
        buffer.close();
        reader.close();
        returnData.put("总行数", a);
        returnData.put("注释行数", n);
        returnData.put("空行", s);
        return returnData;
    }

}

class Node {
    private String key;
    private Integer count;

    public Node(String key, Integer count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
