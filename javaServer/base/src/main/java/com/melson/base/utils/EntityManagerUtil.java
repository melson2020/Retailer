package com.melson.base.utils;

import com.melson.base.cache.CacheKey;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/9/10
 */
@Service
public class EntityManagerUtil {
    @PersistenceContext
    private EntityManager entityManager;

    private Connection GetConnection() {
        return  entityManager.unwrap(SessionImpl.class).connection();
    }

    public EntityManagerExcuteRs BatchUpdateSql(List<String> sqlList) {
        Connection connection = GetConnection();
        EntityManagerExcuteRs rs=new EntityManagerExcuteRs();
        try {
            Statement statement = connection.createStatement();
            for (String sql : sqlList) {
                statement.addBatch(sql);
            }
            int[] res= statement.executeBatch();
            List<Integer> falseList=new ArrayList<>();
            boolean success=true;
            for (int i=0;i<res.length;i++){
                if(res[i]==0){
                    success=false;
                    falseList.add(i);
                }
            }
            if(success){
                rs.setStatus(1);
                rs.setMessage("success");
            }else {
                rs.setStatus(2);
                rs.setMessage("error:数量不匹配");
                rs.setFalseIndexs(falseList);
            }
        } catch (Exception e) {
            rs.setStatus(3);
            rs.setMessage("Exception:"+e.getMessage());
        }finally {
            return rs;
        }
    }

    public List<Object[]> ExcuteSql(String sql) {
        Query query=entityManager.createNativeQuery(sql);
        List<Object[]> list= query.getResultList();
        return list;
    }
}
