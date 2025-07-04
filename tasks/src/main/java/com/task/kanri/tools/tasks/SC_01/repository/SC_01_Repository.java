package com.task.kanri.tools.tasks.SC_01.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.task.kanri.tools.tasks.SC_01.bean.SC_01_TorihikisakiSearchResult;

@Mapper
public interface SC_01_Repository {

    /**
     * 取引先一覧を取得
     */
    List<SC_01_TorihikisakiSearchResult> findAll();

}
