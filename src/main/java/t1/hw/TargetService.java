package t1.hw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t1.hw.unrelatedToAopStuff.UtilsService;

import java.util.List;

@Service
public class TargetService {
    @Autowired private UtilsService utilsService;

    // comment1

    public List<String> applyToMeAop_Before(List<String> param){
        if(param == null) param = List.of();
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        var ret = List.of(whoAmI,":", param.toString());
        //-
        System.out.println("\t::" + whoAmI + " is calling (start)" + " with params:" + param);
        try{return ret;}
        finally{
            System.out.println("\t::" + whoAmI + " is calling (end)" + " ret with:" + ret);
        }
    }

    public void applyToMeAop_AfterThrowing(List<String> param) throws Exception {
        if(param == null) param = List.of();
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("\t::" + whoAmI + " is calling (start)" + " with params:" + param);
        try{
            System.out.println("\t\t:>" + whoAmI + " throwing exception");
            throw new Exception(whoAmI + ": exception message");
        }
        finally {
            System.out.println("\t::" + whoAmI + " is calling (end)");
        }
    }

    public List<String> applyToMeAop_AfterReturning(List<String> param){
        if(param == null) param = List.of();
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        var ret = List.of(whoAmI,":", param.toString());
        //-
        System.out.println("\t::" + whoAmI + " is calling (start)" + " with params:" + param);
        try{return ret;}
        finally{
            System.out.println("\t::" + whoAmI + " is calling (end)" + " ret with:" + ret);
        }
    }

    public List<String> applyToMeAop_Around(List<String> param){
        if(param == null) param = List.of();
        var whoAmI = getClass().getSimpleName() + "." + new Object(){}.getClass().getEnclosingMethod().getName();
        var ret = List.of(whoAmI,":", param.toString());
        utilsService.delayMe();
        //-
        System.out.println("\t::" + whoAmI + " is calling (start)" + " with params:" + param);
        try{return ret;}
        finally{
            System.out.println("\t::" + whoAmI + " is calling (end)" + " ret with:" + ret);
        }
    }

    @MethodMark4Aop
    public void applyToMeAop_AnnotationSourceControl(){
        System.out.println("\t:: method is working");
    }

    @MethodDoubleMark4Aop
    public void applyToMeAop_DoubleAnnotationA(){
        System.out.println("\t:: annotated methodA is working");
    }

    @MethodDoubleMark4Aop
    public void applyToMeAop_DoubleAnnotationB(){
        applyToMeAop_DoubleAnnotationA();
        System.out.println("\t:: annotated methodB is working");
    }

    // без аннотации
    public void applyToMeAop_DoubleAnnotationC(){
        applyToMeAop_DoubleAnnotationA();
        System.out.println("\t:: not annotated methodC is working");
    }

    public void applyToMeAop_UnknownMethod(){
        System.out.println("\t:: method is working");
    }
}
