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
@Mapper
public interface UserMapper {
    User getUserByName(@Param("name") String name);

    List<User> getUserByName2(@Param("name") String name);

    int updateUserByName(@Param("name") String name,@Param("num")int num);

    List<String> getAllKeys(@Param("params") List<String> params);

    int insertUserList(@Param("pojos") List<User> pojos);

    List<User> getAll();

    int updateByUserNo(@Param("userNo")String userNo,@Param("num")int num);

    User queryUserByUserNo(@Param("userNo")String userNo);

}
