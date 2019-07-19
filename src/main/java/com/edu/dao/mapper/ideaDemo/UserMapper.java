package com.edu.dao.mapper.ideaDemo;
import com.edu.dao.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 */
@Repository
public interface UserMapper {
    User getUserByName(@Param("name") String name);

    List<String> getAllKeys();

    int insertUserList(@Param("pojos") List<User> pojos);
}