import mytests.*;

public class MyTest {
    @BeforeSuite
    public void before() {
        System.out.println("MyTest -> before");
    }
    @Test(priority = 1)
    public void makeTest_1() {
        System.out.println("MyTest -> makeTest_1");
    }
    @Test(priority = 2)
    public void makeTest_2() {
        System.out.println("MyTest -> makeTest_2");
    }
    @AfterSuite
    public void after() {
        System.out.println("MyTest -> after");
    }
}
