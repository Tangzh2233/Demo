package com.edu.dao.mapper.cat;

import com.edu.dao.domain.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tangzhihao
 * @date 2018/3/20
 */
@Repository
public interface ProjectMapper {

    List<Project> selectEmailByDomain(@Param("domain") String domain);
}
