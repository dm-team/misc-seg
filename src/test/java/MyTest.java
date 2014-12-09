import com.dmteam.config.SystemConfig;
import org.junit.Test;

/**
 * Created by Administrator on 2014/12/8.
 */
public class MyTest {

    @Test
    public void osBits() {
        System.out.println(System.getProperties().getProperty("os.arch"));

        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        System.out.println(arch);
        System.out.println(wow64Arch);

        System.out.println(System.getProperty("sun.arch.data.model"));
        System.out.println(SystemConfig.NLPIRModuleName());

    }


    @Test
    public void osName() {
        System.out.println(System.getProperties().getProperty("os.name"));
    }

}
