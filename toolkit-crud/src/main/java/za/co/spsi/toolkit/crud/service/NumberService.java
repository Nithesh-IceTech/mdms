package za.co.spsi.toolkit.crud.service;

import javax.ejb.Singleton;

@Singleton
public class NumberService {

    public String getHexString(Integer length) {

        String value = Long
                .toHexString(Math.round(System.currentTimeMillis() * Math.random() * 100))
                .toUpperCase();
        while (value.length() < length) {
            value += Long
                    .toHexString(Math.round(Math.random() * 100 * System.currentTimeMillis()))
                    .toUpperCase();
        }

        return value.substring(0, length);
    }



}
