package za.co.spsi.uaa.util.dto;

/**
 * Created by jaspervdb on 2016/07/14.
 */
public class Authority {
    private String authority;

    public Authority() {}

    public Authority(String authority) {
        setAuthority(authority);
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
