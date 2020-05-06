package proxy1;

/**
 * Created by mjj on 2017/10/09.
 */

public class ProxyTest {
    public static void main(String[] args) {
        AbstractObject proxy = new ProxyObject(new RealObject());
        proxy.operation();
    }
}
