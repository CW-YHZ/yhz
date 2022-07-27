package com.cw.yhz.ExcelDemo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客户基本信息对象 tm_customer
 *
 * @author mrp
 * @date 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Customer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一识别号
     */
    private String tuid;

    /**
     * 客户编号
     */
    @Size(min = 0, max = 30, message = "客户编号长度不能超过30个字符", groups = {Update.class})
    @NotBlank(message = "客户编号不能为空", groups = {Update.class})
    private String customerNo;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 30, message = "客户名称长度不能超过30个字符", groups = {Insert.class, Update.class})
    private String customerName;

    /**
     * 邮编
     */
    @NotBlank(message = "邮编不能为空", groups = {Insert.class, Update.class})
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "邮编无效", groups = {Insert.class, Update.class})
    private String postcode;

    /**
     * 客户电话
     */
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "客户电话格式有误", groups = {Insert.class, Update.class})
    @NotBlank(message = "客户电话不能为空", groups = {Insert.class, Update.class})
    private String customerPhone;

    /**
     * 客户Email
     */
    @Email(message = "邮箱格式不正确", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 30, message = "邮箱长度不能超过30个字符", groups = {Insert.class, Update.class})
    private String customerEmail;

    /**
     * 客户联系人
     */
    @NotBlank(message = "客户联系人不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 30, message = "客户联系人名称长度不能超过30个字符", groups = {Insert.class, Update.class})
    private String customerContact;

    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话不能为空", groups = {Insert.class, Update.class})
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "联系人电话格式有误")
    private String contactTel;

    /**
     * 联系人Email
     */
    @Email(message = "邮箱格式不正确", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 30, message = "邮箱长度不能超过30个字符", groups = {Insert.class, Update.class})
    private String contactEmail;

    /**
     * 客户距离
     */
    private BigDecimal customerDistance;

    /**
     * 客户类型
     */
    @NotBlank(message = "客户类型无效", groups = {Insert.class, Update.class})
    private String customerType;

    /**
     * 客户状态
     */
    @NotBlank(message = "客户状态类型无效", groups = {Update.class})
    private String customerStatus;

    /**
     * 省
     */
    @Size(min = 0, max = 30, message = "客户地址(省)长度不能超过30个字符", groups = {Insert.class, Update.class})
    @NotBlank(message = "客户地址(省)不能为空", groups = {Insert.class, Update.class})
    private String province;

    /**
     * 市
     */
    @Size(min = 0, max = 30, message = "客户地址(市)长度不能超过30个字符", groups = {Insert.class, Update.class})
    @NotBlank(message = "客户地址(市)不能为空", groups = {Insert.class, Update.class})
    private String city;

    /**
     * 区(县)
     */
    @Size(min = 0, max = 30, message = "客户地址(区(县))长度不能超过30个字符", groups = {Insert.class, Update.class})
    @NotBlank(message = "客户地址(区(县))不能为空", groups = {Insert.class, Update.class})
    private String county;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 200, message = "详细地址长度不能超过200个字符", groups = {Insert.class, Update.class})
    private String customerAddress;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 附件
     */
    private String annexFiles;

    /**
     * 状态
     */
    private String status;

    /**
     * 删除标识
     */
    private String delFlg;

    /**
     * 所属工厂
     */
    private String factoryCode;


    private List<CustomerMaterial> customerMaterialList;

}
