package com.cw.yhz.ExcelDemo;


import com.cw.yhz.ExcelDemo.annotation.ExcelDictFormat;
import com.cw.yhz.ExcelDemo.pojo.Customer;
import com.cw.yhz.ExcelDemo.pojo.CustomerVo;
import com.cw.yhz.ExcelDemo.pojo.SysDictData;
import com.cw.yhz.ExcelDemo.utils.DictUtils;
import com.cw.yhz.ExcelDemo.utils.ExcelFillCellMergeStrategy;
import com.cw.yhz.ExcelDemo.utils.ExcelUtil;
import com.cw.yhz.ExcelDemo.utils.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/excel")
public class ExcelDemo {

    /**
     * 客户导入模板下载
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        Map<Integer, List<String>> selectMap = new HashMap<>();
        // 获取类变量注解：
        Field[] fields = CustomerVo.class.getDeclaredFields();
        for (Field f : fields) {
            if(f.isAnnotationPresent(ExcelDictFormat.class)){
                List<String> dictTypelist= DictUtils.getDictCache(f.getAnnotation(ExcelDictFormat.class).dictType()).stream().map(SysDictData::getDictLabel).collect(Collectors.toList());
                selectMap.put(f.getAnnotation(ExcelDictFormat.class).columnNum(),dictTypelist);
            }
        }
        ExcelUtil.download("客户基础信息", CustomerVo.class, response, selectMap);
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, Customer customer) {
        /**
         * 查询要导出数据：
         * 临时使用list代替
         */
        //List<CustomerVo> list = customerService.getCustomerAndWaste(customer);
        List list=new ArrayList();

        //合并多列，直接逗号分隔：int[] mergeColumnIndex = {0,1,2}
        int[] mergeColumnIndex = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        //需要从第几行开始合并
        int mergeRowIndex = 1;
        ExcelUtil.exportExcel(list, "客户基础信息", CustomerVo.class, response, new ExcelFillCellMergeStrategy(mergeRowIndex, mergeColumnIndex));
    }

    /**
     * 导入客户信息列表
     */
    @PostMapping("/import")
    public String importCustomer(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        List<CustomerVo> list = ExcelUtil.importExcel(inputStream, CustomerVo.class);

        //获取到数据后的处理
        String message = importCustomer(list);
        //返回给前端的信息
        return message;
    }


    /**
     * 导入客户基本信息
     */
    public String importCustomer(List<CustomerVo> list) {
        if (StringUtils.isNull(list) || list.size() == 0) {
            throw new ServiceException("导入客户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        //处理合并后的空行
//        for (int i = 0; i < list.size(); i++) {
//            CustomerVo customerNow = list.get(i);
//            if (StringUtils.isBlank(customerNow.getCustomerName()) && StringUtils.isNotBlank(customerNow.getWasteName())) {
//                CustomerVo customerOld = list.get(i - 1);
//                CustomerMaterial customerMaterial = new CustomerMaterial();
//                //获取物料属性
//                BeanUtils.copyProperties(customerNow, customerMaterial);
//                //为空行赋值上一条客户属性(物料信息为上一条客户下)
//                BeanUtils.copyProperties(customerOld, customerNow);
//                //将对象物料信息覆盖
//                BeanUtils.copyProperties(customerMaterial, customerNow, "customerNo","customerName");
//            }
//        }
//
//        //将多个一对一分组为多个一对多
//        Map<CustomerGroupModel, List<CustomerVo>> customerListMap = list.stream().collect(Collectors.groupingBy(s -> {
//            CustomerGroupModel customerGroupModel = new CustomerGroupModel();
//            BeanUtils.copyProperties(s, customerGroupModel);
//            return customerGroupModel;
//        }, LinkedHashMap::new, Collectors.toList()));
//
//        List<String> custNos = new ArrayList<>();
//        for (Map.Entry<CustomerGroupModel, List<CustomerVo>> customerEnt : customerListMap.entrySet()) {
//            CustomerGroupModel customerGroupModel = customerEnt.getKey();
//            List<CustomerVo> customerVoList = customerEnt.getValue();
//            Map<String, String> wasteNoMap = new HashMap<>();
//            try {
//                Customer customer = customerMapper.selectCustomerByNo(customerGroupModel.getCustomerNo(), null);
//                if (customer != null) {
//                    BeanUtils.copyProperties(customerGroupModel, customer);
//                    ValidatorUtils.validate(customer, Update.class);
//                    if (customer.getCustomerDistance() != null) {
//                        Pattern pattern = Pattern.compile("^[1-9]\\d{0,7}(\\.\\d{1,2})?$|^0(\\.\\d{1,2})?$"); // 允许输入整数12位，小数2位的金额！不能输入0开头的整数，如：0100
//                        Matcher isNum = pattern.matcher(customer.getCustomerDistance().toString());
//                        if (!isNum.matches()) {
//                            throw new RuntimeException("客户距离整数位长度不能超过8且小数位不能超过2");
//                        }
//                    }
//                    this.updateCustomer(customer);
//                    for (CustomerVo vo : customerVoList) {
//                        if (StringUtils.isNotBlank(vo.getWasteName())) {
//                            CustomerMaterial material = customerMaterialMapper.selectCustomerMaterialByWasteNo(vo.getWasteNo());
//                            if (material != null) {
//                                BeanUtils.copyProperties(vo, material);
//                                ValidatorUtils.validate(material, Update.class);
//                                customerMaterialService.updateCustomerMaterial(material);
//                            } else {
//                                material = new CustomerMaterial();
//                                R<String> wasteNo = remoteCodeFillRuleService.getCustomerCodeForArea((wasteNoMap.size() == 0 ? customer.getCustomerNo() : wasteNoMap.get("wasteNo")), DictConstant.SNCODE_GEN_TYPE_HW, "", "", SecurityConstants.INNER);
//                                vo.setWasteNo(wasteNo.getData());
//                                wasteNoMap.put("wasteNo", wasteNo.getData());
//                                BeanUtils.copyProperties(vo, material);
//                                material.setCustomerId(customer.getTuid());
//                                ValidatorUtils.validate(material, Insert.class);
//                                customerMaterialService.insertCustomerMaterial(material);
//                            }
//                        }
//                    }
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、客户 " + customerGroupModel.getCustomerName() + " 更新成功");
//                } else {
//                    customer = new Customer();
//                    BeanUtils.copyProperties(customerGroupModel, customer);
//                    ValidatorUtils.validate(customer, Insert.class);
//                    String preName = customer.getCounty().substring(0, customer.getCounty().length() - 1);
//                    String preHead = PinyinConvertUtil.getPinYinHeadChar(preName);
//
//                    R<String> customerNo = remoteCodeFillRuleService.getCustomerCodeForArea((custNos.size() == 0 || !custNos.stream().anyMatch(str -> str.substring(0, preHead.length()).equals(preHead)) ? customerGroupModel.getCounty() : custNos.get(custNos.size() - 1)), DictConstant.SNCODE_GEN_TYPE_CU, customerGroupModel.getProvince(), customerGroupModel.getCity(), SecurityConstants.INNER);
//                    customer.setCustomerNo(customerNo.getData());
//                    custNos.add(preHead + "-" + customerNo.getData());
//                    ValidatorUtils.validate(customer, Insert.class);
//                    Pattern pattern = Pattern.compile("^[1-9]\\d{0,7}(\\.\\d{1,2})?$|^0(\\.\\d{1,2})?$"); // 允许输入整数12位，小数2位的金额！不能输入0开头的整数，如：0100
//                    if (customer.getCustomerDistance() != null) {
//                        Matcher isNum = pattern.matcher(customer.getCustomerDistance().toString());
//                        if (!isNum.matches()) {
//                            throw new RuntimeException("客户距离整数位长度不能超过8且小数位不能超过2");
//                        }
//                    }
//                    this.insertCustomer(customer);
//                    Map<String, String> wasteNoMap2 = new HashMap<>();
//                    for (CustomerVo vo : customerVoList) {
//                        //编号为自动生成 按名称判断是否执行新增
//                        if (StringUtils.isNotBlank(vo.getWasteName())) {
//                            R<String> wasteNo = remoteCodeFillRuleService.getCustomerCodeForArea((wasteNoMap2.size() == 0 ? customer.getCustomerNo() : wasteNoMap2.get("wasteNo")), DictConstant.SNCODE_GEN_TYPE_HW, "", "", SecurityConstants.INNER);
//                            vo.setWasteNo(wasteNo.getData());
//                            wasteNoMap2.put("wasteNo", wasteNo.getData());
//                            CustomerMaterial material = new CustomerMaterial();
//                            BeanUtils.copyProperties(vo, material);
//                            material.setCustomerId(customer.getTuid());
//                            ValidatorUtils.validate(material, Insert.class);
//                            customerMaterialService.insertCustomerMaterial(material);
//                        }
//                    }
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、客户 " + customerGroupModel.getCustomerName() + " 导入成功");
//                }
//            } catch (Exception e) {
//                failureNum++;
//                String msg = "<br/>" + failureNum + "、客户 " + customerGroupModel.getCustomerName() + " 导入失败：";
//                failureMsg.append(msg + e.getMessage());
//                log.error(msg, e);
//            }
//        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
