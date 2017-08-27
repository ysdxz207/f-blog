package proxy;

import java.lang.reflect.*;

interface Logic
{
    public void doSomeBusinessLogic();
}

class BusinessLogic implements Logic
{
    public void doSomeBusinessLogic()
    {
        System.out.println("in method doSomeBusinessLogic()");
    }
}

class ProxyHandler implements InvocationHandler
{
    private Object target;

    public ProxyHandler(Object target)
    {
        this.target = target;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception
    {
        Object result;
        before();
        result = method.invoke(target, args);
        after();
        return result;
    }
    private void before()
    {
        System.out.println("before method doSomeBusinessLogic()");
    }
    private void after()
    {
        System.out.println("after method doSomeBusinessLogic()");
    }
}

public class TestProxy
{
    public static void main(String[] args)
    {
        Logic logic = new BusinessLogic();
        InvocationHandler h = new ProxyHandler(logic);
        Logic proxy = (Logic) Proxy.newProxyInstance(Logic.class.getClassLoader(),logic.getClass().getInterfaces(), h);
        proxy.doSomeBusinessLogic();
    }
}