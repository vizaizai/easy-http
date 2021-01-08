package demo.main;


import com.github.vizaizai.logging.LoggerFactory;
import org.slf4j.Logger;


public class TestLog {
    private static final Logger log = LoggerFactory.getLogger(TestLog.class);
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        log.warn( "warn33333333333");
        log.debug( "debug11111111111");
        log.info( "info333333333333");
        log.error( "error222222222222");
        log.trace( "trace1111111");
        log.info("123123", new RuntimeException("哈哈哈"));
    }

}
