package org.itachi.api.gateway.manage.domain;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:21
 *
 * @author itachi
 */
public class ApplicationInfo {
    private String[] profiles;
    private String[] services;

    public ApplicationInfo(String[] profiles, String[] services) {
        this.profiles = profiles;
        this.services = services;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public void setProfiles(String[] profiles) {
        this.profiles = profiles;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }
}
