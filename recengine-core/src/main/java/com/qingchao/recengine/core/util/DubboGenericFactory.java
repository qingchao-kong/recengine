package com.qingchao.recengine.core.util;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * dubbo 泛化调用管理
 */
public class DubboGenericFactory {


    private static final ApplicationConfig APPLICATION_CONFIG = new ApplicationConfig();

    public static GenericService get(String interfaceName, String interfaceVersion, String name, String protocol, String address) {

        boolean b = StringUtils.isBlank(address);
        APPLICATION_CONFIG.setName(name);
        APPLICATION_CONFIG.setQosEnable(true);
        // 引用远程服务
        // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(APPLICATION_CONFIG);
        RegistryConfig registryConfig = RegistryConfigFactory.get(address);
        if (registryConfig != null) {
            reference.setRegistry(registryConfig);
        } else {
            return null;
        }
        reference.setTimeout(1000);
        reference.setCheck(false);
        //声明为泛化接口
        reference.setGeneric(true);
        if (StringUtils.isNotBlank(interfaceVersion)) {
            reference.setVersion(interfaceVersion);
        }
        // 弱类型接口名
        reference.setInterface(interfaceName);
        reference.setProtocol("dubbo");
        reference.setRetries(0);
        // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用
        return GenericServiceFactory.get(reference);
    }


    static class GenericServiceFactory {
        public static GenericService get(ReferenceConfig<GenericService> reference) {
            ReferenceConfigCache configCache = ReferenceConfigCache.getCache();
            return configCache.get(reference);
        }
    }

    static class RegistryConfigFactory {
        private static final ConcurrentMap<String, RegistryConfig> ZK_ADDRESS_MAP = new ConcurrentHashMap<>();

        public static RegistryConfig get(String zookeeperAddress) {
            if (StringUtils.isBlank(zookeeperAddress)) {
                return null;
            }
            RegistryConfig registryConfig = ZK_ADDRESS_MAP.get(zookeeperAddress);

            if (registryConfig == null) {
                RegistryConfig registry = new RegistryConfig();
                registry.setAddress(zookeeperAddress);
                registry.setProtocol("zookeeper");
                registry.setCheck(false);
                ZK_ADDRESS_MAP.putIfAbsent(zookeeperAddress, registry);
            }
            return ZK_ADDRESS_MAP.get(zookeeperAddress);
        }
    }

}