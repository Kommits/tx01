# первое домашнее задание 
основная идея - это сделать консольное приложение в котором выдается флоу работы методов,
и наглядно показано применение к ним аспектов

основным файлом по сути для рассмотрения является:
TargetService_AopCuts.java

фактически вся суть передана на следующем примере (что является консольной выдачей приложения)
было без аспекта / стало с аспектом. Также рассмотрены особенности работы прокси с двойной аннотацией, c вызовом метода с аннотацией 
из под метода без аннотации, плюс рассмаотрено как правильно получать сигнатуры для установки путей через any селектор.

```python
without an aspect:
AppRunner.main is trying to call with params:[one, two]
	::TargetService.applyToMeAop_Around is calling (start) with params:[one, two]
	::TargetService.applyToMeAop_Around is calling (end) ret with:[TargetService.applyToMeAop_Around, :, [one, two]]
::AppRunner.main result:[TargetService.applyToMeAop_Around, :, [one, two]]

with an aspect:
AppRunner.main is trying to call with params:[one, two]
	<<<  TargetService_AopCuts.applyToMeAop_Around_aspect for: List t1.hw.TargetService.applyToMeAop_Around(List)
	::TargetService.applyToMeAop_Around is calling (start) with params:[three, four]
	::TargetService.applyToMeAop_Around is calling (end) ret with:[TargetService.applyToMeAop_Around, :, [three, four]]
	<<<  TargetService_AopCuts.applyToMeAop_Around_aspect for: List t1.hw.TargetService.applyToMeAop_Around(List)
		 with result: [TargetService.applyToMeAop_Around, :, [three, four]]
		 functionality (performance):118219
		 performance point: pp_test:118219
::AppRunner.main result:[TargetService.applyToMeAop_Around, :, [three, four]]
```
во вторых идея проекта заключается в том чтобы передать 
возможности разработчика относительно каждого из аспектов, и подключение к этой возможности стаба сервиса функциональности.
как то
```java
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
```
