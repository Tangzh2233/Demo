package com.edu.JavaLearning.aop.aop.springaop;

import org.springframework.aop.framework.ProxyFactory;

import java.sql.SQLException;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 */

public class SpringAopTest {

    public static void main(String[] args) throws SQLException {
        ProxyFactory proxyFactory = new ProxyFactory();
        //前置增强
        BeforeAdvice beforeAdvicea = new BeforeAdvice();
        //后置增强
        AfterAdvice afterAdvice = new AfterAdvice();
        //环绕增强
        AroundAdvice aroundAdvice = new AroundAdvice();
        //异常增强
        ExceptionAdvice exceptionAdvice = new ExceptionAdvice();
        NativeWaiter target = new NativeWaiter();

        proxyFactory.setInterfaces(target.getClass().getInterfaces());//指定对接口进行代理[jdk`TAMTMSFCT` (`TAM_BAT_NO`,`MERC_ID`,`TAM_BUS_TYP`,`TAM_FIL_DT`,`FIL_DT_SEQ`,`TAM_FIL_NM`,`FILE_RCV_DT`,`TAM_BEG_TM`,`TAM_END_TM`,`RMK`,`AC_DT`,`TOT_UP_AMT`,`TOT_UP_CNT`,`PASS_TOT_CNT`,`PASS_TOT_AMT`,`ROL_TOT_AMT`,`ROL_TOT_NUM`,`SUC_TOT_AMT`,`SUC_TOT_NUM`,`FAIL_TOT_AMTF`,`FAIL_TOT_AMTS`,`OTHER_INF`,`TAM_FIL_STS`,`TAM_SRV`,`TAM_TX_CD`,`TM_SMP`,`IF_URGENCY`,`TAM_FIL_TM`,`ARM_NO`,`TOT_PASS_FEEAMT`,`TXN_ACC`,`FAIL_TOT_CNT`,`REJ_TOT_CNT`,`CMT_TOT_CNT`,`SEND_TOT_CNT`,`REJ_TOT_AMT`,`SEND_TOT_AMT`,`CMT_TOT_AMT` ,`MERC_TYP`,`MERC_TRD_CLS`,`MERC_NAM`,`HLD_NO`,`RTN_FLG`,`BACK_FILE`,`SYS_CNL`,`BUS_CNL`,`TX_TYP`,`BUS_TYP`,`PS_APPR_STS`,`PS_APPR_OPR`,`PS_APPR_BATNO`,`PS_APPR_DT`,`PS_APPR_TM`,`PS_APPR_RMK`,`PS_WFF_STS`,`PS_WFF_OPR`,`PS_WFF_DT`,`PS_WFF_TM`,`PS_WFF_RMK`,`PS_WFF_BATNO`,`TAM_WF_STS`)]
        //proxyFactory.setOptimize(true);//启用优化[又将使用Cglib代理]
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(beforeAdvicea);
        proxyFactory.addAdvice(afterAdvice);
        proxyFactory.addAdvice(aroundAdvice);
        proxyFactory.addAdvice(exceptionAdvice);


        Waiter aj = (Waiter)proxyFactory.getProxy();//创建目标实例
        aj.serverto("tang");
        aj.throwException();
    }
}
