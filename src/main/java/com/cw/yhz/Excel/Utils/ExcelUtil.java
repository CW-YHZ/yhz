package com.cw.yhz.Excel.Utils;

import com.cw.yhz.Excel.pojo.ExcelReplaceDataVO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * excel 表格导出
 */
public class ExcelUtil {

    /**
     * 功能描述：导出 Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);     //创建一个居中格式
        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

    /**
     * 功能描述：导入 Excel
     *
     * @param file
     * @return
     */
    public static List<Object> getReadExport(MultipartFile file) {
        List<Object> rows = Collections.emptyList();    //读取的excel数据
        try {
            // 1.创建workbook对象，读取整个文档
            Workbook wb = null;                                 //excel 文件
            String fileName = file.getName();                   //excel 名称
            InputStream inputStream = file.getInputStream();    //excel 字节流

            // 2.判断 excel 文件类型，如果是xls，使用 HSSFWorkbook；如果是 xlsx，使用 XSSFWorkbook
            if (fileName.substring(fileName.indexOf("."), fileName.length()).equals(".xls")) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }

            // 3.读取页脚sheet
            Sheet sheetAt = wb.getSheetAt(0);

            // 4.循环读取行
            for (Row row : sheetAt) {
                List<Object> column = Collections.emptyList();    //读取的excel数据
                // 5.循环读取列
                for (int rowIndex = 0; rowIndex < row.getLastCellNum(); rowIndex++) {
                    rows.add(row.getCell(rowIndex).getStringCellValue());
                }
                rows.add(column);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return rows;
        }
    }

    /**
     * 功能描述：excel 上传模板、写入数据、导出报表
     *
     * @param file
     * @return
     */
    public static Workbook replaceModel(List<ExcelReplaceDataVO> datas, MultipartFile file) {
        // 1.创建workbook对象，读取整个文档
        Workbook wb = null;                                 //声明一个 excel 文件流
        List<Object> rows = Collections.emptyList();

        try {
            String fileName = file.getOriginalFilename();       //获取上传的 excel 名称
            InputStream inputStream = file.getInputStream();    //获取上传的 excel 字节流

            // 2.判断 excel 文件类型，如果是xls，使用 HSSFWorkbook；如果是 xlsx，使用 XSSFWorkbook
            if (fileName.substring(fileName.indexOf(".")).equals(".xls")) {
                wb = new HSSFWorkbook(inputStream);     //将上传的 excel 文件放入创建好的流里
            } else {
                wb = new XSSFWorkbook(inputStream);
            }

            // 3.读取页脚 sheet 0 为第一页
            Sheet sheet = wb.getSheetAt(0);

            // 4.循环读取行并替换内容
            datas.forEach((item) -> {
                //获取单元格内容
                Row row = sheet.getRow(item.getRow());
                Cell cell = row.getCell((short) item.getColumn());
                cell.getStringCellValue();
                String str = cell.getStringCellValue();

                //根据根据 key 替换 value
                str = str.replace(item.getKey(), item.getValue());

                //写入单元格内容
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                // poi3.5 已取消，交由 Unicode 处理
                // cell.setEncoding(HSSFCell.ENCODING_UTF_16); //设置编码

                //把模板中的单元格文本替换掉
                cell.setCellValue(str);
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return wb;
        }
    }
}
