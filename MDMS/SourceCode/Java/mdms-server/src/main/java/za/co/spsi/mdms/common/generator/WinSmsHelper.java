package za.co.spsi.mdms.common.generator;

import lombok.Data;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.io.IOUtil;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.util.Util.isInt;

/**
 * Created by jaspervdbijl on 2017/07/26.
 */
@Dependent
public class WinSmsHelper {

    public static final Logger TAG = Logger.getLogger(WinSmsHelper.class.getName());

    @Inject
    @ConfValue(value = "winsms.msisdn", folder = "server")
    private String winSmsMsisdn;

    @Inject
    @ConfValue(value = "winsms.username", folder = "server")
    private String username;

    @Inject
    @ConfValue(value = "winsms.password", folder = "server")
    private String password;

    public String getWinSmsMsisdn() {
        return winSmsMsisdn;
    }

    public static List<Sms> getSms(String data) {
        data = data.replace("Date&Time","Date_Time");
        List<Sms> sms = new ArrayList<>();
        Arrays.stream(data.split("&")).filter(s -> !StringUtils.isEmpty(s)).forEach(s -> {
            try {
                sms.add(Sms.get(s));
            } catch (Exception ex) {
                TAG.log(Level.WARNING, ex.getMessage(), ex);
            }
        });
        return sms;
    }

    public static String encode(String txt) {
        try {
            return URLEncoder.encode(txt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sendGET(String urlStr, String... params) {
        try {
            Object p[] = params != null ? Arrays.stream(params).map(s -> encode(s)).toArray(String[]::new) : null;
            TAG.info("GET SMS");
            URL url = new URL(String.format(urlStr, p));
            URLConnection connection = url.openConnection();
            connection.connect();
            try (InputStream is = connection.getInputStream()) {
                String response = new String(IOUtil.readFully(is, connection.getContentLength()));
                TAG.info("RCV " + response);
                return response;
            }
        } catch (Exception ue) {
            throw new RuntimeException(ue);
        }
    }

    public static Sms sendSms(String username, String password, String winSmsMsisdn, String msisdn, String message) {
        String response = sendGET("https://www.winsms.co.za/api/batchmessage.asp?User=%s&Password=%s&Message=%s&Numbers=%s",
                username, password, message, msisdn);
        // assume response of msisdn=STATUS& - status should be a numeric
        Assert.isTrue(!StringUtils.isEmpty(response), "No response received");
        Assert.isTrue(response.split("=").length == 2 & response.endsWith("&"), "Expected response format msisdn=REF&");
        String ref = response.split("=")[1];
        ref = ref.substring(0, ref.length() - 1);
        if (!isInt(ref)) {
            throw new WinSmsException(String.format("Invalid response [%s] received from WinSms: %s", response, ref));
        }
        return new Sms(msisdn, winSmsMsisdn, message, new Date());
    }

    public Sms sendSms(String msisdn, String message) {
        return sendSms(username, password, winSmsMsisdn, msisdn, message);
    }

    public static String getSms(String username, String password) {
        return sendGET("https://www.winsms.co.za/api/HTTPGetSCmessages.asp?User=%s&Password=%s", username, password);
    }

    public static String getReplies(String username, String password) {
        return sendGET(String.format("https://www.winsms.co.za/api/HTTPGetReplies.ASP?User=%s&Password=%s",username,password));
    }

    public List<Sms> getSms() {
        List<Sms> sms = new ArrayList<>();
        sms.addAll(getSms(getSms(username,password)));
        sms.addAll(getSms(getReplies(username,password)));
        return sms;
    }

    @Data
    public static class Sms {
        private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        private String sc, cli, message, refNo;
        private Date received;
        private String originalData;

        public Sms(String sc, String cli, String message, Date received) {
            this.sc = sc;
            this.cli = cli;
            this.message = message;
            this.received = received;
        }

        public static Sms get(String source) {
            try {
                Assert.notNull(source, "Source sms was null");
                Map<String,String> map = new HashMap<>();
                for (String value : source .split(";")) {
                    map.put(value.split("=")[0],value.substring(value.indexOf("=")+1));
                }
                return new Sms(map.get("SC"),map.get("CLI"),map.get("Message"),format.parse(map.get("DateReceived"))).setOriginalData(source);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public Sms setOriginalData(String originalData) {
            this.originalData = originalData;
            return this;
        }
    }

    // SC=27872406131;CLI=27715563783;DateReceived=201707281137;Message=G1 On 170101-30183 7.47A 13.7 1 100 243 01/01/0000 20:41:14&SC=27872406131;CLI=27715563783;DateReceived=201707281137;Message=G1 Off 170101-30183 7.47A 13.7 1 100 243 01/01/0000 20:41:39&SC=27872406131;CLI=27715563783;DateReceived=201707281139;Message=G1 Off 170101-30183 7.47A 13.8 1 90 243 01/01/0000 20:43:17&
    //  gerhard@pecgroup.co.za, Password – 0872406131

    private static List<File> listFiles(List<File> files,File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(f -> listFiles(files,f));
        } else {
            files.add(file);
        }
        return files;
    }

    private static List<File> listFiles(File folder) {
        List<File> files = new ArrayList<>();
        return listFiles(files,folder);
    }

    public static void main(String args[]) throws Exception {
//        getReplies("gerhard@pecgroup.co.za", "0872406131");
        sendSms("pecutilities_uat", "It$@$ecr3t_uat", "0872406992", "0615075069", "TEST GSMC");
//        final List<String> names = new ArrayList<>();
//        Map<String,String> map = new HashMap<>();
//        File folder = new File("/Users/jaspervdbijl/Downloads/ICE-POSMOB-v1.0.0.133/Install/POSMOB-v1.0.0.133/WEB-INF/lib");
//        Arrays.stream(folder.listFiles()).filter(f -> f.getName().endsWith(".jar")).forEach(f -> {
//            try {
//                final File unzip = IOUtil.unzipFile(f, false);
//                listFiles(unzip).stream().filter(p -> p.getName().endsWith(".class")).forEach(file -> {
//                    String name = file.getAbsolutePath().substring(unzip.getAbsolutePath().length());
//                    if (map.containsKey(name)) {
//                        System.out.println("Duplicate " + name + " in " + map.get(name) + " found in " + f.getName());
//                    }
//                    map.put(name,f.getName());
//
//                });
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });

//        System.out.println("All names "+ names);
    }
}
