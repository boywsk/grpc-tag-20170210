package com.gomeplus.grpc.server;

import io.grpc.HandlerRegistry;
import io.grpc.MethodDescriptor;
import io.grpc.ServerMethodDefinition;
import io.grpc.ServerServiceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wangshikai on 16/12/5.
 */
public class MethodHandlerRegistry extends HandlerRegistry {
    private static Logger LOG = LoggerFactory.getLogger(MethodHandlerRegistry.class);
    private final ConcurrentMap<String, ServerServiceDefinition> services = new ConcurrentHashMap();

    @Nullable
    public ServerServiceDefinition addService(ServerServiceDefinition service) {
        return (ServerServiceDefinition)this.services.put(service.getServiceDescriptor().getName(), service);
    }

    public boolean removeService(ServerServiceDefinition service) {
        return this.services.remove(service.getServiceDescriptor().getName(), service);
    }

    @Override
    public ServerMethodDefinition<?, ?> lookupMethod(String methodName, @Nullable String s1) {
        String serviceName = MethodDescriptor.extractFullServiceName(methodName);
        LOG.info("----lookupMethod---- ：ServiceName:{},methodName:{}",serviceName,methodName);
        if(serviceName == null) {
            return null;
        } else {
            ServerServiceDefinition service = (ServerServiceDefinition)this.services.get(serviceName);
            return service == null?null:service.getMethod(methodName);
        }
    }
}
