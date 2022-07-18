package com.cw.yhz.WebService.server.impl;


import com.cw.yhz.WebService.pojo.User;
import com.cw.yhz.WebService.server.ICommonService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * web service 实现类
 */
@WebService(serviceName = "CommonService", // 与接口中指定的name一致
        targetNamespace = "http://server.WebService.utildemo.ouhl.com/", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.ouhl.utildemo.WebService.server.ICommonService"// 接口地址
)
@Component
public class CommonServiceImpl implements ICommonService {

    @Override
    public String sayHello(String name) {
        return "Hello ," + name;
    }

    @Override
    public User getUser(String name) {
        return new User(name,0,"");
    }
}
