package com.edu.JavaLearning.算法;

import com.google.common.io.PatternFilenameFilter;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/3/30 11:58 PM
 * 请用JAVA代码完成以下功能，要求使用多线程
 * cat /home/admin/logs/*.log | grep "Login" | uniq -c | sort -nr
 **/
public class CatGrepFileForJava {

    private static ExecutorService executorService = Executors.newCachedThreadPool();


    public static void main(String[] args) {

        List<String> data = new ArrayList<>();
        CompletionService<List<String>> completionService = new ExecutorCompletionService<>(executorService);
        LinkedList<Future<List<String>>> result = new LinkedList<>();

        //查询文件数量及名称
        List<String> fileList = findFile("/home/admin/logs", "\\S*.log");

        for (int i = 0; i < fileList.size(); i++) {
            Future<List<String>> submit = completionService.submit(new FileReaderTask(fileList.get(i), "Login"));
            result.add(submit);
        }
        //但文件数据获取
        while (!result.isEmpty()) {
            try {
                List<String> strings = result.peek().get();
                data.addAll(strings);
                result.removeFirst();
            } catch (Exception e) {
            }
        }

        //自然排序
        data = data.stream().sorted().collect(Collectors.toList());

        //分组求和
        Map<String, Long> stringLongMap = data.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));


        //依据Map.value排序
        List<Map.Entry<String, Long>> collect = stringLongMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toList());

        collect.forEach(item -> System.out.println(item.getValue() + " " + item.getKey()));

//        //依据求和值排序,包装类
//        List<Line> lineList = new ArrayList<>();
//        stringLongMap.keySet()
//                .forEach(item -> lineList.add(new Line(item, Math.toIntExact(stringLongMap.get(item)))));
//
//        lineList.sort(Comparator.comparing(Line::getCount));
//
//        lineList.forEach(item -> System.out.println(item.getCount() + " " + item.getLine()));
        executorService.shutdown();
    }

    private static List<String> findFile(String path, String fileName) {
        List<String> returnData = new ArrayList<>();

        File file = new File(path);
        FilenameFilter filenameFilter = new PatternFilenameFilter(fileName);
        String[] list = file.list(filenameFilter);
        if (list == null) {
            return returnData;
        }

        for (String name : list) {
            returnData.add(path + "/" + name);
        }
        return returnData;
    }

//    public static class MyFileNameFilter implements FilenameFilter{
//
//        private String endWith;
//
//        public MyFileNameFilter(String endWith) {
//            this.endWith = endWith;
//        }
//
//        @Override
//        public boolean accept(File dir, String name) {
//            return name.endsWith(endWith);
//        }
//    }

    public static class Item {
        private String fileName;

        public Item(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    public static class Line {
        private String line;
        private Integer count;

        public Line() {
        }

        public Line(String line, Integer count) {
            this.line = line;
            this.count = count;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }


    public static class FileReaderTask implements Callable<List<String>> {
        private String fileName;
        private String filter;

        FileReaderTask(String fileName, String filter) {
            this.fileName = fileName;
            this.filter = filter;
        }

        @Override
        public List<String> call() {
            List<String> fileContent = new ArrayList<>();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    //只保留有效信息,防止内存过大
                    if (line.contains(filter)) {
                        fileContent.add(line);
                    }
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fileContent;
        }
    }

}
