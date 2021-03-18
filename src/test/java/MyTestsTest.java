import mytests.MakeTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MyTestsTest {
    @CsvSource({"MyTest, true",
                "qwe, false"})
    @ParameterizedTest
    public void TestStartByClassName(String testClassName, boolean expected){
        try {
            MakeTest.start(testClassName);
        } catch (Exception e) {
            if(!(e instanceof RuntimeException && expected))
                Assertions.assertTrue(true);
            else {
                e.printStackTrace();
                Assertions.fail();
            }
        }
    }

    @Test
    public void TestStartByClass() {
        try {
            MakeTest.start(MyTest.class);
        } catch (Exception e) {
            Assertions.fail();
            e.printStackTrace();
        }
    }
}
