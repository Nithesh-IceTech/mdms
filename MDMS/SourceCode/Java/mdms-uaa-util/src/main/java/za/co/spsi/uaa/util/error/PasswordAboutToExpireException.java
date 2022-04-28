package za.co.spsi.uaa.util.error;


import za.co.spsi.uaa.util.dto.TokenResponseDao;

/**
 * Created by jaspervdb on 2016/05/31.
 */
public class PasswordAboutToExpireException extends UAException {

    private int daysBeforeExpiry = 0;
    private TokenResponseDao tokenResponseDao;

    public PasswordAboutToExpireException(String msg) {
        super(msg);
        init("password_about_to_expire", "PASS_TO_EXPIRE", "Password about to expire", 200);
    }

    public PasswordAboutToExpireException() {
        this("");
    }

    public PasswordAboutToExpireException(TokenResponseDao tokenResponseDao, int daysBeforeExpiry) {
        this();
        this.daysBeforeExpiry = daysBeforeExpiry;
        this.tokenResponseDao = tokenResponseDao;
    }

    public int getDaysBeforeExpiry() {
        return daysBeforeExpiry;
    }

    public void setDaysBeforeExpiry(int daysBeforeExpiry) {
        this.daysBeforeExpiry = daysBeforeExpiry;
    }

    public TokenResponseDao getTokenResponseDao() {
        return tokenResponseDao;
    }

    public void setTokenResponseDao(TokenResponseDao tokenResponseDao) {
        this.tokenResponseDao = tokenResponseDao;
    }
}
