package za.co.spsi.uaa.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import za.co.spsi.pjtk.io.IOUtil;
import za.co.spsi.uaa.util.dto.*;
import za.co.spsi.uaa.util.error.BadRequestException;
import za.co.spsi.uaa.util.error.InvalidTokenException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static za.co.spsi.uaa.util.Constants.COMPRESSED_PREFIX;

public abstract class BaseUAHelper {

    public static final long MILLIS_PER_DAY = 86400000;

    /**
     * decode into client and password
     *
     * @param auth
     * @return
     */
    public static String[] decodeBasicAuth(String auth) {
        try {
            auth = auth.toLowerCase().startsWith("basic ") ? auth.substring("Basic ".length()) : auth;
            auth = auth.toLowerCase().startsWith("bearer ") ? auth.substring("Bearer ".length()) : auth;
            return new String(Base64.getDecoder().decode(auth)).split(":");
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
    
    public static boolean isValidAgencyForUser(Integer agencyId, String token){
        AgencyRoleMap agencyRoleMap = getAgencyRoleMapFromToken(token);
        if (!agencyRoleMap.hasAgency(agencyId)){
            return false;
        }
        return true;
    }
    
    public static boolean isPasswordDueToExpire(Attributes attributes) {
        return attributes != null && attributes.getPasswordExpiryWarningTime() != null &&
                attributes.getPasswordExpiryWarningTime() < System.currentTimeMillis();
    }

    public static int getDaysBeforeExpiry(Attributes attributes) {
        return (int) (attributes != null && attributes.getPasswordExpiryTime() != null ?
                (attributes.getPasswordExpiryTime() - System.currentTimeMillis()) / MILLIS_PER_DAY : -1);
    }

    @SneakyThrows
    public static String compressRoles(List<String> roles) {
        return Base64.getEncoder().encodeToString(
                roles.isEmpty()
                        ? new byte[]{}
                        : (IOUtil.zip(roles.stream().reduce((s1, s2) -> s1 + "$" + s2).get())));
    }

    public static List<String> decompressRoles(String base64) {
        try {
            return Arrays.asList(new String(IOUtil.unzip(Base64.getDecoder().decode(base64))).split("\\$"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isAboutToExpire(String token) {
        JWTokenClaimDao jwt = getJwTokenClaimDaoStaticIgnoreExpiration(token);
        return new Date(jwt.getExp()*1000L).compareTo(new Date(System.currentTimeMillis() + 1000*60 )) < 0;
    }

    public JWTokenClaimDao getJwTokenClaimDao(String token) {
        return getJwTokenClaimDaoStatic(token);
    }

    public static Jwt parseJwt(JwtParser parser, String token) {
        try {
            return parser.parse(token);
        } catch (Exception ex) {
            throw new InvalidTokenException("Invalid Token: " + ex.getMessage());
        }
    }

    public static JWTokenClaimDao getJwTokenClaimDaoStatic(String token) {
        Jwt jwt = parseJwt(Jwts.parser().setSigningKey(Constants.PUBLIC_KEY), token);
        return new JWTokenClaimDao((Claims) jwt.getBody());
    }

    public static JWTokenClaimDao getJwTokenClaimDaoStaticIgnoreExpiration(String token) {
        Jwt jwt = parseJwt(Jwts.parser().setAllowedClockSkewSeconds(TimeUnit.DAYS.toSeconds(30)).setSigningKey(Constants.PUBLIC_KEY), token);
        return new JWTokenClaimDao((Claims) jwt.getBody());
    }

    public static List<String> decodeRole(String authStr) {
        return authStr.startsWith(COMPRESSED_PREFIX) ? BaseUAHelper.decompressRoles(
                authStr.substring(COMPRESSED_PREFIX.length())) : Arrays.asList(authStr);
    }

    /**
     * get the user agency role map for token
     *
     * @param accessToken
     * @return
     */
    public static AgencyRoleMap getAgencyRoleMapFromToken(String accessToken) {
        AgencyRoleMap agencyRoleMap = new AgencyRoleMap();
        JWTokenClaimDao jwTokenClaimDao = getJwTokenClaimDaoStatic(accessToken);
        if (jwTokenClaimDao.getAuthorities() != null) {
            for (String authStr : jwTokenClaimDao.getAuthorities()) {
                List<String> authList = authStr.startsWith(COMPRESSED_PREFIX) ? BaseUAHelper.decompressRoles(
                        authStr.substring(COMPRESSED_PREFIX.length())) : Arrays.asList(authStr);
                for (String auth : authList) {
                    if (auth.contains("|")) {
                        String agencyId = auth.substring(0, auth.indexOf("|"));
                        agencyId = agencyId.startsWith("ROLE_") ? agencyId.substring("ROLE_".length()) : agencyId;
                        String role = auth.substring(auth.indexOf("|") + 1);
                        agencyRoleMap.add(agencyId, role);
                    }
                }
            }
        }
        return agencyRoleMap;
    }

    public static AgencyRoleMap getAgencyRoleMapFromToken(TokenResponseDao token) {
        return getAgencyRoleMapFromToken(token.getAccessToken());
    }

    // implementors

    public abstract TokenResponseDao login(String realm, String url, String username, String password);

    public TokenResponseDao login(String realm, String url, String username, String password, String locale) {
        return login(realm,url,username,password);
    }


    public abstract void deleteUser(String realm, String url, String token, String uid);

    public abstract User createUser(String realm, String url, String token, CreateOrUpdateUser createUser);

    public abstract void updateUser(String realm, String url, String token, CreateOrUpdateUser createUser);

    public abstract void changePassword(String realm, String url, String uid, String curPassword, String newPassword);

    public abstract String resetPassword(String realm, String url, String token, String uid);

    public abstract Boolean isChangePasswordRequired(String realm, String url, String uid, String password);

    public abstract void resetPassword(String realm, String url, String token, User user);

    public abstract List<SimpleUser> getUserForAgency(String realm, String url, String token, ArrayList<String> agencies);

    public abstract List<User> getUserWithRoles(String realm, String url, String token, ArrayList<String> roles);

    public abstract User getUserDetail(String realm, String url, String username, String token);

    public static void main(String[] args) {
        System.out.println(getJwTokenClaimDaoStaticIgnoreExpiration("eyJhbGciOiJSUzI1NiJ9.eyJwcmVmZXJyZWRMYW5ndWFnZSI6ImVuIiwiY2xpZW50SWQiOiJjbGllbnQiLCJ1c2VyX25hbWUiOiJKQVNQRVJWREIiLCJvcmdVbml0IjoiMCIsInVzZXJOYW1lIjoiSkFTUEVSVkRCIiwiYXV0aG9yaXRpZXMiOlsiQ09NUFJFU1NFRF9INHNJQUFBQUFBQUFBSDFYVFpPYk9CRDlLem5rbWlwN3Rtb21QaEloejZxQ3dRR2IzYzBsUlRHeXJSb01YaUZtNDZyNThTdUV3VWpkR3QvOG5qNWEzYTgvU0pPSS9ucFk5TCtuZDFLMEo4SGw1M1FPWnQyRnl6ZlJOamQ4OFI1MDZoUzhuRVZ0TC94V2xLOVZjeVRGUlhYU1BZWCs1bVdueEJ1MzRhQjdFYXFSTG9pZXI5Y2lhTlEwcjkwRkliSnJpNkc4YmtWdng0K095NnZOSVZET3BUZ0FsK3g0ZWRKdXVUUlNPU2IyRjI0SytZcmN2STFpVW5INU9ub1J0Mi9UdkhRVi83Vk5LTmdOQTZHUFVQeHNMeVI1YWdNcGI1VVVwVUl1aTdjMitMUmFMRUM4eDhYUFNZNi82bE55T0loU0ZKWGp1bDMyeVhkV3o0RmRUNDhMbjFkU3p0cTI0elAvalV6STNEZjBwMEFGR1JoM2hhSG1iak1BcGl0RDVDeGtkWHZocFJKTlRRQ0hQTm5aaGpCclVadDlUczZrYVJCdVdHeWpTVjJKbXFmOGFCekk3ZURaRWpZUW9paURvejQxREJwVHczajBQZTVDem9MeDZkRVAzd0NlYlZBczU4WjdNWitiazBEUkdWNEIxZEhEdnZwbFNGUU9LMHl3QnJYa3RQSkx6NXlBbDZUbDhtNFRLRU9HSk91VU5MV1NUVldOMU5LSVpQbSs1U1d0bFZEWEhHRzJzcmtBM1BVaE9FbjdCVDBKdzdOT3ZuSDA3ZzFYWEhvSi8xa1RNMjhTKzlaMUNTUDB5MCsyK2FLTEZkcWNiRWN1NENsRGVXSXhVb1RJK25tbXk0WHoveDZVT1RyM2xWWnVVZEdheStQVlEvNVZxUElVZXNpZ0xKM09SMjBqemZVdU9DK2Z3ZVVpbTdmWlkvOVk5UExiYjJtYXN5eEpiWnpGMlphU25RdlR2eW5aNzFoT2JaZ0UyWitNcGtDZ0tlOHpOaGY4UHlUbTJibVE2cVlJbjcvT1VCR0RWRHFsM0xpRjNZV2NpcllWVGtvT2hrdlpTQVFuUlYzeWFrWUFHM1NTdGQzWmVWcVFodHMwQ2ZjRVBGb3pKSWwzR293d0R0T01obG1XN2JIbFVmTE1zaDBqMmR5NEIxUm1ENzdKeWFxYjQxcFc4bjdldTZ2ZklZQkhIdDUvOHJad0NsanZRYnliand4U25JM2Y0MFMvaWs3cXc4aGt2V2FFQlpGamhUaHZtMVlGUjE0cm5JRkJEc3AvTzZHVVhXRnY4dDlza2pnak9sYmZuVkhFN1RhM1poMUdUcCs3NFpFSER5T2R1amlGTktnYmsrNldqOGllTUtLL0MvMDZEbWVJQ0Y0enIzeUVTMFdhTVp1K3VsTDVpalF5YzZIT1g5MmRzSEtJWmNERUlCa3djWTdVSnh3WmRwQU1HQU9IV2piTWN2Q09DWWVIVFpRemREejZjM25pOFBkcmdUVDFnVVFnZWtSL1hSMDhVMkFFSTNzN2pOWXZqV3lSK1hDa2tLQ25YTmY1bGlOaFkzR2VVa0xaZGdkd1g4dzB0UW5pL1RvZ3UzMUtBWWVFVGFPSW8xZWdQYzYrT2VCZ0IzcnpNSWhSYk55QzRadE5kR0I2TWlUKzRQbG9EUHJsdU05MTRJaGpYaG81eEx3UHZJUnBhNWluMFpoYmxLMFVRNEdCWnRqZ2FaZldOR3lWamZ0VmlNSlhudFFiUElyb2Nkcmhpd09lc0l2YkY2VHZjd3BqN25uWjI2RS8zUHpUNFRXTTNKcUxiTHRYM1E4KytFS01IQlBraDNiU1AyQk9xMXRWS0Q3TUpQWjVlcEtTb2xhUmNiMjdENk5HSzdTTXF5dmFuRUF1alhicjBxbzJSVjBja1Q0RDVxV3BWdWNwU1lnV2xXUDROOUdjZWYveDA0TDFVUVNYZStBeGhmNEhycFdVbGJFU0FBQT0iXSwiYXVkIjpbInJlc3RfYXBpIl0sInBhc3N3b3JkRXhwaXJ5VGltZSI6bnVsbCwic2NvcGUiOlsidHJ1c3QiLCJyZWFkIiwid3JpdGUiXSwicGFzc3dvcmRFeHBpcnlXYXJuaW5nVGltZSI6bnVsbCwiYXR0cmlidXRlcyI6eyJmaXJzdE5hbWUiOiJKYXNwZXIiLCJsYXN0TmFtZSI6InZhbiBkZXIgQmlqbCIsInByZWZlcnJlZExhbmd1YWdlIjoiZW4iLCJvcmdVbml0IjoiMCIsInBhc3N3b3JkRXhwaXJ5VGltZSI6bnVsbCwicGFzc3dvcmRFeHBpcnlXYXJuaW5nVGltZSI6bnVsbH0sImV4cCI6MTU2ODYzNzU1OSwiaWF0IjoxNTY4NjMzOTU5LCJqdGkiOiI5MWZiODY5OS0zYmMzLTRlODItOTc2MC0zMDA0YzYwOGMwMWIifQ.QszeKexkRnI_3lzBLu8CmGIqfmdkH5joBwm3S0BDuA9NWK7kcabPeL6Y6cCm0UoszEj1mccei3_-_8Ww0Bmh66FIHm3to-YTwcFQW6SX7jt49os2P5cWn0SzL0OG5yelzRcnRq4HE07pW7xAnYAfbgiYxwuxehX3JW571Kczeh4"));
    }

}
