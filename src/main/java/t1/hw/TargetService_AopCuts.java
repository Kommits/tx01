package t1.hw;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import t1.hw.unrelatedToAopStuff.*;

import java.time.LocalDateTime;
import java.util.List;

@Aspect
@Component
public class TargetService_AopCuts {
    @Autowired private StubAuthService stubAuthService;
    @Autowired private StubLoggingService stubLoggingService;
    @Autowired private StubNotificationService stubNotificationService;
    @Autowired private StubPerformanceService stubPerformanceService;
    @Autowired private StubSystemDiagnosticsService stubSystemDiagnosticsService;

    //
    // врезка @Before:
    //
    // что мы можем:
    //      1. сбросить исполнение аспектируемого метода - вызвав исключение
    //      2. взаимодействовать с именем вызываемого метода и массивом параметров
    //      3. в случае не понятности селектора и установки any выборки "execution(* *(..))"
    //         посмотреть реальную сигнатуру метода для точечного воздействия
    //         через joinPoint.getSignature() <- селектор не надо изобретать - он будет лежать тут
    // что мы не можем:
    //      1. заменить исполнение метода другим кодом (вызов состоится по окончании отработки аспекта)
    //      2. гарантированно заменить параметры другим набором данных (в примере это не мутируемый список)
    //
    @Before("execution(* *.*.TargetService.applyToMeAop_Before(..))")
    public void applyToMeAop_Before_aspect(JoinPoint joinPoint) throws Exception {
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        if(!AopCutsCfg.useBefore) return;
        //-
        System.out.println(
            "\t\u001B[31m<<<\u001B[0m " + whoAmI + " for: " + "\u001B[32m" + joinPoint.getSignature() + "\u001B[0m"
        );
        var res = stubSystemDiagnosticsService.isSystemReady();
        if(!res) throw new Exception("system diagnostics failed");
        System.out.println("\t\tfunctionality (system diagnostic):"+res);
    }

    //
    // врезка @AfterThrowing
    //
    // что мы можем:
    //      1. обработать исключение от метода дополнительно
    //      2. увидеть исключение даже в случае если оно перехвачено, до такого перехвата
    // что мы не можем:
    //      1. обогнать finally (вызов аспекта будет позже, по факту выхода из метода)
    //      2. чтото вернуть взамен сорвавшегося метода
    //
    @AfterThrowing(
            pointcut = "execution(* *.*.TargetService.applyToMeAop_AfterThrowing(..))",
            throwing = "exception"
    )
    public void applyToMeAop_AfterThrowing_aspect(JoinPoint joinPoint, Throwable exception) {
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        if(!AopCutsCfg.useAfterThrowing) return;
        //-
        System.out.println(
                "\t\u001B[31m<<<\u001B[0m " + whoAmI + " for: " +
                "\u001B[32m" + joinPoint.getSignature() + "\u001B[0m " + "\n" +
                "\t\texception: \u001B[35m" + exception.getMessage() + "\u001B[0m"
        );
        stubLoggingService
        .addLoggingMessage("aop",new StubLoggingService.LoggingMessage(LocalDateTime.now(), "message"));
        System.out.println("functionality (logging):");
        stubLoggingService.printLogging();
    }

    //
    // врезка @AfterReturning
    //
    // что мы можем:
    //      1. получить результат исполнения в произвольном месте после исполнения
    //      2. использовать результат даже если он ничему не присваивается
    // что мы не можем:
    //      1. гарантированно переопределить результат (в примере это не мутируемый список)
    //
    @AfterReturning(
            pointcut = "execution(* *.*.TargetService.applyToMeAop_AfterReturning(..))",
            returning = "result"
    )
    public void applyToMeAop_AfterReturning_aspect(JoinPoint joinPoint, List<String> result) {
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        if(!AopCutsCfg.useAfterReturning) return;
        //-
        System.out.println(
                "\t\u001B[31m<<< \u001B[0m " + whoAmI + " for: " + "\u001B[32m" +
                        joinPoint.getSignature() +  "\u001B[0m" + "\n" +
                        "\t\t with result: " + "\u001B[35m" + result + "\u001B[0m"
        );
        stubNotificationService
        .sendNotification("aop_addressee",
        new StubNotificationService.Notification("aop_addressee", LocalDateTime.now(),"message"));
        System.out.println("functionality (notification):");
        stubNotificationService.printNotifications();
    }

    //
    // врезка @Around (декоратор)
    //
    // что мы можем:
    //      1. получить доступ к параметрам и к названию вызываемого метода
    //      2. заменить параметры и/или заменить метод
    //      3. выбрать место вставки в логику аспекта аспектируемого метода
    //      4. мутировать параметры - получив к ним прямой именованный доступ
    // что мы не можем:
    //      -- типичный декоратор : возможна полная замена функциональности метода
    //
    @Around("execution(* *.*.TargetService.applyToMeAop_Around(..)) && args(params)")
    public List<String> applyToMeAop_Around_aspect(ProceedingJoinPoint joinPoint, List<String> params) throws Throwable {
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        if(!AopCutsCfg.useAround){
            return (List<String>)joinPoint.proceed(new Object[]{params});
        }
        //-
        params = List.of("three","four");
        System.out.println(
                "\t\u001B[31m<<< \u001B[0m " + whoAmI + " for: " + "\u001B[32m" +
                        joinPoint.getSignature() +  "\u001B[0m"
        );
        stubPerformanceService.startPerformancePoint("pp_test");
            var result = (List<String>)joinPoint.proceed(new Object[]{params});
        stubPerformanceService.endPerformancePoint("pp_test");
        //-
        System.out.println(
                "\t\u001B[31m<<< \u001B[0m " + whoAmI + " for: " + "\u001B[32m" +
                        joinPoint.getSignature() +  "\u001B[0m" + "\n" +
                        "\t\t with result: " + "\u001B[35m" + result + "\u001B[0m"
        );
        System.out.println("\t\t functionality (performance):"
        + stubPerformanceService.getPerformance("pp_test"));
        stubPerformanceService.printPerformances();
        return result;
    }

    //
    // врезка под аннотацию : замена пути на аннотацию
    // или указывается полный путь к аннотации (не селектор - а путь к аннотации (что не посоедовательно))
    // или она лежит в том же пакадже что и выбираемый метод - тогда только имя аннотации
    //
    // что мы можем:
    //      1. добавлять функциональность через маркировку (по сути модификатор)
    //      2. делать АОП явным
    //      3. логикой объединять реакцию на аннотации как то
    //      Pointcut(@annotation())  +  Pointcut(@annotation())  = @Around(someA() || someB())
    //      public void someA(){}       public void someB(){}      public Object someC(..){}
    // что мы не можем:
    //      1. управлять динамическим снятием/установкою аннотаций
    //      2. отделаться коротким путем к аннотации - в случае если аннотация как файл лежит в другом пакете
    //
    @Around("@annotation(MethodMark4Aop)")
    public Object
    applyToMeAop_AnnotationSourceControl_aspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        if(!AopCutsCfg.useAnnotationSourceControl) return proceedingJoinPoint.proceed();
        System.out.println(
                "\t\u001B[31m<<< \u001B[0m " + "for: " + "\u001B[32m" +
                        proceedingJoinPoint.getSignature() +  "\u001B[0m"
        );
        var res = proceedingJoinPoint.proceed();
        System.out.println(
                "\t\u001B[31m<<< \u001B[0m " + "for: " + "\u001B[32m" +
                        proceedingJoinPoint.getSignature() +  "\u001B[0m"
        );
        var token = StubAuthService.defaultSystemServiceToken;
        var authorized = stubAuthService.isTokenAuthorized(token);
        if(!authorized) throw new Exception("not authorized");
        System.out.println("\tfunctionality:" + authorized);
        return res;
    }

    //
    // проксирование вызова с двойной аннотацией:
    // для вложенных методов с одинаковой аннотацией одного класса обертка будет вызвана единожды
    // а если вызывается метод того же класса без обертки в котором есть обертка - (!) она вообще не будет реализована
    //
    @Around("@annotation(t1.hw.MethodDoubleMark4Aop)")
    public Object applyToMeAop_DoubleAnnotationB_aspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if(!AopCutsCfg.useDoubleAnnotation) return proceedingJoinPoint.proceed();
        System.out.println("\t\u001B[31m<<< \u001B[0m " + "transactional-like");
        var res = proceedingJoinPoint.proceed();
        System.out.println("\t\u001B[31m<<< \u001B[0m " + "transactional-like");
        return res;
    }

    //
    // врезка any : на вызов любого метода
    //
    // что мы можем:
    //      1. перехватить вызов любого метода - и отлогировать этот вызов
    //      2. получить сигнатуру вызова - для того чтобы иметь возможньсть за него ухватиться
    // что мы не можем:
    //      1. --
    //
    @Before("execution(* *(..))")
    public void applyToMeAop_UnknownMethod_aspect(JoinPoint joinPoint){
        if(!AopCutsCfg.useInterceptAnyCall) return;
        System.out.println("\t\u001B[31m<<< \u001B[0m "+joinPoint.getSignature());
    }

    //
    // Введём вкл/выкл аспектов, получая возможность их тестировать
    // это антипаттерн, как бы привлекательно он для аспектов не выглядел
    //
    // в итоге всё закончиться сценарием:
    //    threadA выключает использование аспекта чтобы выполнится без ограничений
    //    в это время threadB рассчитывая выполниться с аспектом
    //    выполнится без него (а синхрон вокруг фактически "точек отладки" - ну такое)
    //
    // в нормальном проекте этот кусок был бы срезан через мэнифолд вне debug flow
    // http://manifold.systems/articles/preprocessor.html
    // или даже вырезан вовсе (но тогда не будет возможности ни для тестов ни для отладки,
    // так как оригинальное исполнение метода будет всегда закрыто аспектом (проблема в методе? аспекте?))
    //
    public static class AopCutsCfg{
        public static boolean
                useBefore = true,
                useAfterThrowing = true,
                useAfterReturning = true,
                useAround = true,
                useAnnotationSourceControl = true,
                useDoubleAnnotation = true,
                useInterceptAnyCall = false;
    }
}
