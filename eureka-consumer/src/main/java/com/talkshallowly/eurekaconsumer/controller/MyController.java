package com.talkshallowly.eurekaconsumer.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class MyController {

    @Value("${server.port}")
    String  port;

    @Autowired
    DiscoveryClient discoveryClient;

    @Qualifier("eurekaClient")
    @Autowired
    EurekaClient eurekaClient;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Resource
    RestTemplate restTemplate;

    @GetMapping("getMsg")
    public String getMsg(){
        return "consumer端口： " + port;
    }

    @GetMapping("/client")
    public String getClient(){
        String description = discoveryClient.description();
        System.out.println("description = " + description);
        List<ServiceInstance> provider = discoveryClient.getInstances("provider");
        int order = discoveryClient.getOrder();
        System.out.println("order = " + order);
        List<String> services = discoveryClient.getServices();
        services.stream().forEach((msg) -> {
            System.out.println("services : " + msg);
        });
       return "ok";
    }

    @GetMapping("/eurekaClient")
    public String eurekaClient(){
        List<InstanceInfo> provider = eurekaClient.getInstancesByVipAddress("eureka-provider", true);
        System.out.println("++++++++");
        provider.stream().forEach(System.out::println);
        System.out.println("+++++++++++");
        EurekaClientConfig eurekaClientConfig = eurekaClient.getEurekaClientConfig();
        System.out.println("eurekaClientConfig.getEurekaServerPort() = " + eurekaClientConfig.getEurekaServerPort());
        System.out.println("eurekaClientConfig.getEurekaServerDNSName() = " + eurekaClientConfig.getEurekaServerDNSName());
        System.out.println("eurekaClientConfig.getEurekaServerURLContext() = " + eurekaClientConfig.getEurekaServerURLContext());
        provider.stream().forEach((msg) -> {
            System.out.println("msg.getAppName() = " + msg.getAppName());
            System.out.println("msg.getHealthCheckUrl() = " + msg.getHealthCheckUrl());
            System.out.println("msg.getHostName() = " + msg.getHostName());
            System.out.println("msg.getInstanceId() = " + msg.getInstanceId());
            System.out.println("msg.getPort() = " + msg.getPort() + " -- " + msg.getSecurePort() + " -- " + msg.getId() + " -- " + msg.getIPAddr());
            System.out.println("msg.getMetadata().toString() = " + msg.getMetadata().toString());

        });
        return "ok";
    }


    @GetMapping("/eurekaClient2")
    public String eurekaClient2(){
        List<InstanceInfo> provider = eurekaClient.getInstancesByVipAddress("eureka-provider", true);
        String url = null;
        for (InstanceInfo instanceInfo : provider) {
            url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort()  + "/getMsg";
            System.out.println("url = " + url);
//            RestTemplate restTemplate = new RestTemplate();
            String forObject = restTemplate.getForObject(url, String.class);
            System.out.println("forObject = " + forObject);
        }
        return "ok";
    }

    @GetMapping("/eurekaClient3")
    public String eurekaClient3(){

        ServiceInstance choose = loadBalancerClient.choose("eureka-provider");
        System.out.println("choose.getMetadata().toString() = " + choose.getMetadata().toString());
        System.out.println("choose.getInstanceId() = " + choose.getInstanceId());
        String url = "http://" + choose.getHost() + ":" + choose.getPort()  + "/getMsg";
        System.out.println("url = " + url);
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        System.out.println("forObject = " + forObject);
        return "ok";
    }


}
