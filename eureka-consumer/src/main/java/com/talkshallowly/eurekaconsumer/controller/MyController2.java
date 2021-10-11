package com.talkshallowly.eurekaconsumer.controller;


import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class MyController2 {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Qualifier("eurekaClient")
    @Autowired
    EurekaClient eurekaClient;

    @Autowired
    DiscoveryClient discoveryClient;

    @Resource
    RestTemplate  restTemplate;


}
