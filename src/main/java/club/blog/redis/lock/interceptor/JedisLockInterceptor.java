package club.blog.redis.lock.interceptor;

import club.blog.redis.lock.GlobalLock;
import club.blog.redis.lock.annotation.JedisLock;
import club.blog.redis.lock.annotation.LockKey;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 分布式拦截器
 * @author machenike
 */
public class JedisLockInterceptor implements MethodInterceptor {


    GlobalLock globalLock;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result;
        //方法取得
        Method method = methodInvocation.getMethod();
        //实参取得
        Object[] args = methodInvocation.getArguments();

        //判断方法注解
        if(method.getAnnotation(JedisLock.class)!=null){
            Parameter[]  parameters = method.getParameters();
            Parameter LockParameter =parameters[0];
            String lockKey =args[1].toString();

            int index = 0;
            //迭代形参
            out:for(Parameter parameter:parameters){
                Annotation [] annotations =parameter.getAnnotations();
                //判断形参注解
                for(Annotation annotation:annotations){
                    if(annotation instanceof LockKey){
                        LockParameter = parameter;
                       lockKey = args[index].toString();
                        break out;
                    }
                }
                index++;
            }
            //取锁
            globalLock.lock(lockKey);
            result =  methodInvocation.proceed();
            //释放锁
            globalLock.unlock(lockKey);
        } else {
            result =  methodInvocation.proceed();
        }
        return result;
    }

    public JedisLockInterceptor(GlobalLock globalLock) {
        this.globalLock = globalLock;
    }
}
