package org.itachi.api.gateway.manage.error;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by itachi on 2017/3/18.
 * User: itachi
 * Date: 2017/3/18
 * Time: 10:56
 * @author itachi
 */
public abstract class ServiceError {
    protected ServiceError() {}

    public  interface ErrorType {
        /**
         * 错误代码
         * @return 错误代码
         */
        int getCode();

        /**
         * 错误原因信息
         * @return 错误信息
         */
        String getReasonPhrase();
    }

    public enum Error implements ErrorType {
        /**
         * 非法请求
         */
        ILLEGAL_REQUEST(101, "非法请求"),
        /**
         * 不支持的数据类型
         */
        UN_SUPPORT_MEDIATYPE(102, "不支持的数据类型"),
        /**
         * 远程服务错误
         */
        REMOTE_SERVICE_ERROR(103, "远程服务错误"),
        /**
         * 参数错误
         */
        PARAMETER_INVALID(104, "参数错误"),
        /**
         * 非法的用户名
         */
        ACCOUNT_INVALID(105, "非法的用户名"),
        /**
         * 非法的密码
         */
        PASSWORD_INVALID(106, "非法的密码"),
        /**
         * 用户名或密码错误
         */
        USER_NOT_EXISTS(107, "用户名或密码错误"),
        /**
         * Id为空!
         */
        ID_EMPTY(108, "Id为空!"),
        /**
         * 账号为空!
         */
        ACCOUNT_EMPTY(109, "账号为空!"),
        MAIL_EMPTY(110, "邮箱为空!"),
        MODIFY_FAILED(111,"修改失败!"),
        NAME_EMPTY(112,"名字为空"),
        PHONE_EMPTY(113,"手机号为空"),
        DEPARTMENT_EMPTY(114,"部门为空"),
        PASSWORD_EMPTY(115,"密码为空"),
        GROUP_EMPTY(116,"组信息请至少选择一个"),
        ADD_OR_MODIFY_GROUP_FAILURE(201,"用户组添加或者修改失败"),
        AUTHORITY_GROUP_FAILURE(202,"部分权限添加失败，请补充添加权限。"),
        DEL_GROUP_FAILURE(203,"用户组删除失败"),
        USER_EXISTS(204,"用户已经存在"),
        SERVICE_ID_EMPTY(301,"待删除的服务id集合为空!"),
        SERVICE_IS_EXISTS(302,"该服务已经存在!"),
        SERVICE_OBJECT_ERROR(303,"错误的服务对象数据!"),
        APP_NAME_NULL(304,"服务应用名称不能为空!"),
        SERVICE_NAME_ERROR(305,"服务应用名称由大小写英文字母，数字，-，_组成!"),
        SERVICE_URL_NULL(306,"服务地址不能为空!"),
        SERVICE_ID_ERROR(307,"服务ID非法!"),
        CACHE_ID_EMPTY(401,"待删除的缓存规则id集合为空!"),
        CACHE_OBJECT_ERROR(403,"错误的缓存规则对象数据!"),
        CACHE_PATH_NULL(406,"缓存规则不能为空!"),
        CACHE_ID_ERROR(407,"服务ID非法!"),
        CLIENT_NOT_REGISTERED(1009, "客户端版本未注册。"),
        SERVICE_NAME_NULL(1010, "服务应用名称为空。"),
        API_PATH_EMPTY(1011, "请求api路径不能为空。"),
        SERVICE_NOT_REGISTERED(1003, "服务应用未注册。应用名=[{0}]"),
        NO_AUTHORIZATION_PARAMETER(1013, "无token参数。"),
        TOKEN_ERROR(1014, "token验证不通过。"),
        NO_SUPPORT_METHOD(1015, "目前此类请求不支持此种method。"),
        FILE_UPLOAD_CONTENT_TYPE_ERROR(1016, "上传文件的请求content-type必须是multipart/form-data。"),
        FILE_NOT_EXISTS(1017, "缓存文件不存在。"),
        REQUEST_DATA_ERROR(2040, "请求数据有误。"),
        OAUTH_URI_EMPTY(1110, "认证uri没有配置。"),
        SETTINGS_URI_EMPTY(1111, "租户信息uri没有配置。"),
        /**
         * 未知错误
         */
        UNKNOWN(999, "未知错误");


        private final int code;
        private final String reason;

        Error(final int errorCode, final String reasonPhrase) {
            code = errorCode;
            reason = reasonPhrase;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getReasonPhrase() {
            return reason;
        }

        @Override
        public String toString() {
            return reason;
        }

        public static Error fromErrorCode(final int errorCode) {
            Optional<Error> optional = Stream.of(Error.values()).filter((Error error) -> error.code == errorCode).findFirst();
            return optional.orElse(UNKNOWN);
        }
    }
}
