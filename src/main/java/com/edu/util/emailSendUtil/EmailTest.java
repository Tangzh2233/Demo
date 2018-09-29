package com.edu.util.emailSendUtil;

/**
 * @author Tangzhihao
 * @date 2018/4/10
 */

public class EmailTest {

    public static void main(String[] args) {
        String content = "你好!\n" +
                "2018-04-03  Cat监控日报信息如下：\n" +
                "监控日报: 2018-02-26\n" +
                "详情:<table border='1px' height='150px' cellpadding='0' cellspacing='0'><tr><th>项目名</th><th>Transaction</th><th>Event</th><th>Metric</th><th>总计</th></tr><tr><td align='center'>DemoCat</td><td align='center'>0</td><td align='center'>4</td><td align='center'>2</td><td align='center'>6</td></tr><tr><td align='center'>报警指标</td><td></td><td>login事件-All-failRatio;login事件-失败-count;</td><td>金额;</td><td></td></tr></table>";
        EmailSendUtil sendUtil = new EmailSendUtil();
        Email email = new Email();
        email.setToAddress("s_tangzh@jiedaibao.com");
        email.setSubject("HtmlEmailTest");
        email.setContent(content);

        try {
            EmailSendUtil.sendMail(email);
        } catch (Exception e) {
            System.out.println("邮件发送失败！");
        }
    }
}
