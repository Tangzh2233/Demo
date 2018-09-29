package com.edu.dao.mapper.ideaDemo;

import com.edu.dao.domain.Dlog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
@Repository
public interface DlogMapper {
    List<Dlog> getDlog();
    int addDlog(@Param("pojo") Dlog dlog,@Param("tableName") String tableName);
    int addDlog(Dlog dlog);
}