package za.co.spsi.toolkit.util;

public enum MaskId {

    REGEX1(Mask.REGEX1, "MSG000014"),
    REGEX2(Mask.REGEX2, "MSG000025"),
    REGEX3(Mask.REGEX3, "MSG000036"),
    REGEX4(Mask.REGEX4, "MSG000038"),
    REGEX5(Mask.REGEX5, "MSG000039"),
    REGEX6(Mask.REGEX6, "MSG000040"),
    REGEX7(Mask.REGEX7, "MSG000041"),
    REGEX8(Mask.REGEX8, "MSG000042"),
    REGEX9(Mask.REGEX9, "MSG000043"),
    REGEX10(Mask.REGEX10, "MSG000015"),
    REGEX11(Mask.REGEX11, "MSG000016"),
    REGEX12(Mask.REGEX12, "MSG000017"),
    REGEX13(Mask.REGEX13, "MSG000018"),
    REGEX14(Mask.REGEX14, "MSG000019"),
    REGEX15(Mask.REGEX15, "MSG000020"),
    REGEX16(Mask.REGEX16, "MSG000021"),
    REGEX17(Mask.REGEX17, "MSG000022"),
    REGEX18(Mask.REGEX18, "MSG000023"),
    REGEX19(Mask.REGEX19, "MSG000024"),
    REGEX20(Mask.REGEX20, "MSG000026"),
    REGEX21(Mask.REGEX21, "MSG000027"),
    REGEX22(Mask.REGEX22, "MSG000028"),
    REGEX23(Mask.REGEX23, "MSG000029"),
    REGEX24(Mask.REGEX24, "MSG000030"),
    REGEX25(Mask.REGEX25, "MSG000031"),
    REGEX26(Mask.REGEX26, "MSG000032"),
    REGEX27(Mask.REGEX27, "MSG000033"),
    REGEX28(Mask.REGEX28, "MSG000034"),
    REGEX29(Mask.REGEX29, "MSG000035"),
    REGEX30(Mask.REGEX30, "MSG000037"),
    REGEX42(Mask.REGEX42, "MSG000058"),
    REGEX43(Mask.REGEX43, "MSG000059"),
    REGEX46(Mask.REGEX46, "MSG7000109"),
    REGEX47(Mask.REGEX47, "MSG7000112"),
    REGEX48(Mask.REGEX48, "MSG7000113"),
    REGEX49(Mask.REGEX49, "MSG000012"),

    DATE1(Mask.DATE1, "MSG000005"),

    CHECKBOX(Mask.CHECKBOX, "MSG000004"),
    TRUE(Mask.TRUE, null),
    FALSE(Mask.FALSE, null),
    NUM(Mask.NUM, "MSG000013"),
    ALPHA(Mask.ALPHA, "MSG000001"),
    ANY(Mask.ANY, "MSG000002"),
    DECIMAL3(Mask.DECIMAL3, "MSG1300030"),
    REG_NO_BINDURA(Mask.REG_NO_BINDURA, "MSG7000108"),
    FORMATTEDDECIMAL3(Mask.FORMATTEDDECIMAL3, "MSG1300030"),
    DECIMAL1(Mask.DECIMAL4, "MSG1300028"),
    NUMWITHFORMATTING(Mask.NUMWITHFORMATTING, "MSG1300028"),
    MONEY("^[0-9]+(\\.[0-9]{1,2})?", "MSG000012"),
    REGEXADDRESS(Mask.REGEXADDRESS, "MSG1300098");

    private String regExp;
    private String msgCode;

    MaskId(String regExp, String msgCode) {
        this.regExp = regExp;
        this.msgCode = msgCode;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

}
