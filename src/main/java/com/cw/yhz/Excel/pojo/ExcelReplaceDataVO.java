package com.cw.yhz.Excel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelReplaceDataVO {
    private int row;// Excel单元格行
    private int column;// Excel单元格列
    private String key;// 替换的关键字
    private String value;// 替换的文本
}
