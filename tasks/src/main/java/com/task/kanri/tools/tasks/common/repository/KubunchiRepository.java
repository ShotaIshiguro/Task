package com.task.kanri.tools.tasks.common.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.task.kanri.tools.tasks.common.bean.Kubunchi;

@Mapper
public interface KubunchiRepository {
    Kubunchi findByKubunCdAndKubunchiCd(@Param("kubunCd") String kubunCd, @Param("kubunchiCd") String kubunchiCd);
}
