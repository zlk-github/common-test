package com.zlk.producer.controller;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 扫描code规则校验
 * @author likuan.zhou
 * @date 2021/11/5/005 10:40
 */
public class ScanCodeCheckUtil2 {


    public static void main(String[] args) {
        Boolean aBoolean = checkWaybillCodeTest("NGL41234567890002001");
        System.out.println("=============="+aBoolean);
    }


    /**
     * 校验是否符合运单号规则(测试类)
     * @param waybillCode 运单号
     * @return 符合true，不符合false
     */
    public static Boolean checkWaybillCodeTest(String waybillCode) {
        //NGI202100000015,NGL41234567890002001
        Pattern WAYBILL_CODE_PATTERN1 = compile("(NG|CN|EG|GH|KE|MA|UG|PK)(((L|I)(\\d))|\\d)");
        // 纸质面单14位。开头两位为国家，后面12位为流水号。
        // 总长度14。前2位一个校验，后12位为数字。

        // 电子面单主单14位，子单20位。
        // 国家2位，来源1位（L本地，I国际），类型1位（1本地海外仓包裹||国际前置仓，2本地揽件包裹||国际客户仓，3零担，4整车）
        // 年份2位（只能校验数字）,主流水8位（数字），子单总数3位（数字），子单序号3位（数字）
        // 总长度14或者20。前4位一个校验，后面校验全部为数字。
        if (StringUtils.isBlank(waybillCode)) {
            return false;
        }
       /* // 判断单号长度14或者20
        boolean bl = waybillCode.length() == CodeRegexConfig.MAIN_WAYBILL_CODE_LENGTH || waybillCode.length() == CodeRegexConfig.SUB_WAYBILL_CODE_LENGTH;
        if (!bl) {
            // 长度不满足单号规则长度
            return false;
        }*/
        return WAYBILL_CODE_PATTERN1.matcher(waybillCode).matches();
    }

}
