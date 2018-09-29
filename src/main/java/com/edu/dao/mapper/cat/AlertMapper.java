package com.edu.dao.mapper.cat;

import com.edu.dao.domain.Alert;
import com.edu.dao.domain.CategoryDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Tangzhihao
 * @date 2018/3/19
 */
@Repository
public interface AlertMapper {
    List<Alert> selectAlertByDomainAndDate(@Param("domain") String domain, @Param("alertTime") String alertTime);
    Set<String> selectDomainByAlertTime(@Param("alertTime") String alertTime);
    int countAlertByDomainAndDate(@Param("domain") String domain, @Param("alertTime") String alertTime);
    List<CategoryDto> getCategory(@Param("domain") String domain, @Param("alertTime")String alertTime);
}
