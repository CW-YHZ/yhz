package com.cw.yhz.WebService.conf;


import com.cw.yhz.WebService.server.ICommonService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * web server 配置类
 *
 * 注意：如出现添加 web server 模块导致无法启动项目的情况，
 *      通常是 POM 中的 parent 版本和 jaxws 版本相冲突。
 *
 * 注意：这里把Commonservice接口发布在了路径/services/CommonService下，wsdl文档路径为http://localhost:{port}/services/CommonService?wsdl
 *          如果想自定义wsdl的访问url，那么可以在application.yml中自定义：
 *             cxf:
 *                path: /services  # 替换默认的/services路径
 */
@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    ICommonService commonService;

    /**
     * JAX-WS
     **/
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, commonService);
        endpoint.publish("/CommonService");
        return endpoint;
    }

}
