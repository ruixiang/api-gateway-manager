package org.itachi.api.gateway.manage.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:07
 *
 * @author itachi
 */
@Entity
@Table(name = "app_service", indexes = {@Index(name = "app_name_idx", columnList = "app_name", unique = true)})
@Data
@NoArgsConstructor
public class Service implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "app_name", nullable = false, unique = true)
    private String appName;
    private String level;
    private Integer encryption;
    private Integer needHeaders;
    private String type;
    private Integer readTimeOut;
    private Integer connectTimeOut;
    private Integer requestTimeOut;
    private String url;
    private String descriptionCn;
    private String descriptionEn;
    private String remark;
    private Integer verification;
    private String serviceUri;
    private String token;
    private String tenantId;
    private String accountId;
}
