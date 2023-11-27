
# 七、Spring
##  0. 什么是spring？

Spring是一个轻量级Java开发框架，目的是为了解决企业级应用开发的业务逻辑层和其他各层的耦合问题。它是一个分层的一站式轻量级开源框架，Spring负责基础架构，让Java开发者可以专注于程序的开发。
Spring最根本的使命是解决企业级应用开发的复杂性，即简化Java开发。
Spring可以做很多事情，它为企业级开发提供给了丰富的功能，但是这些功能的底层都依赖于它的两个核心特性，也就是依赖注入（DI）和面向切面编程（AOP）。

##  1. 使用Spring框架的好处是什么？

- **方便解耦，简化开发**(Spring就是一个大工厂，可以将所有对象的创建和依赖关系的维护，交给Spring管理)

- **AOP编程的支持**(Spring提供面向切面编程，可以方便的实现对程序进行权限拦截、运行监控等功能)

- **声明式事务的支持**(只需要通过配置就可以完成对事务的管理，而无需手动编程)

- **方便程序的测试**(Spring对Junit4支持，可以通过注解方便的测试Spring程序)

- **方便集成各种优秀框架**(Spring不排斥各种优秀的开源框架，其内部提供了对各种优秀框架的直接支持（如：Struts、

  Hibernate、MyBatis等）

- **降低JavaEE API的使用难度**(Spring对JavaEE开发中非常难用的一些API（JDBC、JavaMail、远程调用等），都提供了封装，使这些API应用难度大大降低)

## 2. Spring AOP，IOC 系列 ？

### 2.1 什么是 AOP？

AOP：Aspect oriented programming 面向切面编程，是 OOP 的一种延续、补充

OOP 思想是一种垂直纵向的继承体系，通过子类继承父类可以解决大部分的代码重复问题，但对于一些特殊问题，如在父类的所有方法的相同位置进行一些日志控制，OOP 无法解决

AOP 就是解决这些问题的，它通过一种横向抽取机制，将那些与业务无关，但却对多个对象产生影响的公共行为和逻辑，抽取并封装为一个可重用的模块，也就是切面，通过将横切逻辑代码与业务代码分离，在不改变原有代码的基础下，将逻辑增强部分应用到原有业务中，从根本上解耦，避免大量的重复操作，降低了模块间的耦合度，提高了系统的可维护性.

### 2.2 AOP 有哪些实现方式？

AOP 的实现是基于 **代理模式** 的，主要分为静态代理与动态代理

静态代理使用 `AspectJ` 来实现，动态代理则以 `Spring AOP` 为代表

- 静态代理 - 指使用 AOP 框架提供的命令进行编译，从而在编译阶段就可生成 AOP 代理类，因此也称为编译时增强；
  - 编译时编织（特殊编译器实现）
  - 类加载时编织（特殊的类加载器实现）。
- 动态代理 - 在运行时在内存中“临时”生成 AOP 动态代理类，因此也被称为运行时增强。
  - `JDK` 动态代理：通过反射来接收被代理的类，并且要求被代理的类必须实现一个接口 。JDK 动态代理的核心是 InvocationHandler 接口和 Proxy 类 。
  - `CGLIB`动态代理： 如果目标类没有实现接口，那么 `Spring AOP` 会选择使用 `CGLIB` 来动态代理目标类 。`CGLIB` （ Code Generation Library ），是一个代码生成的类库，可以在运行时动态的生成某个类的子类，注意， `CGLIB` 是通过继承的方式做的动态代理，因此如果某个类被标记为 `final` ，那么它是无法使用 `CGLIB` 做动态代理的。



### 2.3 什么是IOC和DI？

首先，Spring IOC，全称控制反转（Inversion of Control）。在传统的 Java 程序开发中，我们只能通过 new 关键字来创建对象，这种导致程序中对象的依赖关系比较复杂，耦合度较高。而 IOC 的主要作用是实现了对象的管理，也就是我们把设计好的对象交给了 IOC容器控制，然后在需要用到目标对象的时候，直接从容器中去获取。有了 IOC 容器来管理 Bean 以后，相当于把对象的创建和查找依赖对象的控制权交给了容器，这种设计理念使得对象与对象之间是一种松耦合状态，极大提升了程序的灵活性以及功能的复用性。

DI 表示依赖注入，也就是对于 IOC 容器中管理的 Bean，如果 Bean 之间存在依赖关系，那么 IOC 容器需要自动实现依赖对象的实例注入，通常有三种方法来描述 Bean 之间的依赖关系。通过依赖注入机制，我们只需要通过简单的配置，而无需任何代码就可指定目标需要的资源，完成自身的业务逻辑，而不需要关心具体的资源来自何处，由谁实现。

* 接口注入
* setter 注入
* 构造器注入

另外，为了更加灵活的实现 Bean 实例的依赖注入，Spring 还提供了@Resource和@Autowired 这两个注解。

分别是根据 bean 的 id 和 bean 的类型来实现依赖注入。

以上就是我对这个问题的理解！

### 2.4 什么是 Spring IOC 容器？
Spring 框架的核心是 Spring 容器。容器创建对象，将它们装配在一起，配置它们并管理它们的完整生命周期。Spring 容器使用依赖注入来管理组成应用程序的组件。容器通过读取提供的配置元数据来接收对象进行实例化，配置和组装的指令。该元数据可以通过 XML，Java 注解或 Java 代码提供。

## 3. 什么是依赖注入？可以通过多少种方式完成依赖注入？

在依赖注入中，您不必创建对象，但必须描述如何创建它们。您不是直接在代码中将组件和服务连接在一起，而是描述配置文件中哪些组件需要哪些服务。由 IoC 容器将它们装配在一起。

通常，依赖注入可以通过三种方式完成，即：

- 构造函数注入
- setter 注入
- 接口注入

在 Spring Framework 中，仅使用构造函数和 setter 注入。

## 4. 区分 BeanFactory 和 ApplicationContext？

| BeanFactory                | ApplicationContext       |
| -------------------------- | ------------------------ |
| 它使用懒加载               | 它使用即时加载           |
| 它使用语法显式提供资源对象 | 它自己创建和管理资源对象 |
| 不支持国际化               | 支持国际化               |
| 不支持基于依赖的注解       | 支持基于依赖的注解       |

BeanFactory和ApplicationContext的优缺点分析：

BeanFactory的优缺点：

- 优点：应用启动的时候占用资源很少，对资源要求较高的应用，比较有优势；
- 缺点：运行速度会相对来说慢一些。而且有可能会出现空指针异常的错误，而且通过Bean工厂创建的Bean生命周期会简单一些。

ApplicationContext的优缺点：

- 优点：所有的Bean在启动的时候都进行了加载，系统运行的速度快；在系统启动的时候，可以发现系统中的配置问题。
- 缺点：把费时的操作放到系统启动中完成，所有的对象都可以预加载，缺点就是内存占用较大。

## 5. 区分构造函数注入和 setter 注入

| 构造函数注入               | setter 注入                |
| -------------------------- | -------------------------- |
| 没有部分注入               | 有部分注入                 |
| 不会覆盖 setter 属性       | 会覆盖 setter 属性         |
| 任意修改都会创建一个新实例 | 任意修改不会创建一个新实例 |
| 适用于设置很多属性         | 适用于设置少量属性         |

## 6. spring 提供了哪些配置方式？

- 基于 xml 配置

bean 所需的依赖项和服务在 XML 格式的配置文件中指定。这些配置文件通常包含许多 bean 定义和特定于应用程序的配置选项。它们通常以 bean 标签开头。例如：

```xml
<bean id="studentbean" class="org.edureka.firstSpring.StudentBean">
 <property name="name" value="Edureka"></property>
</bean>
```

- 基于注解配置

您可以通过在相关的类，方法或字段声明上使用注解，将 bean 配置为组件类本身，而不是使用 XML 来描述 bean 装配。默认情况下，Spring 容器中未打开注解装配。因此，您需要在使用它之前在 Spring 配置文件中启用它。例如：

```xml
<beans>
<context:annotation-config/>
<!-- bean definitions go here -->
</beans>
```

- 基于 Java API 配置

Spring 的 Java 配置是通过使用 @Bean 和 @Configuration 来实现。

1. @Bean 注解扮演与 `<bean />` 元素相同的角色。
2. @Configuration 类允许通过简单地调用同一个类中的其他 @Bean 方法来定义 bean 间依赖关系。

例如：

```java
@Configuration
public class StudentConfig {
    @Bean
    public StudentBean myStudent() {
        return new StudentBean();
    }
}
```

## 7. Spring 中的 bean 的作用域有哪些?

- singleton : 唯一 bean 实例，Spring 中的 bean 默认都是单例的。
- prototype : 每次请求都会创建一个新的 bean 实例。
- request : 每一次HTTP请求都会产生一个新的bean，该bean仅在当前HTTP request内有效。
- session : ：在一个HTTP Session中，一个Bean定义对应一个实例。该作用域仅在基于web的Spring ApplicationContext情形下有效。
- global-session： 全局session作用域，仅仅在基于portlet的web应用中才有意义，Spring5已经没有了。Portlet是能够生成语义代码(例如：HTML)片段的小型Java Web插件。它们基于portlet容器，可以像servlet一样处理HTTP请求。但是，与 servlet 不同，每个 portlet 都有不同的会话



## 9. 将一个类声明为Spring的 bean 的注解有哪些?

我们一般使用 @Autowired 注解自动装配 bean，要想把类标识成可用于 @Autowired 注解自动装配的 bean 的类,采用以下注解可实现：

- @Component ：通用的注解，可标注任意类为 Spring 组件。如果一个Bean不知道属于哪个层，可以使用@Component 注解标注。 
- @Repository : 对应持久层即 Dao 层，主要用于数据库相关操作。 
- @Service : 对应服务层，主要涉及一些复杂的逻辑，需要用到 Dao层。
- @Controller : 对应 Spring MVC 控制层，主要用户接受用户请求并调用 Service 层返回数据给前端页面。

## 10. spring 支持几种 bean scope？

Spring bean 支持 5 种 scope：

- **Singleton** - 每个 Spring IoC 容器仅有一个单实例。
- **Prototype** - 每次请求都会产生一个新的实例。
- **Request** - 每一次 HTTP 请求都会产生一个新的实例，并且该 bean 仅在当前 HTTP 请求内有效。
- **Session** - 每一次 HTTP 请求都会产生一个新的 bean，同时该 bean 仅在当前 HTTP session 内有效。
- **Global-session** - 类似于标准的 HTTP Session 作用域，不过它仅仅在基于 portlet 的 web 应用中才有意义。Portlet 规范定义了全局 Session 的概念，它被所有构成某个 portlet web 应用的各种不同的 portlet 所共享。在 global session 作用域中定义的 bean 被限定于全局 portlet Session 的生命周期范围内。如果你在 web 中使用 global session 作用域来标识 bean，那么 web 会自动当成 session 类型来使用。

仅当用户使用支持 Web 的 ApplicationContext 时，最后三个才可用。

## 11. Spring 中的 bean 生命周期?

Bean的生命周期是由容器来管理的。主要在创建和销毁两个时期。

![](http://blog-img.coolsen.cn/img/1583675090641_51.png)

### 创建过程：

1，实例化bean对象，以及设置bean属性； 
2，如果通过Aware接口声明了依赖关系，则会注入Bean对容器基础设施层面的依赖，Aware接口是为了感知到自身的一些属性。容器管理的Bean一般不需要知道容器的状态和直接使用容器。但是在某些情况下是需要在Bean中对IOC容器进行操作的。这时候需要在bean中设置对容器的感知。SpringIOC容器也提供了该功能，它是通过特定的Aware接口来完成的。 
比如BeanNameAware接口，可以知道自己在容器中的名字。 
如果这个Bean已经实现了BeanFactoryAware接口，可以用这个方式来获取其它Bean。 
（如果Bean实现了BeanNameAware接口，调用setBeanName()方法，传入Bean的名字。 
如果Bean实现了BeanClassLoaderAware接口，调用setBeanClassLoader()方法，传入ClassLoader对象的实例。 
如果Bean实现了BeanFactoryAware接口，调用setBeanFactory()方法，传入BeanFactory对象的实例。） 
3，紧接着会调用BeanPostProcess的前置初始化方法postProcessBeforeInitialization，主要作用是在Spring完成实例化之后，初始化之前，对Spring容器实例化的Bean添加自定义的处理逻辑。有点类似于AOP。 
4，如果实现了BeanFactoryPostProcessor接口的afterPropertiesSet方法，做一些属性被设定后的自定义的事情。 
5，调用Bean自身定义的init方法，去做一些初始化相关的工作。 
6，调用BeanPostProcess的后置初始化方法，postProcessAfterInitialization去做一些bean初始化之后的自定义工作。 
7，完成以上创建之后就可以在应用里使用这个Bean了。

### 销毁过程：

当Bean不再用到，便要销毁 
1，若实现了DisposableBean接口，则会调用destroy方法； 
2，若配置了destry-method属性，则会调用其配置的销毁方法；

### 总结

主要把握创建过程和销毁过程这两个大的方面； 
创建过程：首先实例化Bean，并设置Bean的属性，根据其实现的Aware接口（主要是BeanFactoryAware接口，BeanFactoryAware，ApplicationContextAware）设置依赖信息， 
接下来调用BeanPostProcess的postProcessBeforeInitialization方法，完成initial前的自定义逻辑；afterPropertiesSet方法做一些属性被设定后的自定义的事情;调用Bean自身定义的init方法，去做一些初始化相关的工作;然后再调用postProcessAfterInitialization去做一些bean初始化之后的自定义工作。这四个方法的调用有点类似AOP。 
此时，Bean初始化完成，可以使用这个Bean了。 
销毁过程：如果实现了DisposableBean的destroy方法，则调用它，如果实现了自定义的销毁方法，则调用之。 

## 12. 什么是 spring 的内部 bean？

只有将 bean 用作另一个 bean 的属性时，才能将 bean 声明为内部 bean。为了定义 bean，Spring 的基于 XML 的配置元数据在 `<property>` 或 `<constructor-arg>` 中提供了 `<bean>` 元素的使用。内部 bean 总是匿名的，它们总是作为原型。

例如，假设我们有一个 Student 类，其中引用了 Person 类。这里我们将只创建一个 Person 类实例并在 Student 中使用它。

Student.java

```java
public class Student {
    private Person person;
    //Setters and Getters
}
public class Person {
    private String name;
    private String address;
    //Setters and Getters
}
```

bean.xml

```xml
<bean id=“StudentBean" class="com.edureka.Student">
    <property name="person">
        <!--This is inner bean -->
        <bean class="com.edureka.Person">
            <property name="name" value=“Scott"></property>
            <property name="address" value=“Bangalore"></property>
        </bean>
    </property>
</bean>
```

## 13. 什么是 spring 装配？

当 bean 在 Spring 容器中组合在一起时，它被称为装配或 bean 装配。 Spring 容器需要知道需要什么 bean 以及容器应该如何使用依赖注入来将 bean 绑定在一起，同时装配 bean。

Spring 容器能够自动装配 bean。也就是说，可以通过检查 BeanFactory 的内容让 Spring 自动解析 bean 的协作者。

自动装配的不同模式：

- **no** - 这是默认设置，表示没有自动装配。应使用显式 bean 引用进行装配。
- **byName** - 它根据 bean 的名称注入对象依赖项。它匹配并装配其属性与 XML 文件中由相同名称定义的 bean。
- **byType** - 它根据类型注入对象依赖项。如果属性的类型与 XML 文件中的一个 bean 名称匹配，则匹配并装配属性。
- **构造函数** - 它通过调用类的构造函数来注入依赖项。它有大量的参数。
- **autodetect** - 首先容器尝试通过构造函数使用 autowire 装配，如果不能，则尝试通过 byType 自动装配。

## 14. 自动装配有什么局限？

- 覆盖的可能性 - 您始终可以使用 `<constructor-arg>` 和 `<property>` 设置指定依赖项，这将覆盖自动装配。
- 基本元数据类型 - 简单属性（如原数据类型，字符串和类）无法自动装配。
- 令人困惑的性质 - 总是喜欢使用明确的装配，因为自动装配不太精确。

## 15. Spring中出现同名bean怎么办？

- 同一个配置文件内同名的Bean，以最上面定义的为准
- 不同配置文件中存在同名Bean，后解析的配置文件会覆盖先解析的配置文件
- 同文件中ComponentScan和@Bean出现同名Bean。同文件下@Bean的会生效，@ComponentScan扫描进来不会生效。通过@ComponentScan扫描进来的优先级是最低的，原因就是它扫描进来的Bean定义是最先被注册的~ 

## 16. 循环依赖相关问题？

### 16.1 什么是循环依赖？	

循环依赖-->循环引用。--->即2个或以上bean 互相持有对方，最终形成闭环。

eg：A依赖B，B依赖C，C又依赖A。【注意：这里不是函数的循环调用【是个死循环，除非有终结条件】，是对象相互依赖关系】

### 16.2  Spring中循环依赖的场景？

spring对循环依赖的处理有三种情况：

①构造器的循环依赖：这种依赖spring是处理不了的，直 接抛出BeanCurrentlylnCreationException异常。

②单例模式下的setter循环依赖：通过“三级缓存”处理循环依赖。

③非单例循环依赖：无法处理。

### 16.3  如何检测是否有循环依赖？

可以 Bean在创建的时候给其打个标记，如果递归调用回来发现正在创建中的话--->即可说明循环依赖。

### 16.4  怎么解决循环依赖问题？

Spring 是利用缓存机制来解决循环依赖问题的，而 Spring 中设计了三级缓存来解决循环依赖问题，当我们去调用 getBean()方法的时候，Spring 会先从一级缓存中去找到目标 Bean，如果发现一级缓存中没有便会去二级缓存中去找，而如果一、二级缓存中都没有找到，意味着该目标 Bean还没有实例化。于是，Spring 容器会实例化目标 Bean（PS：刚初始化的 Bean称为早期 Bean） 。然后，将目标 Bean 放入到二级缓存中，同时，加上标记是否存在循环依赖。如果不存在循环依赖便会将目标 Bean 存入到二级缓存，否则，便会标记该 Bean 存在循环依赖，然后将等待下一次轮询赋值，也就是解析@Autowired 注解。等@Autowired 注解赋值完成后，会将目标 Bean 存入到一级缓存。Spring 一级缓存中存放所有的成熟 Bean，二级缓存中存放所有的早期 Bean，先取一级缓存，再去二级缓存。

### 16.5  三级缓存的作用？

三级缓存是用来存储代理 Bean，当调用 getBean()方法时，发现目标 Bean 需要通过代理工厂来创建，此时会将创建好的实例保存到三级缓存，最终也会将赋值好的 Bean 同步到一级缓存中。





## 17. Spring 中的单例 bean 的线程安全问题？

当多个用户同时请求一个服务时，容器会给每一个请求分配一个线程，这时多个线程会并发执行该请求对应的业务逻辑（成员方法），此时就要注意了，如果该处理逻辑中有对单例状态的修改（体现为该单例的成员属性），则必须考虑线程同步问题。 
**线程安全问题都是由全局变量及静态变量引起的。** 
若每个线程中对全局变量、静态变量只有读操作，而无写操作，一般来说，这个全局变量是线程安全的；若有多个线程同时执行写操作，一般都需要考虑线程同步，否则就可能影响线程安全.

**无状态bean和有状态bean**

- 有状态就是有数据存储功能。有状态对象(Stateful Bean)，就是有实例变量的对象，可以保存数据，是非线程安全的。在不同方法调用间不保留任何状态。
- 无状态就是一次操作，不能保存数据。无状态对象(Stateless Bean)，就是没有实例变量的对象 .不能保存数据，是不变类，是线程安全的。

在spring中无状态的Bean适合用不变模式，就是单例模式，这样可以共享实例提高性能。有状态的Bean在多线程环境下不安全，适合用Prototype原型模式。 
Spring使用ThreadLocal解决线程安全问题。如果你的Bean有多种状态的话（比如 View Model 对象），就需要自行保证线程安全 。

- - 

## 20. Spring AOP and AspectJ AOP 有什么区别？

Spring AOP 基于动态代理方式实现；AspectJ 基于静态代理方式实现。
Spring AOP 仅支持方法级别的 PointCut；提供了完全的 AOP 支持，它还支持属性级别的 PointCut。

## 21. Spring 框架中用到了哪些设计模式？

**工厂设计模式** : Spring使用工厂模式通过 `BeanFactory`、`ApplicationContext` 创建 bean 对象。

**代理设计模式** : Spring AOP 功能的实现。

**单例设计模式** : Spring 中的 Bean 默认都是单例的。

**模板方法模式** : Spring 中 `jdbcTemplate`、`hibernateTemplate` 等以 Template 结尾的对数据库操作的类，它们就使用到了模板模式。

**包装器设计模式** : 我们的项目需要连接多个数据库，而且不同的客户在每次访问中根据需要会去访问不同的数据库。这种模式让我们可以根据客户的需求能够动态切换不同的数据源。

**观察者模式:** Spring 事件驱动模型就是观察者模式很经典的一个应用。

**适配器模式** :Spring AOP 的增强或通知(Advice)使用到了适配器模式、spring MVC 中也是用到了适配器模式适配`Controller`。

## 22. Spring 事务实现方式有哪些？

- 编程式事务管理：这意味着你可以通过编程的方式管理事务，这种方式带来了很大的灵活性，但很难维护。
- 声明式事务管理：这种方式意味着你可以将事务管理和业务代码分离。你只需要通过注解或者XML配置管理事务。

## 23. Spring框架的事务管理有哪些优点？

- 它提供了跨不同事务api（如JTA、JDBC、Hibernate、JPA和JDO）的一致编程模型。

- 它为编程事务管理提供了比JTA等许多复杂事务API更简单的API。

- 它支持声明式事务管理。

- 它很好地集成了Spring的各种数据访问抽象。

## 24. spring事务定义的传播规则

- PROPAGATION_REQUIRED: 支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
- PROPAGATION_SUPPORTS: 支持当前事务，如果当前没有事务，就以非事务方式执行。
- PROPAGATION_MANDATORY: 支持当前事务，如果当前没有事务，就抛出异常。
- PROPAGATION_REQUIRES_NEW: 新建事务，如果当前存在事务，把当前事务挂起。
- PROPAGATION_NOT_SUPPORTED: 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- PROPAGATION_NEVER: 以非事务方式执行，如果当前存在事务，则抛出异常。
- PROPAGATION_NESTED:如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。

## 25. SpringMVC 工作原理了解吗?

**原理如下图所示：**

![img](http://blog-img.coolsen.cn/img/SpingMVC-Process.jpg)

上图的一个笔误的小问题：Spring MVC 的入口函数也就是前端控制器 `DispatcherServlet` 的作用是接收请求，响应结果。

**流程说明（重要）：**

1. 客户端（浏览器）发送请求，直接请求到 `DispatcherServlet`。
2. `DispatcherServlet` 根据请求信息调用 `HandlerMapping`，解析请求对应的 `Handler`。
3. 解析到对应的 `Handler`（也就是我们平常说的 `Controller` 控制器）后，开始由 `HandlerAdapter` 适配器处理。
4. `HandlerAdapter` 会根据 `Handler`来调用真正的处理器开处理请求，并处理相应的业务逻辑。
5. 处理器处理完业务后，会返回一个 `ModelAndView` 对象，`Model` 是返回的数据对象，`View` 是个逻辑上的 `View`。
6. `ViewResolver` 会根据逻辑 `View` 查找实际的 `View`。
7. `DispaterServlet` 把返回的 `Model` 传给 `View`（视图渲染）。
8. 把 `View` 返回给请求者（浏览器）

## 26. 简单介绍 Spring MVC 的核心组件

那么接下来就简单介绍一下 `DispatcherServlet` 和九大组件（按使用顺序排序的）：

| 组件                        | 说明                                                         |
| :-------------------------- | :----------------------------------------------------------- |
| DispatcherServlet           | Spring MVC 的核心组件，是请求的入口，负责协调各个组件工作    |
| MultipartResolver           | 内容类型( `Content-Type` )为 `multipart/*` 的请求的解析器，例如解析处理文件上传的请求，便于获取参数信息以及上传的文件 |
| HandlerMapping              | 请求的处理器匹配器，负责为请求找到合适的 `HandlerExecutionChain` 处理器执行链，包含处理器（`handler`）和拦截器们（`interceptors`） |
| HandlerAdapter              | 处理器的适配器。因为处理器 `handler` 的类型是 Object 类型，需要有一个调用者来实现 `handler` 是怎么被执行。Spring 中的处理器的实现多变，比如用户处理器可以实现 Controller 接口、HttpRequestHandler 接口，也可以用 `@RequestMapping` 注解将方法作为一个处理器等，这就导致 Spring MVC 无法直接执行这个处理器。所以这里需要一个处理器适配器，由它去执行处理器 |
| HandlerExceptionResolver    | 处理器异常解析器，将处理器（ `handler` ）执行时发生的异常，解析( 转换 )成对应的 ModelAndView 结果 |
| RequestToViewNameTranslator | 视图名称转换器，用于解析出请求的默认视图名                   |
| LocaleResolver              | 本地化（国际化）解析器，提供国际化支持                       |
| ThemeResolver               | 主题解析器，提供可设置应用整体样式风格的支持                 |
| ViewResolver                | 视图解析器，根据视图名和国际化，获得最终的视图 View 对象     |
| FlashMapManager             | FlashMap 管理器，负责重定向时，保存参数至临时存储（默认 Session） |

Spring MVC 对各个组件的职责划分的比较清晰。`DispatcherServlet` 负责协调，其他组件则各自做分内之事，互不干扰。

## 27. @Controller 注解有什么用？

`@Controller` 注解标记一个类为 Spring Web MVC **控制器** Controller。Spring MVC 会将扫描到该注解的类，然后扫描这个类下面带有 `@RequestMapping` 注解的方法，根据注解信息，为这个方法生成一个对应的**处理器**对象，在上面的 HandlerMapping 和 HandlerAdapter组件中讲到过。

当然，除了添加 `@Controller` 注解这种方式以外，你还可以实现 Spring MVC 提供的 `Controller` 或者 `HttpRequestHandler` 接口，对应的实现类也会被作为一个**处理器**对象

## 28. @RequestMapping 注解有什么用？

`@RequestMapping` 注解，在上面已经讲过了，配置**处理器**的 HTTP 请求方法，URI等信息，这样才能将请求和方法进行映射。这个注解可以作用于类上面，也可以作用于方法上面，在类上面一般是配置这个**控制器**的 URI 前缀

## 29. @注解间的区别

### 1 @RestController 和 @Controller 有什么区别？

`@RestController` 注解，在 `@Controller` 基础上，增加了 `@ResponseBody` 注解，更加适合目前前后端分离的架构下，提供 Restful API ，返回例如 JSON 数据格式。当然，返回什么样的数据格式，根据客户端的 `ACCEPT` 请求头来决定。

### 2 @RequestMapping 和 @GetMapping 注解的不同之处在哪里？

1. `@RequestMapping`：可注解在类和方法上；`@GetMapping` 仅可注册在方法上
2. `@RequestMapping`：可进行 GET、POST、PUT、DELETE 等请求方法；`@GetMapping` 是 `@RequestMapping` 的 GET 请求方法的特例，目的是为了提高清晰度。

### 3 @RequestParam 和 @PathVariable 两个注解的区别

两个注解都用于方法参数，获取参数值的方式不同，`@RequestParam` 注解的参数从请求携带的参数中获取，而 `@PathVariable` 注解从请求的 URI 中获取

### 4 @Component和@Bean的区别

1. 作用对象不同：`@Component` 注解作用于类，而 `@Bean` 注解作用于方法、
2. `@Component` 通常是通过路径扫描来自动侦测以及自动装配到 Spring 容器中(我们可以使用 `@ComponentScan` 注解定义要扫描的路径从中找出标识了需要装配的类自动装配到 Spring 的 bean 容器中)。`@Bean` 注解通常是我们在标有该注解的方法中定义产生这个 bean，`@Bean` 告诉了 Spring 这是某个类的实例，当我们需要用它的时候还给我。
3. `@Bean` 注解比 `@Component` 注解的自定义性更强，而且很多地方我们只能通过 `@Bean` 注解来注册 bean。比如当我们引用第三方库中的类需要装配到 Spring 容器时，只能通过 `@Bean` 来实现。



## 32. 返回 JSON 格式使用什么注解？

可以使用 **`@ResponseBody`** 注解，或者使用包含 `@ResponseBody` 注解的 **`@RestController`** 注解。

当然，还是需要配合相应的支持 JSON 格式化的 HttpMessageConverter 实现类。例如，Spring MVC 默认使用 MappingJackson2HttpMessageConverter。

## 33. 什么是springmvc拦截器以及如何使用它？

Spring的处理程序映射机制包括处理程序拦截器，当你希望将特定功能应用于某些请求时，例如，检查用户主题时，这些拦截器非常有用。拦截器必须实现org.springframework.web.servlet包的HandlerInterceptor。此接口定义了三种方法：

- preHandle：在执行实际处理程序之前调用。
- postHandle：在执行完实际程序之后调用。
- afterCompletion：在完成请求后调用。

## 34. Spring MVC 和 Struts2 的异同？

**入口**不同

- Spring MVC 的入门是一个 Servlet **控制器**。
- Struts2 入门是一个 Filter **过滤器**。

**配置映射**不同，

- Spring MVC 是基于**方法**开发，传递参数是通过**方法形参**，一般设置为**单例**。
- Struts2 是基于**类**开发，传递参数是通过**类的属性**，只能设计为**多例**。

**视图**不同

- Spring MVC 通过参数解析器是将 Request 对象内容进行解析成方法形参，将响应数据和页面封装成 **ModelAndView** 对象，最后又将模型数据通过 **Request** 对象传输到页面。其中，如果视图使用 JSP 时，默认使用 **JSTL** 。
- Struts2 采用**值栈**存储请求和响应的数据，通过 **OGNL** 存取数据。

## 35. REST 代表着什么?

REST 代表着抽象状态转移，它是根据 HTTP 协议从客户端发送数据到服务端，例如：服务端的一本书可以以 XML 或 JSON 格式传递到客户端

可以看看 [REST API design and development](http://bit.ly/2zIGzWK) ，知乎上的 [《怎样用通俗的语言解释 REST，以及 RESTful？》](https://www.zhihu.com/question/28557115)了解。

## 36. 什么是安全的 REST 操作?

REST 接口是通过 HTTP 方法完成操作

- 一些 HTTP 操作是安全的，如 GET 和 HEAD ，它不能在服务端修改资源
- 换句话说，PUT、POST 和 DELETE 是不安全的，因为他们能修改服务端的资源

所以，是否安全的界限，在于**是否修改**服务端的资源

## 37. REST API 是无状态的吗?

**是的**，REST API 应该是无状态的，因为它是基于 HTTP 的，它也是无状态的

REST API 中的请求应该包含处理它所需的所有细节。它**不应该**依赖于以前或下一个请求或服务器端维护的一些数据，例如会话

**REST 规范为使其无状态设置了一个约束，在设计 REST API 时，你应该记住这一点**

## 38. REST安全吗? 你能做什么来保护它?

安全是一个宽泛的术语。它可能意味着消息的安全性，这是通过认证和授权提供的加密或访问限制提供的

REST 通常不是安全的，需要开发人员自己实现安全机制

## 39. 为什么要用SpringBoot?

在使用Spring框架进行开发的过程中，需要配置很多Spring框架包的依赖，如spring-core、spring-bean、spring-context等，而这些配置通常都是重复添加的，而且需要做很多框架使用及环境参数的重复配置，如开启注解、配置日志等。Spring Boot致力于弱化这些不必要的操作，提供默认配置，当然这些默认配置是可以按需修改的，快速搭建、开发和运行Spring应用。

以下是使用SpringBoot的一些好处：

- 自动配置，使用基于类路径和应用程序上下文的智能默认值，当然也可以根据需要重写它们以满足开发人员的需求。
- 创建Spring Boot Starter 项目时，可以选择选择需要的功能，Spring Boot将为你管理依赖关系。
- SpringBoot项目可以打包成jar文件。可以使用Java-jar命令从命令行将应用程序作为独立的Java应用程序运行。
- 在开发web应用程序时，springboot会配置一个嵌入式Tomcat服务器，以便它可以作为独立的应用程序运行。（Tomcat是默认的，当然你也可以配置Jetty或Undertow）
- SpringBoot包括许多有用的非功能特性（例如安全和健康检查）。

## 40. Spring Boot中如何实现对不同环境的属性配置文件的支持？

Spring Boot支持不同环境的属性配置文件切换，通过创建application-{profile}.properties文件，其中{profile}是具体的环境标识名称，例如：application-dev.properties用于开发环境，application-test.properties用于测试环境，application-uat.properties用于uat环境。如果要想使用application-dev.properties文件，则在application.properties文件中添加spring.profiles.active=dev。

如果要想使用application-test.properties文件，则在application.properties文件中添加spring.profiles.active=test。



## 42. 你如何理解 Spring Boot 中的 Starters？

Starters可以理解为启动器，它包含了一系列可以集成到应用里面的依赖包，你可以一站式集成 Spring 及其他技术，而不需要到处找示例代码和依赖包。如你想使用 Spring JPA 访问数据库，只要加入 spring-boot-starter-data-jpa 启动器依赖就能使用了。

Starters包含了许多项目中需要用到的依赖，它们能快速持续的运行，都是一系列得到支持的管理传递性依赖。

## 43. Spring Boot Starter 的工作原理是什么？

Spring Boot 在启动的时候会干这几件事情：

- Spring Boot 在启动时会去依赖的 Starter 包中寻找 resources/META-INF/spring.factories 文件，然后根据文件中配置的 Jar 包去扫描项目所依赖的 Jar 包。
- 根据 spring.factories 配置加载 AutoConfigure 类
- 根据 @Conditional 注解的条件，进行自动配置并将 Bean 注入 Spring Context

总结一下，其实就是 Spring Boot 在启动的时候，按照约定去读取 Spring Boot Starter 的配置信息，再根据配置信息对资源进行初始化，并注入到 Spring 容器中。这样 Spring Boot 启动完毕后，就已经准备好了一切资源，使用过程中直接注入对应 Bean 资源即可

## 44. 保护 Spring Boot 应用有哪些方法？

- 在生产中使用HTTPS
- 使用Snyk检查你的依赖关系
- 升级到最新版本
- 启用CSRF保护
- 使用内容安全策略防止XSS攻击

## 45. Spring 、Spring Boot 和 Spring Cloud 的关系?

Spring 最初最核心的两大核心功能 Spring Ioc 和 Spring Aop 成就了 Spring，Spring 在这两大核心的功能上不断的发展，才有了 Spring 事务、Spring Mvc 等一系列伟大的产品，最终成就了 Spring 帝国，到了后期 Spring 几乎可以解决企业开发中的所有问题。

Spring Boot 是在强大的 Spring 帝国生态基础上面发展而来，发明 Spring Boot 不是为了取代 Spring ,是为了让人们更容易的使用 Spring 。

Spring Cloud 是一系列框架的有序集合。它利用 Spring Boot 的开发便利性巧妙地简化了分布式系统基础设施的开发，如服务发现注册、配置中心、消息总线、负载均衡、断路器、数据监控等，都可以用 Spring Boot 的开发风格做到一键启动和部署。

Spring Cloud 是为了解决微服务架构中服务治理而提供的一系列功能的开发框架，并且 Spring Cloud 是完全基于 Spring Boot 而开发，Spring Cloud 利用 Spring Boot 特性整合了开源行业中优秀的组件，整体对外提供了一套在微服务架构中服务治理的解决方案。

用一组不太合理的包含关系来表达它们之间的关系。

Spring ioc/aop > Spring > Spring Boot > Spring Cloud



## 46. SpringBoot 常用注解

**@SpringBootApplication**包含以下

**@ComponentScan**	用来自动扫描被这些注解标识的类，最终生成ioc容器里的bean，默认扫描范围是@ComponentScan注解所在配置类包及子包的类
**@SpringBootConfiguration**	与@Configuration作用相同，都是用来声明当前类是一个配置类，这里表明是springboot主类使用的配置类
**@EnableAutoConfiguration**	是springboot实现自动化配置的核心注解，通过这个注解把spring应用所需的bean注入容器中

| **@Repository** | **持久层（dao）注入spring容器**    |
| --------------- | ---------------------------------- |
| @Service        | 业务逻辑层（server）注入spring容器 |
| @Controller     | 控制层（controller）注入spring容器 |
| @Component      | 普通pojo注入spring容器             |

| @ResponseBody   | @ResponseBody的作用其实是将java对象转为json格式的数据。      |
| --------------- | :----------------------------------------------------------- |
| @RestController | 该注解是@Controller和@ResponseBody的结合体，一般用于类，作用等于在类上面添加了@ResponseBody和@Controller |

**@AutoWired**	@Autowired默认按类型装配，如果发现找到多个bean，则按照name方式比对，如果还有多个，则报出异常
**@Qualifier**	spring的注解，按名字注入 一般当出现两个及以上bean时,不知道要注入哪个，结合@AutoWired使用
**@Resource**	默认按名称注入例如@Resource(name = “zhaozhao”)则根据name属性注入找不到则报错，若无name属性则根据属性名称注入，如果匹配不成功则按照类型匹配匹配不成功则报错。

**@RequestMapping**	@RequestMapping（url），通过该注解就可以通过配置的url进行访问，方式可以是get或post请求，两种方式均可
**@GetMapping**	@GetMapping（url） ，功能类似的，只是这个限定了只能是Get请求
**@PostMapping**	@PostMapping（url），功能类似的，只是这个限定了只能是Post请求

**@Value**	用于获取bean的属性，一般用于读取配置文件的数据，作用在变量上
**@ConfigurationProperties**	用于注入Bean属性，然后再通过当前Bean获取注入值，作用在类上
**@PropertySource**	用于指定要读取的配置文件，可以和@Value或@ConfigurationProperties配合使用（**@PropertySource不支持yml文件读取**）

| @Configuration | 作用于类上表示这是一个配置类，可理解为用spring的时候xml里面的< beans>标签 |
| -------------- | ------------------------------------------------------------ |
| @Bean          | 产生bean对象加入容器，作用于方法，可理解为用spring的时候xml里面的标签 |

**@RequestParam**	获取查询参数。即url?name=这种形式
**@PathVariable**	获取路径参数。即url/{id}这种形式。
**@RequestParam**	获取Body的参数，一般用于post获取参数
**@RequestHeader**	获取请求头的信息
**@CookieValue**	获取Cookie的信息

## 47. springboot自动装配（核心注解）

### 1、自动装配是什么及作用

springboot的**自动装配**实际上就是为了从**spring.factories**文件中获取到对应的需要进行自动装配的类，并生成相应的Bean对象，然后将它们交给spring容器来帮我们进行管理

### 2、spring自动装配的原理

#### 2.1、启动类上注解的作用

**@SpringBootApplication**
这个注解是springboot启动类上的一个注解，是一个组合注解，也就是由其他注解组合起来，它的主要作用就是标记说明这个类是springboot的主配置类，springboot应该运行这个类里面的main()方法来启动程序

这个注解主要由三个子注解组成：

- **@SpringBootConfiguration**
- **@EnableAutoConfiguration**
- **@ComponentScan**

##### @SpringBootConfiguration

这个注解包含了@[Configuration](https://so.csdn.net/so/search?q=Configuration&spm=1001.2101.3001.7020)，@Configuration里面又包含了一个@Component注解，也就是说，**这个注解标注在哪个类上，就表示当前这个类是一个配置类，而配置类也是spring容器中的组件**



##### @EnableAutoConfiguration

这个注解是开启自动配置的功能，里面包含了两个注解

- @AutoConfigurationPackage
- @Import（AutoConfigurationImportSelector.class）

**@AutoConfigurationPackage**
这个注解的作用说白了就是将主配置类（@SpringBootApplication标注的类）所在包以及子包里面的所有组件扫描并加载到spring的容器中，这也就是为什么我们在利用springboot进行开发的时候，无论是Controller还是Service的路径都是与主配置类同级或者次级的原因

**@Import(AutoConfigurationImportSelector.class)**

这个注解就是将需要自动装配的类以全类名的方式返回

1、**AutoConfigurationImportSelector**这个类里面有一个方法**selectImports**()

2、在**selectImport**()方法里调用了一个**getAutoConfigurationEntry**()方法，这个方法里面又调用了一个**getCandidateConfigurations**()方法

3、在**getCandidateConfigurations**()方法里面调用了**loadFactoryNames**()方法

4、**loadFactoryNames**()方法里面又调用了一个**loadSpringFactories**()方法

5、关键就在这个**loadSpringFactories**()方法里面，在这个方法里，它会查找所有在**META-INF**路径下的**spring.factories**文件

6、在**META-INF/spring.factories**这个文件里面的数据是以**键=值**的方式存储，然后解析这些文件，找出以**EnableAutoConfiguration**为键的所有值，以列表的方式返回



##### @ComponentScan

这个注解的作用就是扫描当前包及子包的注解



#### 2.2、springboot自动装配的流程

1、在springboot启动的时候会创建一个SpringApplication对象，在对象的构造方法里面会进行一些参数的初始化工作，最主要的是判断当前应用程序的类型以及设置初始化器以及监听器，并在这个过程中会加载整个应用程序的spring.factories文件，将文件中的内容放到缓存当中，方便后续获取；

2、SpringApplication对象创建完成之后会执行run()方法来完成整个应用程序的启动，启动的过程中有两个最主要的方法prepareContext()和refreshContext()，在这两个方法中完成了自动装配的核心功能，在run()方法里还执行了一些包括上下文对象的创建，打印banner图，异常报告期的准备等各个准备工作，方便后续进行调用；

3、在prepareContext()中主要完成的是对上下文对象的初始化操作，包括属性的设置，比如设置环境变量。在整个过程中有一个load()方法，它主要是完成一件事，那就是将启动类作为一个beanDefinition注册到registry，方便后续在进行BeanFactoryPostProcessor调用执行的时候，可以找到对应执行的主类，来完成对@SpringBootApplication、@EnableAutoConfiguration等注解的解析工作；

4、在refreshContext()方法中会进行整个容器的刷新过程，会调用spring中的refresh()方法，refresh()方法中有13个非常关键的方法，来完成整个应用程序的启动。而在自动装配过程中，会调用的关键的一个方法就是invokeBeanFactoryPostProcessors()方法，在这个方法中主要是对ConfigurationClassPostProcessor类的处理，这个类是BFPP（BeanFactoryPostProcessor）的子类，因为实现了BDRPP（BeanDefinitionRegistryPostProcessor）接口，在调用的时候会先调用BDRPP中的postProcessBeanDefinitionRegistry()方法，然后再调用BFPP中的postProcessBeanFactory()方法，在执行postProcessBeanDefinitionRegistry()方法的时候会解析处理各种的注解，包含@PropertySource、@ComponentScan、@Bean、@Import等注解，最主要的是对@Import注解的解析；

5、在解析@Import注解的时候，会有一个getImport()方法，从主类开始递归解析注解，把所有包含@Import的注解都解析到，然后在processImport()方法中对import的类进行分类，例如AutoConfigurationImportSelect归属于ImportSelect的子类，在后续的过程中会调用DeferredImportSelectorHandler类里面的process方法，来完成整个EnableAutoConfiguration的加载。



## 48.Spring事务回滚

因为事务具有ACID特性，当在执行某个方法时，如果方法中有包含对数据库的插入或者更新操作，并且在当前事务上下文抛出了异常，spring会对已经进行的数据库操作“撤销”改变，恢复到原来的状态。

**Spring事务工作原理**
**1）事务开始时，通过AOP机制，生成一个代理的connection对象，并将其放入DataSource实例的某个与DataSourceTransactionManager相关的容器中。在接下来的整个事务中，客户端与数据库的交互都使用该connection。**

**2）事务结束时，回滚在第一步中得到的代理的connection中的数据库命令，然后关闭该代理connection对象。**

Spring事务注解@Transactional
**@Transactional可以作用于接口、接口方法、类以及类方法上。当作用于类时，该类的所有的public方法都将具有事务的特性。但是 Spring 建议不要在接口或者接口方法上使用该注解，因为这只有在使用基于接口的代理时它才会生效。另外，@Transactional注解只能应用到public方法上，作用于protected、private时，会被忽略，也不会抛出任何异常，这是有Spring AOP的本质决定的。**

此外，Spring声明式事务默认会对非检查型异常和运行时异常进行回滚，而对检查型异常不进行回滚操作。Error和RuntimeException及其子类为非检查型异常。如何改变默认规则？

1）让checked异常也进行回滚，需要加上(rollbackFor=Exception.class)

2）让unchecked异常也不回滚，加上(notRollbackFor=RunTimeException.class)

3）不需要事务，@Transactional(propagation=Propagation.NOT_SUPPORTED)

4）如果不添加rollbackFor等属性，Spring碰到Unchecked Exceptions都会回滚，不仅是RuntimeException，也包括Error。

4、虽然配置了rollbackFor属性，但是方法执行失败，还是没有进行回滚
1）类内部方法调用奔雷的其他方法，是不会引起事务的，因为不能被AOP捕获，即使被调用方法使用@Transactional注解进行修饰。

2）抛出异常，但是被catch住了

