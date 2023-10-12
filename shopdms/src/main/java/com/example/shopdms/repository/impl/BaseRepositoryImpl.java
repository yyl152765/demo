package com.example.shopdms.repository.impl;


import com.example.shopdms.entity.Account;
import com.example.shopdms.repository.BaseRepository;
import com.example.shopdms.service.impl.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Repository
public class BaseRepositoryImpl <T,ID extends Serializable> implements BaseRepository<T,ID> {
    static Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public boolean save(T entity) {
        boolean flag=false;
        try {
            entityManager.persist(entity);
            flag=true;
        }catch (Exception e){
            logger.info("---------------保存出错---------------");
            throw e;
        }
        return flag;
    }

    @Override
    public Object findByid(Object t, Object id) {
        return entityManager.find(t.getClass(),id);
    }

    @Override
    public List<T> findBysql(String tablename, String filed, Object o) {
        String sql="from "+tablename+" u WHERE u."+filed+"= ?1";
        logger.info(sql+"--------sql语句-------------");
        Query query=entityManager.createQuery(sql);
        query.setParameter(1,o);
        List<T> list= query.getResultList();
        entityManager.close();
        return list;
    }

    @Override
    public List<T> findall(String tablename) {
        String sql="from "+tablename;
        logger.info(sql+"--------sql语句-------------");
        Query query=entityManager.createQuery(sql);
        List<T> list= query.getResultList();
        entityManager.close();
        return list;
    }

    @Override
    public Object findObjiectBysql(String tablename, String filed, Object o) {
        String sql="from "+tablename+" u WHERE u."+filed+"= ?1";
        logger.info(sql+"--------sql语句-------------");
        Query query=entityManager.createQuery(sql);
        query.setParameter(1,o);

        entityManager.close();
        Account acc=null;
      try{
          acc=  (Account)query.getSingleResult();
      }catch (Exception e){

      }

        return acc;
    }

    @Override
    public List<T> findByMoreFiled(String tablename, LinkedHashMap<String, Object> map) {
        String sql="from "+tablename+" u WHERE ";
        Set<String> set=null;
        set=map.keySet();
        List<String> list=new ArrayList<>(set);
        List<Object> filedlist=new ArrayList<>();
        Integer bj=1;
        for (String filed:list){
            sql+="u."+filed+"?"+bj+" and ";
            filedlist.add(filed);
            bj++;
        }
        sql=sql.substring(0,sql.length()-4);
        logger.info(sql+"--------sql语句-------------");
        Query query=entityManager.createQuery(sql);
        for (int i=0;i<filedlist.size();i++){
            query.setParameter(i+1,map.get(filedlist.get(i)));
        }
        List<T> listRe= query.getResultList();
        entityManager.close();
        return listRe;
    }



/*    @Override
    public List<T> findByMoreFiledpages(String tablename, LinkedHashMap<String, Object> map, int start, int pageNumber) {
        String sql="from "+tablename+" u WHERE ";
        Set<String> set=null;
        set=map.keySet();
        List<String> list=new ArrayList<>(set);
        List<Object> filedlist=new ArrayList<>();
        Integer bj=1;
        for (String filed:list){
            sql+="u."+filed+"=?"+bj+" and ";
            filedlist.add(filed);
            bj++;
        }
        sql=sql.substring(0,sql.length()-4);
        logger.info(sql+"--------sql语句-------------");
        Query query=entityManager.createQuery(sql);
        for (int i=0;i<filedlist.size();i++){
            query.setParameter(i+1,map.get(filedlist.get(i)));
        }
        query.setFirstResult((start-1)*pageNumber);
        query.setMaxResults(pageNumber);
        List<T> listRe= query.getResultList();
        entityManager.close();
        return listRe;
    }*/
@Override
public List<T> findByMoreFiledpages(String tablename, LinkedHashMap<String, Object> map, int start, int pageNumber) {
    String sql="from "+tablename+" u WHERE ";
    Set<String> set=null;
    set=map.keySet();
    List<String> list=new ArrayList<>(set);
    List<Object> filedlist=new ArrayList<>();
    Integer bj=1;
    for (String filed:list){
        sql+="u."+filed+"?"+bj+" and ";
        filedlist.add(filed);
        bj++;
    }
    sql=sql.substring(0,sql.length()-4);
    logger.info(sql+"--------sql语句-------------");
    Query query=entityManager.createQuery(sql);
    for (int i=0;i<filedlist.size();i++){
        query.setParameter(i+1,map.get(filedlist.get(i)));
    }
    query.setFirstResult((start-1)*pageNumber);
    query.setMaxResults(pageNumber);
    List<T> listRe= query.getResultList();
    entityManager.close();
    return listRe;
}


    @Override
    public List<T> findpages(String tablename, String filed, Object o, int start, int pageNumer) {
        String sql="from "+tablename+" u WHERE u."+filed+"=?1";
        logger.info(sql+"--------page--sql语句-------------");
        List<T> list=new ArrayList<>();
        try {
            Query query=entityManager.createQuery(sql);
            query.setParameter(1,o);
            query.setFirstResult((start-1)*pageNumer);
            query.setMaxResults(pageNumer);
            list= query.getResultList();
            entityManager.close();
        }catch (Exception e){
            logger.info("------------分页错误---------------");
        }

        return list;
    }
    @Transactional
    @Override
    public boolean delete(T entity) {
        boolean flag=false;
        try {
            entityManager.remove(entityManager.merge(entity));
            flag=true;
        }catch (Exception e){
            logger.info("---------------删除出错---------------");
        }
        return flag;
    }
    @Transactional
    @Override
    public boolean update(T entity) {
        boolean flag = false;
        try {
            entityManager.merge(entity);
            flag = true;
        } catch (Exception e) {
            logger.info("---------------更新出错---------------");
        }
        return flag;
    }
    @Transactional
    @Override
    public Integer updateMoreFiled(String tablename, LinkedHashMap<String, Object> map,String file,String filename) {
        String sql="UPDATE "+tablename+" AS u SET ";
        Set<String> set=null;
        set=map.keySet();
        List<String> list=new ArrayList<>(set);
        for (int i=0;i<list.size()-1;i++){
            if (map.get(list.get(i)).getClass().getTypeName()=="java.lang.String"){
                logger.info("-*****"+map.get(list.get(i))+"------------"+list.get(i));
                sql+="u."+list.get(i)+"='"+map.get(list.get(i))+"' , ";
            }else {
                sql+="u."+list.get(i)+"="+map.get(list.get(i))+" , ";
            }
        }
        sql=sql.substring(0,sql.length()-2);
        sql+="where u."+file+"=?1 ";
        logger.info(sql+"--------sql语句-------------");
        int resurlt=0;
        try {
            Query query=entityManager.createQuery(sql);
            query.setParameter(1,filename);
            resurlt= query.executeUpdate();
        }catch (Exception e){
            logger.info("更新出错-----------------------");
            e.printStackTrace();

        }
        return resurlt;
    }

    @Override
    public Object findCount(String tablename, LinkedHashMap<String, Object> map) {
        String sql="select count(u) from "+tablename+" u WHERE ";
        Set<String> set=null;
        set=map.keySet();
        List<String> list=new ArrayList<>(set);
        List<Object> filedlist=new ArrayList<>();
        Integer bj=1;
        for (String filed:list){
            sql+="u."+filed+"=?"+bj+" and ";
            filedlist.add(filed);
            bj++;
        }
        sql=sql.substring(0,sql.length()-4);
        logger.info(sql+"--------sql语句-------------");
        Query query=entityManager.createQuery(sql);
        for (int i=0;i<filedlist.size();i++){
            query.setParameter(i+1,map.get(filedlist.get(i)));
        }
        return query.getSingleResult();
    }

    @Override
    public Object findCount2(String tablename, LinkedHashMap<String, Object> map) {
        String sql="select count(u) from "+tablename+" u WHERE ";
        Set<String> set=null;
        set=map.keySet();
        List<String> list=new ArrayList<>(set);
        List<Object> filedlist=new ArrayList<>();
        Integer bj=1;
        for (String filed:list){
            sql+="u."+filed+"?"+bj+" and ";
            filedlist.add(filed);
            bj++;
        }
        sql=sql.substring(0,sql.length()-4);
        logger.info(sql+"--------sql语句-------------");
        Query query=entityManager.createQuery(sql);
        for (int i=0;i<filedlist.size();i++){
            query.setParameter(i+1,map.get(filedlist.get(i)));
        }
        return query.getSingleResult();
    }

}
