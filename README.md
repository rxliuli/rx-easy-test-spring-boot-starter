# rx-easy-test-spring-boot-starter

## 简介

Spring Boot 测试已经简化了很多，但还是有很多模板注解要写，所以吾辈创建了这个仓库将一些常见的基于 Spring 的测试封装成了几个 Base 基类，只需要继承就能直接测试 Spring 相关类。

## 使用

### 安装

添加仓库

```xml
<repositories>
  <repository>
    <id>rx-easy-test-spring-boot-starter</id>
    <url>
      https://raw.githubusercontent.com/rxliuli/rx-easy-test-spring-boot-starter/mvn-repo
    </url>
  </repository>
</repositories>
```

复制以下 xml 配置到 `pom.xml` 中即可

```xml
<dependency>
  <groupId>com.rxliuli</groupId>
  <artifactId>rx-easy-test-spring-boot-starter</artifactId>
  <version>0.0.3</version>
  <scope>test</scope>
</dependency>
```

## 测试普通的 SpringBean

只要继承 `BaseTest` 就会获得最基本的 Spring 环境

```java
// com.rxliuli.rx.easy.test.base.SimpleTest
class SimpleTest extends BaseTest {
    @Autowired
    private ApplicationContext context;
    @Autowired(required = false)
    private DemoComponent demoComponent;

    @Test
    void testApplicationContext() {
        assertThat(context)
                .isNotNull();
    }

    @Test
    void testGetDemoComponent() {
        assertThat(demoComponent)
                .isNotNull();
    }
}
```

## 测试 Dao/Service

继承 `BaseService<T>` 加载 Spring 环境并自动注入需要测试的 Bean

```java
// com.rxliuli.rx.easy.test.base.data.service.UserServiceTest
class UserServiceTest extends BaseServiceTest<UserService> {
    @Test
    void testTransactional() {
        final String username = "琉璃";
        // 第一条数据没有错误能插入成功
        final User user1 = RandomDataUtil.random(User.class).setUsername(username);
        // 第二条数据存在错误
        final User user2 = RandomDataUtil.random(User.class).setUsername(null);
        boolean result = false;
        try {
            // 发生异常导致回滚
            result = base.insertBatch(list(user1, user2));
        } catch (Exception e) {
            log.error("insert error: {}", e);
        }
        assertThat(result).isFalse();

        // 两条数据都没有被插入
        final List<User> list = base.list();
        log.info("list: {}", list);
        assertThat(list.stream().anyMatch(u -> Objects.equals(u.getUsername(), username)))
                .isFalse();
    }
}
```

## 测试 API

### 测试单个 API 类

继承 `BaseWebUnitTest` 并自动注入指定的控制器，然后使用 `restMockMvc` 测试注入控制器中的 API 吧！

如果你只需要测试单个 Controller 就尽量使用这个，测试运行速度上有明显的提升
注：这个测试基类只能测试注入 Controller 的 url, 如果你测试其他的 Controller 则会发生错误
{@code java.lang.AssertionError: Range for response status value 404}

```java
// com.rxliuli.rx.easy.test.base.data.web.UserApiForUnitTest
class UserApiForUnitTest extends BaseWebUnitTest<UserApi> {
    @Test
    void insert() throws Exception {
        final String username = "琉璃";
        final User user = RandomDataUtil.random(User.class).setUsername(username);
        restMockMvc.perform(
                post("/api/user/insert")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(om.writeValueAsString(user))
        )
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void list() throws Exception {
        restMockMvc.perform(get("/api/user/list"))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试其他 API
     */
    @Test
    void testOtherApi() throws Exception {
        boolean result;
        try {
            restMockMvc.perform(get("/"));
            result = true;
        } catch (AssertionError e) {
            log.error("request error: {} {} ", e.getMessage(), e.getClass());
            result = false;
        }
        assertThat(result)
                .isFalse();
    }
}
```

### 测试多个 API 类

继承 `BaseWebUnitTest` 并自动加载完全模拟的 Web 环境，然后使用 `restMockMvc` 测试项目中所有的 API 吧！

```java
// com.rxliuli.rx.easy.test.base.data.web.UserApiForIntegratedTest
class UserApiForIntegratedTest extends BaseWebIntegratedTest {
    @Test
    void insert() throws Exception {
        final String username = "琉璃";
        final User user = RandomDataUtil.random(User.class).setUsername(username);
        restMockMvc.perform(
                post("/api/user/insert")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(om.writeValueAsString(user))
        )
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void list() throws Exception {
        restMockMvc.perform(get("/api/user/list"))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试其他 API
     */
    @Test
    void testOtherApi() throws Exception {
        restMockMvc.perform(get("/"))
                .andExpect(jsonPath("$.data").value("index"));
    }
}
```

### 手动加载指定的 Bean

在项目比较庞大的时候，如果我们想要单独对某个 Bean 进行测试，那么可以手动加载 Bean 避免默认加载整个项目的 Spring 环境。

下面通过比较来查看一下自动与手动的区别

测试获取指定类型的 Bean 并且自动扫描依赖的 Bean

```java
// com.rxliuli.rx.easy.test.base.GetSpecifyClassBeanForAutoScanDependenciesTest
class GetSpecifyClassBeanForAutoScanDependenciesTest extends BaseTest {
    @Autowired(required = false)
    private SpringBeanUtil springBeanUtil;
    @Autowired(required = false)
    private SpringConfigUtil springConfigUtil;
    @Autowired(required = false)
    private DemoComponent demoComponent;

    @Test
    void testGetBean() {
        // 扫描不到
        assertThat(springBeanUtil).isNull();
        // 扫描不到
        assertThat(springConfigUtil).isNull();
        // 因为在 Application 或其子包下所以可以扫描到
        assertThat(demoComponent).isNotNull();
    }
}
```

测试获取指定类型的 Bean 并且手动声明测试所依赖的 Bean

```java
// com.rxliuli.rx.easy.test.base.GetSpecifyClassBeanForManualDeclaringDependenciesTest
@SpringBootTest(classes = {SpringBeanUtil.class})
class GetSpecifyClassBeanForManualDeclaringDependenciesTest extends BaseTest {
    @Autowired(required = false)
    private SpringBeanUtil springBeanUtil;
    @Autowired(required = false)
    private SpringConfigUtil springConfigUtil;
    @Autowired(required = false)
    private DemoComponent demoComponent;

    @Test
    void testGetBean() {
        // 手动声明所以可以拿到
        assertThat(springBeanUtil).isNotNull();
        // 否则拿不到
        assertThat(springConfigUtil).isNull();
        // 本该可以自动拿到的 Bean 也拿不到，因为没有手动声明
        // 一旦手动声明依赖，则自动扫描就失效了，对于测试一些简单依赖的 Bean 有时候会在速度上很有优势
        assertThat(demoComponent).isNull();
    }
}
```

项目目录结构

```sh
# /src/test/java/com/rxliuli/rx/easy/test
├── base
│   ├── data
│   │   ├── bean
│   │   │   └── DemoComponent.java
│   ├── Application.java
│   ├── GetSpecifyClassBeanForAutoScanDependenciesTest.java
│   ├── GetSpecifyClassBeanForManualDeclaringDependenciesTest.java
└── spring
    ├── SpringBeanUtil.java
    └── SpringConfigUtil.java
```

可以看到，本来启动类 Application 是扫描不到 _/spring_ 下的，但如果我们在 `@SpringBootTest` 中声明它，就能顺利的加载到它。与此同时，默认的扫描将会失效，这在测试一些工具类（例如上面的 `SpringBeanUtil`, `SpringConfigUtil`）在速度上有些优势，但在一般 `Dao/Service/API` 测试中则不被推荐。因为 Dao/Service/API 除了依赖于基本的 Spring 环境之外，通常还依赖于非常多的其他东西。

例如上面的 `UserService`，若想调用成功，则首先需要至少以下几个 Bean 创建成功才行

- `UserServiceImpl`
- `UserDaoImpl`
- `JdbcTemplate`
- `ManualSpecifyDataSourceConfig`

所以对于复杂的 Bean 仍然推荐使用直接继承 `BaseServiceTest` 进行测试。

## 在没有 Application 启动类的模块测试

有时候我们需要在没有 Application 启动类的模块进行测试（例如这个项目本身），则可以在 _/src/test/java_ 中创建一个 Application 启动类（就像这个项目的测试一样），然后就会默认使用这个启动啦

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 一些其他的工具

- `ListGenerator#generate`: 测试数据 List 生成
- `RandomDataUtil#random`: 生成一个指定类型的对象（属性值随机生成）

```java
class UserApiForUnitTest extends BaseWebUnitTest<UserApi> {
    @Autowired
    private UserService userService;
    /**
     * 测试反序列化 list
     */
    @Test
    void listOfDeserialize() throws Exception {
        final int num = 10;
        // 生成测试数据
        final List<User> list = generate(num, i -> random(User.class));
        final boolean updateResult = userService.insertBatch(list);
        assertThat(updateResult)
                .isTrue();
        final String str = restMockMvc.perform(get("/api/user/list"))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();
        // 忽略未知的属性，这里主要是因为 User 中包含了 LocalDateTime 类型而又没有配置全局 json 转换器的缘故
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        // 使用配置好的 ObjectMapper 对象进行反序列化
        final Result<List<User>> result = om.readValue(str, new TypeReference<Result<List<User>>>() {
        });
        assertThat(result.getData())
                .hasSize(num)
                .allMatch(u1 -> list.stream().anyMatch(u2 ->
                        Objects.equals(u1.getUsername(), u2.getUsername())
                                && Objects.equals(u1.getPassword(), u2.getPassword())
                ));
    }
}
```

## 要点

- **rx-easy-test** 基于 [junit5](https://junit.org/junit5/)，但同时保留了 [junit4](https://junit.org/junit4/) 的依赖，所以不需要更改之前的测试代码即可引入
- 熟练使用 [assertj](http://joel-costigliola.github.io/assertj/) 断言将对测试有很大的帮助

## 还有问题？

如果还有什么问题或者发现了什么 bug 的话，可以在 [Issues](https://github.com/rxliuli/rx-easy-test-spring-boot-starter/issues) 提出，当然，也欢迎所有 [Pull requests](https://github.com/rxliuli/rx-easy-test-spring-boot-starter/pulls)。
