package top.ntutn;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testTest() {
        System.out.println(System.getProperty("user.home"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        System.out.println(date);
    }

    @Test
    public void helpTest() {
        App.main(new String[] { "-h" });
    }

    @Test
    public void addTest() {
        App.main(new String[] { "添加测试4363447" });
    }

    @Test
    public void showTest() {
        App.main(new String[] {});
    }

    @Test
    public void changeTest(){
        App.main(new String[]{"-c","0"});
    }

    @Test
    public void arrangeTest(){
        App.main(new String[]{"-a"});
    }
}
