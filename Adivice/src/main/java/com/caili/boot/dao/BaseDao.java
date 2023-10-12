package com.caili.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BaseDao<T> {
    int save(T entity);// 保存

    int saveList(List<T> entity);// 保存

    int delete(T entity);// 删除

    int update(T entity);// 更新

    T findById(Integer id);// 根据主键查找

    Map<String,String>  findByEntity(Map<String, String> map); //根据类查询

    List<Map<String,String>> getAll(Map<String, String> map);// 查看所有

    Integer getTotalCount();

    Integer getCountByStr(Map<String, String> map);

    List<Map<String,String>> list(Map<String, String> map);
}
