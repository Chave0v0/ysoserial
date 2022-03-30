package ysoserial.test.payloads;


import org.junit.Assert;
import ysoserial.exploit.JRMPListener;
import ysoserial.test.CustomTest;

import javax.management.BadAttributeValueExpException;
import java.util.concurrent.Callable;


/**
 * @author mbechler
 */
public class JRMPReverseConnectTest implements CustomTest {

    private int port;


    /**
     *
     */
    public JRMPReverseConnectTest() {
        // some payloads cannot specify the port
        port = 1099;
    }


    public void run(Callable<Object> payload) throws Exception {
        JRMPListener l = new JRMPListener(port, new BadAttributeValueExpException("foo"));
        Thread t = new Thread(l, "JRMP listener");
        try {
            t.start();
            try {
                payload.call();
            } catch (Exception e) {
                // ignore
            }
            Assert.assertTrue("Did not have connection", l.waitFor(1000));
        } finally {
            l.close();
            t.interrupt();
            t.join();
        }
    }


    public String getPayloadArgs() {
        return "rmi:localhost:" + port;
    }

}
