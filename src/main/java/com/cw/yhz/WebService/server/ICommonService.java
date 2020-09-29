package com.cw.yhz.WebService.server;

import com.ouhl.utildemo.WebService.pojo.User;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * web service 接口类
 * 路径：http://localhost:8080/services/CommonService?wsdl
 */
@WebService(name = "CommonService", // 透过名称访问
        targetNamespace = "http://model.webservice.xncoding.com/"// 命名空间,一般是接口的包名倒序
)public interface ICommonService {

    @WebMethod
    public String sayHello(@WebParam(name = "userName") String name);

    @WebMethod
    public User getUser(@WebParam(name = "userName") String name);
}
