package com.cw.yhz.ExcelDemo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 * 
 * @author XL.Pei
 * @Date 2022-01-26
 *
 */
@Data
public class BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 唯一标识
	 */
	private String tuid;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

	/** 删除标识
	 *  0-正常
	 *  2-删除
	 */
	private String delFlg;
	
	/**
	 * 所属机构-对应工厂编号
	 */
	private String factoryCode;
	

	/** 备注 */
	private String remark;
	
    /** 搜索值 */
    private String searchValue;
	/** 请求参数 */
	private Map<String, Object> params= new HashMap<>();
}
