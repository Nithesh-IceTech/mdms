package za.co.spsi.toolkit.util;

public class Mask {

    public static final String REGEX1 = "[ A-Z\\d-&()'ËÉÄÖÜÇ]*";
    public static final String REGEX2 = "[A-Z\\d]*";
    public static final String REGEX3 = "[ A-Z\\d]*";
    public static final String REGEX4 = "[N][\\/][A]|[A-Z\\d._-]*@[A-Z\\d._-]*";

    public static final String REGEX5 = "V\\d{7}";
    public static final String REGEX6 = "C\\d{10}";
    public static final String REGEX7 = "S\\d{5}";
    public static final String REGEX8 = "J\\d{19}";
    public static final String REGEX9 = "T\\d{19}";
    public static final String REGEX10 = "A\\d{9}";
    public static final String REGEX11 = "R\\d{9}";
    public static final String REGEX12 = "[ A-Z\\-&().'ÃÁÀÂÄÇÉÊËÍÕÓÔÖÚÜªº,]{0,}";
    public static final String REGEX13 = "[+]{0,1}\\d{1,3}";
    public static final String REGEX14 = "([+]\\d{5,15})|(\\d{5,15})";
    public static final String REGEX15 = "[ A-Z\\d-&()'ËÉÄÖÜÇ_%]*";
    public static final String REGEX16 = "[ A-Z-'ËÉÄÖÜÇ_%]*";
    public static final String REGEX17 = "(\\d{4}\\d{0,2}[A-Z]{2})|([A-Z]{1}\\d{8})";
    public static final String REGEX18 = "[A-Z\\d%_]*";
    public static final String REGEX19 = "V((\\d{7})|((\\d{0,6})([%_]+)(\\d{0,6})){1,7})";
    public static final String REGEX20 = "[ A-Z-&()'ËÉÄÖÜÇ%_]*";
    public static final String REGEX21 = "[ A-Z\\d-&()'ËÉÄÖÜÇ%_]*";
    public static final String REGEX22 = "[A-Z]{1}[A-Z]{1}\\d{3}[A-Z&&[^AEIOUQY]]{3}";
    public static final String REGEX23 = "A\\d{7}";
    public static final String REGEX24 = "R((\\d{9})|((\\d{0,8})([%_]+)(\\d{0,8})){1,9})";
    public static final String REGEX25 = "R\\d{6}";
    public static final String REGEX26 = "R((\\d{6})|((\\d{0,5})([%_]+)(\\d{0,5})){1,5})";
    public static final String REGEX27 = "LT\\d{8}";
    public static final String REGEX28 = "LL\\d{8}";
    public static final String REGEX29 = "LD\\d{8}";
    public static final String REGEX30 = "PL\\d{8}";
    public static final String REGEX42 = "[A-Z]{3}[1-9]{3}[A-Z]{2}|[A-Z]{3}[1-9]{4}";
    public static final String REGEX43 = "[ A-Z\\d-&()'ÃÁÀÂÄÇÉÊËÍÕÓÔÖÚÜ.,]*";
    public static final String REGEXADDRESS = "[ A-Z0-9\\-&().'ÃÁÀÂÄÇÉÊËÍÕÓÔÖÚÜº,\\/]{0,}";
    public static final String REGEX46 = "\\d*[A-Z]{0,1}\\d*";
    public static final String REGEX47 = "^[0-9]{8,9}[A-Z][0-9]{2}$";
    public static final String REGEX48 = "[H][M][0-9]{3}";
    public static final String REGEX49 = "^(?=.*[1-9])\\d*(?:\\.\\d{1,2})?$";

    public static final String DATE1= "^\\d{2}/\\d{2}/\\d{4}$";

    public static final String CHECKBOX = "true|false|TRUE|FALSE";
    public static final String TRUE = "true|TRUE";
    public static final String FALSE = "false|FALSE";
    public static final String NUM = "\\d+";
    public static final String NUMWITHFORMATTING = "[,.\\d]+";
    public static final String DECIMAL3 = "^(\\d{1,12})([.,]\\d{1,2})?$";
    public static final String FORMATTEDDECIMAL3 = "[0-9]{0,3}[.,]{0,1}[0-9]{0,3}[.,]{0,1}[0-9]{0,3}[.,]{0,1}[0-9]{1,3}[.,][0-9]{2}";
    public static final String DECIMAL4 = "^(\\d{1,2})([.,]\\d{1,2})?$";
    public static final String ALPHA = "[A-Za-z]*";
    public static final String ANY = ".*";
    public static final String REG_NO_BINDURA = "[\\d]{4}\\/(19|20)\\d{2}$";

    public static final String MONEY = "\\d+(\\.\\d{1,2})?";

}
