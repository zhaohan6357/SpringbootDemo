package com.chem2cs.aspects;


        import org.aspectj.lang.annotation.After;
        import org.aspectj.lang.annotation.Aspect;
        import org.aspectj.lang.annotation.Before;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.stereotype.Component;


//this is master
@Aspect
@Component
public class LogAspect {
    private static  final Logger LOGGER=  LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.chem2cs.controller.*.*(..)) ")
    public void beforeMethod(){
        LOGGER.info("before method");
    }

    @After("execution(* com.chem2cs.controller.*.*(..)) ")
    public void afterMethod(){
        LOGGER.info("after method");
    }
}
