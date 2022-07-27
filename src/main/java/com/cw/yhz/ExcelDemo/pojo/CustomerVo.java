package com.cw.yhz.ExcelDemo.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.cw.yhz.ExcelDemo.annotation.ExcelDictFormat;
import com.cw.yhz.ExcelDemo.convert.ExcelDictConvert;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 客户基本信息对象 CustomerVo
 *
 * @author mrp
 * @date 2022-01-17
 */
@Data
@ContentRowHeight(20) // 文本行高度
@HeadRowHeight(20) // 标题高度
@ColumnWidth(22) // 默认列宽度
@ExcelIgnoreUnannotated
@HeadFontStyle(color = 1) //表格头字体样式
@HeadStyle(fillForegroundColor = 57) //表哥头样式    fillForegroundColor：背景色
public class CustomerVo {
    private static final long serialVersionUID = 1L;

    /**
     * 客户编号
     */
    @ExcelProperty(value = "*客户编号", index = 0)
    private String customerNo;

    /**
     * 客户名称
     */
    @ExcelProperty(value = "*客户名称", index = 1)
    private String customerName;

    /**
     * 邮编
     */
    @ExcelProperty(value = "*邮编", index = 2)
    private String postcode;


    /**
     * 客户电话
     */
    @ExcelProperty(value = "*客户电话", index = 3)
    private String customerPhone;

    /**
     * 客户Email
     */
    @ExcelProperty(value = "客户Email", index = 4)
    private String customerEmail;

    /**
     * 客户联系人
     */
    @ExcelProperty(value = "*客户联系人", index = 5)
    private String customerContact;

    /**
     * 联系人电话
     */
    @ExcelProperty(value = "*联系人电话", index = 6)
    private String contactTel;

    /**
     * 联系人Email
     */
    @ExcelProperty(value = "联系人Email", index = 7)
    private String contactEmail;

    /**
     * 客户距离
     */
    @ExcelProperty(value = "客户距离", index = 8)
    private BigDecimal customerDistance;

    /**
     * 客户类型
     */
    
    @ExcelProperty(value = "*客户类型", converter = ExcelDictConvert.class, index = 9)
    @ExcelDictFormat(dictType = "mas_customer_type",columnNum = 10)
    private String customerType;

    /**
     * 客户发送状态
     */
    @ExcelProperty(value = "客户状态", converter = ExcelDictConvert.class, index = 10)
    @ExcelDictFormat(dictType = "customer_status",columnNum = 11)
    private String customerStatus;

    /**
     * 省
     */
    @ExcelProperty(value = "*省", index = 11)
    private String province;

    /**
     * 市
     */
    @ExcelProperty(value = "*市", index = 12)
    private String city;

    /**
     * 区(县)
     */
    @ExcelProperty(value = "*区(县)", index = 13)
    private String county;

    /**
     * 详细地址
     */
    @ExcelProperty(value = "*详细地址", index = 14)
    private String customerAddress;

    /**
     * 危废编号
     */
    @ExcelProperty(value = {"危废信息", "危废编号"})
    @Size(min = 0, max = 30, message = "危废编号长度不能超过30个字符")
    private String wasteNo;

    /**
     * 危废类别
     */
    @ExcelProperty(value = {"危废信息", "*危废类别"})
    private String wasteClassify;

    /**
     * 危废代码
     */
    @ExcelProperty(value = {"危废信息", "*危废代码"})
    private String wasteCode;

    /**
     * 危废名称
     */
    @ExcelProperty(value = {"危废信息", "*危废名称"})
    private String wasteName;

    /**
     * 危废状态
     */
    @ExcelProperty(value = {"危废信息", "危废状态"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "customer_status",columnNum = 19)

    private String wasteStatus;

    /**
     * 处置方式
     */
    @ExcelProperty(value = {"危废信息", "*处置方式"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_standard_type",columnNum = 20)
    private String disposalMethod;

    /**
     * 危废特性
     */
    @ExcelProperty(value = {"危废信息", "*危废特性"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_waste_features",columnNum = 21)
    private String wasteFeatures;

    /**
     * 危废形态
     */
    @ExcelProperty(value = {"危废信息", "*危废形态"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_waste_shape",columnNum = 22)
    private String wasteShape;

    /**
     * 危废外观
     */
    @ExcelProperty(value = {"危废信息", "危废外观"})
    private String wasteAppearance;

    /**
     * 危废颜色
     */
    @ExcelProperty(value = {"危废信息", "危废颜色"})
    private String wasteColor;

    /**
     * 包装方式
     */
    @ExcelProperty(value = {"危废信息", "*包装方式"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_packing_method",columnNum = 25)
    private String packingMethod;

    /**
     * 主要危险成分
     */
    @ExcelProperty(value = {"危废信息", "主要危险成分"})
    private String dangerousComponents;

    /**
     * 禁忌与应急措施
     */
    @ExcelProperty(value = {"危废信息", "禁忌与应急措施"})
    private String tabooMeasure;

    /**
     * 应急设备
     */
    @ExcelProperty(value = {"危废信息", "应急设备"})
    private String emergencyEquipment;

    /**
     * 危废用途
     */
    @ExcelProperty(value = {"危废信息", "*危废用途"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_use_type",columnNum = 29)
    private String useType;

    /**
     * SKU重量单位
     */
    @ExcelProperty(value = {"危废信息", "*SKU重量单位"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_weight_unit",columnNum = 30)
    private String weightUnit;

    /**
     * SKU存储单位
     */
    @ExcelProperty(value = {"危废信息", "*SKU存储单位"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_storage_unit",columnNum = 31)
    private String storageUnit;

    /**
     * 是否允许拆分
     */
    @ExcelProperty(value = {"危废信息", "*是否允许拆分"}, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mas_is_split",columnNum = 32)
    private String isSplit;


}
