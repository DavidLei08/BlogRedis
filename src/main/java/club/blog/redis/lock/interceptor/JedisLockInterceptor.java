package club.blog.redis.lock.interceptor;

import club.blog.redis.lock.GobalLock;
import club.blog.redis.lock.annotation.JedisLock;
import club.blog.redis.lock.annotation.LockKey;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author machenike
 */
public class JedisLockInterceptor implements MethodInterceptor {


    GobalLock gobalLock;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result;
        Method method = methodInvocation.getMethod();
        Object[] args = methodInvocation.getArguments();

        if(method.getAnnotation(JedisLock.class)!=null){
            Parameter[]  parameters = method.getParameters();
            Parameter LockParameter =parameters[0];
            String lockKey =args[1].toString();

            int index = 0;
            out:for(Parameter parameter:parameters){
                Annotation [] annotations =parameter.getAnnotations();
                for(Annotation annotation:annotations){
                    if(annotation instanceof LockKey){
                        LockParameter = parameter;
                       lockKey = args[index].toString();
                        break out;
                    }
                }
                index++;
            }
            gobalLock.lock(lockKey);
            result =  methodInvocation.proceed();
            gobalLock.unlock(lockKey);
        } else {
            result =  methodInvocation.proceed();
        }
        return result;
    }

    public JedisLockInterceptor(GobalLock gobalLock) {
        this.gobalLock = gobalLock;
    }
}
