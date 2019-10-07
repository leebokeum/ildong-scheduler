package com.ildong.scheduler.mapper;

import com.ildong.scheduler.domain.EtcMenuTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenuRepository {
    public EtcMenuTable test(@Param("id") String id) throws Exception;
}
