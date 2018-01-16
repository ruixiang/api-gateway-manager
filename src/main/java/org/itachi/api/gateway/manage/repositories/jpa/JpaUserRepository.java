package org.itachi.api.gateway.manage.repositories.jpa;

import org.itachi.api.gateway.manage.domain.AdminUser;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by itachi on 2018/1/16.
 * User: itachi
 * Date: 2018/1/16
 * Time: 15:17
 *
 * @author itachi
 */
@Repository
@Profile({"in-memory", "mysql", "postgres", "oracle", "sqlserver"})
public interface JpaUserRepository extends JpaRepository<AdminUser, Long> {
    /**
     * 根据用户名密码获取管理员用户对象
     * @param name 用户名
     * @param password 密码
     * @return 管理员对象
     */
    AdminUser findByUserNameAndPassword(String name, String password);
}
