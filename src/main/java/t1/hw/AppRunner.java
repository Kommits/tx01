package t1.hw;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static t1.hw.TargetService_AopCuts.AopCutsCfg.*;

@ComponentScan()
public class AppRunner {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(AppRunner.class);
        var srv = ctx.getBean(TargetService.class);
        var whoAmI
        = (AppRunner.class).getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        List<String> resList = null;
        List<String> params = List.of("one","two");

        //
        // сценарий (-1-) : вызов аспекта перед методом адресной врезкой (функционал: диагностика)
        System.out.println("""

                ----------------------
                Aspect type: \u001B[34m"Before"\u001B[0m:
                pattern: \u001B[32m
                @Before("execution(* *.*.TargetService.applyToMeAop_Before(..))")
                \u001B[0mpublic void __(JoinPoint joinPoint) {}
                
                without an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useBefore = false;
        resList = srv.applyToMeAop_Before(params);
        System.out.println("::" + whoAmI + " result:" + resList);
        resList = null;
        System.out.println("""
                
                with an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useBefore = true;
        resList = srv.applyToMeAop_Before(params);
        System.out.println("::" + whoAmI + " result:" + resList);
        resList = null;

        //
        // сценарий (-2-) : вызов аспекта после выброса методом исключения (функционал: логирование)
        System.out.println("""

                -----------------------------
                Aspect type: \u001B[34m"AfterThrowing"\u001B[0m:
                pattern: \u001B[32m
                @AfterThrowing(
                            pointcut = "execution(* *.*.TargetService.applyToMeAop_AfterThrowing(..))",
                            throwing = "exception"
                )
                \u001B[0mpublic void __(JoinPoint joinPoint, Throwable exception) {}

                without an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useAfterThrowing = false;
        try {srv.applyToMeAop_AfterThrowing(params);}
        catch(Exception e){System.out.println("::" + whoAmI + ":" + e.getMessage());}
        System.out.println("""
                
                with an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useAfterThrowing = true;
        try {srv.applyToMeAop_AfterThrowing(params);}
        catch(Exception e){System.out.println("::" + whoAmI + ":" + e.getMessage());}

        //
        // сценарий (-3-) : вызов асппекта после отработки метода (функционал: нотификация)
        System.out.println("""

                ------------------------------
                Aspect type: \u001B[34m"AfterReturning"\u001B[0m:
                pattern: \u001B[32m
                @AfterReturning(
                            pointcut = "execution(* *.*.TargetService.applyToMeAop_AfterReturning(..))",
                            returning = "result"
                    )
                \u001B[0mvoid __(JoinPoint joinPoint, List<String> result) {}
                
                without an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useAfterReturning = false;
        resList = srv.applyToMeAop_AfterReturning(params);
        System.out.println("::" + whoAmI + " result:" + resList);
        resList = null;
        System.out.println("""
                
                with an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useAfterReturning = true;
        resList = srv.applyToMeAop_AfterReturning(params);
        System.out.println("::" + whoAmI + " result:" + resList);
        resList = null;

        //
        // сценарий (-4-) : декорирование метода аспектом (функционал: перформанс контроль)
        System.out.println("""

                ----------------------
                Aspect type: \u001B[34m"Around"\u001B[0m:
                pattern: \u001B[32m
                @Around("execution(* *.*.TargetService.applyToMeAop_Around(..)) && args(params)")
                \u001B[0mList<String> __(ProceedingJoinPoint joinPoint, List<String> params) {}
                
                without an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useAround = false;
        resList = srv.applyToMeAop_Around(params);
        System.out.println("::" + whoAmI + " result:" + resList);
        resList = null;
        System.out.println("""
                
                with an aspect:""");
        System.out.println(whoAmI + " is trying to call with params:" + params);
        useAround = true;
        resList = srv.applyToMeAop_Around(params);
        System.out.println("::" + whoAmI + " result:" + resList);
        resList = null;

        //
        // сценарий (-5-) : вызов аспекта по аннотации на методе/before (функционал: авторизация)
        System.out.println("""
                
                -----------------------------------------
                Aspect type: \u001B[34m"Annotation source control"\u001B[0m:
                pattern: \u001B[32m
                @Around("@annotation(MethodMark4Aop)")
                @Around("@annotation(t1.hw.unrelatedToAopStuff.MethodDoubleMark4Aop)")
                \u001B[0mpublic Object __(ProceedingJoinPoint proceedingJoinPoint) {}
                
                without an aspect:""");
        System.out.println(whoAmI + " is trying to call");
        useAnnotationSourceControl = false;
        srv.applyToMeAop_AnnotationSourceControl();
        System.out.println("""
                
                with an aspect:""");
        System.out.println(whoAmI + " is trying to call");
        useAnnotationSourceControl = true;
        srv.applyToMeAop_AnnotationSourceControl();

        //
        // сценарий (-6-) : вызов метода с аннотацией внутри которого есть метод с такой же аннотацией
        System.out.println("""
                
                ---------------------------------
                Aspect type: \u001B[34m"Double Annotation"\u001B[0m:
                pattern: \u001B[32m
                @Around("@annotation(MethodMark4Aop)")
                @Around("@annotation(t1.hw.unrelatedToAopStuff.MethodDoubleMark4Aop)")
                \u001B[0mpublic Object __(ProceedingJoinPoint proceedingJoinPoint) {}
                
                without an aspect:""");
        System.out.println(whoAmI + " is trying to call");
        useDoubleAnnotation = false;
        srv.applyToMeAop_DoubleAnnotationB();
        System.out.println("""
                
                with an aspect:""");
        System.out.println(whoAmI + " is trying to call");
        useDoubleAnnotation = true;
        srv.applyToMeAop_DoubleAnnotationB();
        System.out.println("\n\t(-!-)");
        srv.applyToMeAop_DoubleAnnotationC();

        //
        // сценарий (-7-) : перехват через before вызова любого метода заданного адресной врезкой класса
        System.out.println("""
                
                ------------------------------------
                Aspect type: \u001B[34m"Intercept any method"\u001B[0m:
                pattern: @Before("execution(* *(..))")
                
                without an aspect:""");
        System.out.println(whoAmI + " is trying to call any method of the service");
        useInterceptAnyCall = false;
        srv.applyToMeAop_UnknownMethod();
        System.out.println("""
                
                with an aspect:""");
        System.out.println(whoAmI + " is trying to call any method of the service");
        useInterceptAnyCall = true;
        srv.applyToMeAop_UnknownMethod();
        useInterceptAnyCall = false;
    }
}
