package proxy1;

/**
 * Created by mjj on 2017/10/09.
 */

public class RealObject extends AbstractObject {
    @Override
    protected void operation() {
        System.out.println("do operation...");
    }
}
