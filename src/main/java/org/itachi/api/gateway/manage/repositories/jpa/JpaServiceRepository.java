package org.itachi.api.gateway.manage.repositories.jpa;

import org.itachi.api.gateway.manage.domain.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:25
 *
 * @author itachi
 */
@Repository
@Profile({"in-memory", "mysql", "postgres", "oracle", "sqlserver"})
public interface JpaServiceRepository extends JpaRepository<Service, Long> {
    /**
     * 根据应用名获取记录数
     * @param appName 应用名
     * @return 记录数
     */
    int countByAppName(String appName);

    /**
     * 根据应用名查询分页数据
     * @param appName 应用名
     * @param pageable 分页参数
     * @return 应用分页数据
     */
    Page<Service> findByAppName(String appName, Pageable pageable);

    /**
     * 根据id批量删除应用数据
     * @param ids 应用id集合
     */
    void deleteByIdIn(Collection<Long> ids);
}
