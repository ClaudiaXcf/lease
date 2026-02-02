
import com.greatxcf.lease.AdminWebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AdminWebApplication.class)
public class TestALiYunOSS {
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Test
    public void test() {
        System.out.println("AK = " + accessKeyId);
    }

    @Test
    public void test1() {
        System.out.println("Env: " + System.getenv("ALIYUN_OSS_ACCESS_KEY_ID"));
        System.out.println("YML: " + accessKeyId);
    }
}
