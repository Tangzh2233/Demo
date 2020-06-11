package com.edu.JavaLearning.算法;

import lombok.Data;
import org.apache.ibatis.cache.Cache;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/12 9:29 PM
 * 并行调用
 **/
public class ConcurrentInvoke {

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);
    private static int DEFAULT_TRY_COUNT = 3;

    private DataSource dataSourceA;
    private DataSource dataSourceB;
    private DataSource dataSourceC;
    private DataSource dataSourceD;

    public static void main(String[] args) {
//        ConcurrentInvoke invoker = new ConcurrentInvoke();
//        invoker.concurrentInvoke(new Request());
        for (Field field : ConcurrentInvoke.class.getDeclaredFields()) {
            System.out.println(field.getName());
        }

    }

    public List<String> concurrentInvoke(Request request) {
        if (request.getFlag() == 0) {
            return null;
        }

        LinkedList<Future<List<String>>> futureList = new LinkedList<>();
        List<String> result = new ArrayList<>();

        /**
         * 设置映射,数据源新增只需要添加相应映射
         */
//        Set<String> dataSourceKeys = DataSourceSelector.getDataSourceKeys(request);
//        dataSourceKeys.forEach(item -> {
//            DataSource dataSource = DataSourceFactory.getDataSource(item);
//            futureList.add(executorService.submit(new InvokeTask(dataSource,DEFAULT_TRY_COUNT,filed)));
//        });
        //获取数据源标识
        int flag = request.getFlag();
        for (int i = 0; i < 4; i++) {
            //存在数据源字段则新建查询任务
            if ((flag & (1 << i)) > 0) {
                DataSource dataSource;
                Filed filed;
                String key;
                switch (i) {
                    case 0:
                        key = "dataSourceA";
                        filed = request.getFieldA();
                        break;
                    case 1:
                        key = "dataSourceB";
                        filed = request.getFieldB();
                        break;
                    case 2:
                        key = "dataSourceC";
                        filed = request.getFieldC();
                        break;
                    case 3:
                        key = "dataSourceD";
                        filed = request.getFieldD();
                        break;
                    default:
                        throw new IllegalArgumentException("参数校验失败");
                }
                dataSource = DataSourceFactory.getDataSource(key);
                futureList.add(executorService.submit(new InvokeTask(dataSource,DEFAULT_TRY_COUNT,filed)));
            }
        }

        while (!futureList.isEmpty()) {
            Future<List<String>> future = futureList.peek();
            try {
                //尝试一秒钟,依旧没有获取到则跳过
                List<String> invokeData = future.get(1000, TimeUnit.MILLISECONDS);
                result.addAll(invokeData);
                futureList.remove(future);
            } catch (Exception e) {
                //1s未获取数据则移除任务
                futureList.remove(future);
                future.cancel(true);
            }
        }

        return result;
    }


    class InvokeTask implements Callable<List<String>> {

        private DataSource dataSource;
        private int tryCount;
        private Filed filed;

        public InvokeTask(DataSource dataSource, int tryCount, Filed filed) {
            this.dataSource = dataSource;
            this.tryCount = tryCount;
            this.filed = filed;
        }

        /**
         * 备注: 此处调用的需要有时间限制
         * 默认重试3次
         * @return
         * @throws Exception
         */
        @Override
        public List<String> call() throws Exception {

            List<String> invoke = null;
            for (int i = 0; i < tryCount; i++) {
                try {
                    invoke = dataSource.invoke(filed);
                    //不一定为空,判断此业务调用成功是否调用
                    if (invoke != null) {
                        break;
                    }
                } catch (Exception e) {
                    //超时异常,或者其他异常重试
                }
            }
            return invoke;
        }
    }
}

/**
 * 请求参数field -> dataSourceKey 的映射
 */
class DataSourceSelector{

    private static HashMap<Filed,String> flagMap = new HashMap<>();

    public static Set<String> getDataSourceKeys(Request request) {
        Set<String> keySet = new HashSet<>();
        for (Filed item : flagMap.keySet()) {
            if (item.contains(request.getFiledList())) {
                keySet.add(flagMap.get(item));
            }
        }
        return keySet;
    }

}

/**
 * 维护key->dataSource的映射
 */
class DataSourceFactory{

    private static HashMap<String,DataSource> dataSourceHashMap = new HashMap<>();

    static {

    }

    public static DataSource getDataSource(String key) {
        return dataSourceHashMap.get(key);
    }

    public static DataSource getDataSource(String key, DataSource dataSource) {
        return dataSourceHashMap.put(key, dataSource);
    }
}


interface DataSource {
    List<String> invoke(Filed filed);
}

/**
 * 对于查询,可以通过Redis做二级缓存,增加查询效率
 */
class DataSoureA implements DataSource, Cache {


    @Override
    public List<String> invoke(Filed filed) {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void putObject(Object key, Object value) {

    }

    @Override
    public Object getObject(Object key) {
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}

class DataSoureB implements DataSource {

    @Override
    public List<String> invoke(Filed filed) {
        return null;
    }
}

class DataSoureC implements DataSource {

    @Override
    public List<String> invoke(Filed filed) {
        return null;
    }
}

class DataSoureD implements DataSource {

    @Override
    public List<String> invoke(Filed filed) {
        return null;
    }
}


class Request {

    /**
     * 二进制表示
     * 0001 => dataSourceA
     * 0010 => dataSourceB
     * 0100 => dataSourceC
     * 1000 => dataSourceD
     * 分别对应多数据源
     */
    private int flag;

    private FieldA fieldA;

    private FieldB fieldB;

    private FieldC fieldC;

    private FieldD fieldD;

    public List<String> getFiledList(){
        return null;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public FieldA getFieldA() {
        return fieldA;
    }

    public void setFieldA(FieldA fieldA) {
        this.fieldA = fieldA;
    }

    public FieldB getFieldB() {
        return fieldB;
    }

    public void setFieldB(FieldB fieldB) {
        this.fieldB = fieldB;
    }

    public FieldC getFieldC() {
        return fieldC;
    }

    public void setFieldC(FieldC fieldC) {
        this.fieldC = fieldC;
    }

    public FieldD getFieldD() {
        return fieldD;
    }

    public void setFieldD(FieldD fieldD) {
        this.fieldD = fieldD;
    }
}

interface Filed{

    boolean contains(List<String> fields);
}

@Data
class FieldA implements Filed{

    private static Set<String> filedList = new HashSet<>();

    static {
        for(Field field : FieldA.class.getDeclaredFields()){
            filedList.add(field.getName());
        }
    }

    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;

    public boolean contains(List<String> fields) {
        for (String item : fields) {
            if (filedList.contains(item)) {
                return true;
            }
        }
        return false;
    }
}

@Data
class FieldB implements Filed{
    private String field6;
    private String field7;
    private String field8;
    private String field9;
    private String field10;

    @Override
    public boolean contains(List<String> fields) {
        return false;
    }
}

@Data
class FieldC implements Filed{
    private String field11;
    private String field12;
    private String field13;
    private String field14;
    private String field15;

    @Override
    public boolean contains(List<String> fields) {
        return false;
    }
}

@Data
class FieldD implements Filed{
    private String field16;
    private String field17;
    private String field18;
    private String field19;
    private String field20;

    @Override
    public boolean contains(List<String> fields) {
        return false;
    }
}

