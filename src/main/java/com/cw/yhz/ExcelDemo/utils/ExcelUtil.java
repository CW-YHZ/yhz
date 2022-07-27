package com.cw.yhz.ExcelDemo.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.exception.ExcelCommonException;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.csvreader.CsvWriter;
import com.cw.yhz.ExcelDemo.ServiceException;
import com.cw.yhz.ExcelDemo.annotation.ExcelDictFormat;
import com.cw.yhz.ExcelDemo.constant.Constants;
import com.cw.yhz.ExcelDemo.convert.ExcelBigNumberConvert;
import com.cw.yhz.ExcelDemo.excel.DefaultExcelListener;
import com.cw.yhz.ExcelDemo.excel.ExcelListener;
import com.cw.yhz.ExcelDemo.excel.ExcelResult;
import com.google.common.base.Throwables;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Excel相关处理
 *
 * @author Lion Li
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtil {

    /**
     * 同步导入(适用于小数据量)
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
        try{
            return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
        }catch (ExcelCommonException e){
            throw new ServiceException("上传文件格式只支持 xlsx");
        }
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     * @param inputStream 流
     * @param clazz 模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static <T> List<T> syncReadModel(InputStream inputStream, Class<T> clazz, Integer sheetNo, Integer headRowNum){
        try{
            return EasyExcel.read(inputStream).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
        }catch (ExcelCommonException e){
            throw new ServiceException("上传文件格式只支持 xlsx");
        }
    }


    /**
     * 使用校验监听器 异步导入 同步返回
     *
     * @param is         输入流
     * @param clazz      对象类型
     * @param isValidate 是否 Validator 检验 默认为是
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, boolean isValidate) {
        DefaultExcelListener<T> listener = new DefaultExcelListener<>(isValidate);
        EasyExcel.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 使用自定义监听器 异步导入 自定义返回
     *
     * @param is       输入流
     * @param clazz    对象类型
     * @param listener 自定义监听器
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, ExcelListener<T> listener) {
        EasyExcel.read(is, clazz, listener).sheet().headRowNumber(3).doRead();
        return listener.getExcelResult();
    }

    /**
     * 导出excel
     *
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response,ExcelFillCellMergeStrategy ...excelFillCellMergeStrategy) {
        try {
            response.reset();
            ServletOutputStream os = response.getOutputStream();
            if (list != null && list.size() > 5000) {
                // csv
                String filename = encodingFilename(sheetName, "csv");
                FileUtils.setAttachmentResponseHeader(response, filename);
                response.setContentType("text/csv");
                writeCsv(list, clazz, os);
            } else {
                // excel
                String filename = encodingFilename(sheetName, "xlsx");
                FileUtils.setAttachmentResponseHeader(response, filename);
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
                EasyExcel.write(os, clazz)
                        .autoCloseStream(false)
                        // 自动适配
                        .registerWriteHandler(StringUtils.isEmpty(excelFillCellMergeStrategy)?new LongestMatchColumnWidthStyleStrategy():excelFillCellMergeStrategy[0])
                        // 大数值自动转换 防止失真
                        .registerConverter(new ExcelBigNumberConvert())
                        .sheet(sheetName).doWrite(list);
            }
        } catch (IOException e) {
            throw new RuntimeException("导出Excel异常");
        }
    }

    /**
     * 导出csv
     * @param list      导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public static <T> void exportCsv(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response) {
        try {
            response.reset();
            ServletOutputStream os = response.getOutputStream();
            // csv
            String filename = encodingFilename(sheetName, "csv");
            FileUtils.setAttachmentResponseHeader(response, filename);
            response.setContentType("text/csv");
            writeCsv(list, clazz, os);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel异常");
        }
    }

    private static <T> void writeCsv(List<T> list, Class<T> clazz, ServletOutputStream os) {
        // 整理字段
        Field[] fields = ReflectUtil.getFields(clazz);
        List<String> columns = new ArrayList<>();
        List<String> methodStrs = new ArrayList<>();
        Map<String, String> formats = new HashMap<>();
        Map<String, String> converters = new HashMap<>();
        for (Field field : fields) {
            if (AnnotationUtil.hasAnnotation(field, ExcelProperty.class)) {
                String[] value = AnnotationUtil.getAnnotationValue(field, ExcelProperty.class);
                int index = AnnotationUtil.getAnnotationValue(field, ExcelProperty.class, "index");
                columns.add(index + "&&" + value[0]);
                methodStrs.add(index + "&&" + field.getName());
                String formatter = AnnotationUtil.getAnnotationValue(field, ExcelProperty.class, "format");
                if (StrUtil.isNotBlank(formatter)) {
                    formats.put(field.getName(), formatter);
                }
                Object cover = AnnotationUtil.getAnnotationValue(field, ExcelProperty.class, "converter");
                String converterClassName = ((Class) cover).getName();
                if ("com.immotors.slm.service.convert.ExcelDictConvert".equals(converterClassName)) {
                	if (AnnotationUtil.hasAnnotation(field, ExcelDictFormat.class)) {
                        String dictType = AnnotationUtil.getAnnotationValue(field, ExcelDictFormat.class, "dictType");
                        if (StrUtil.isNotBlank(dictType)) {
                            converters.put(field.getName(), dictType);
                        }
                    }
                }
            }
        }
        // 排序
        columns.stream().sorted();
        methodStrs.stream().sorted();
        // 组织codelist
        Map<String, Map<String, Object>> dictMap = new HashMap<>();
   /*     if (!converters.isEmpty()) {
            Set<String> dictTypeSet = new HashSet<>(converters.values());
            dictMap = DictUtils.getDictValueByDictTypes(dictTypeSet);
        }*/

        // 组织csv
        try {
            @Cleanup
            CsvWriter wr = new CsvWriter(os, ',', Charset.forName(Constants.GBK));
            String[] header = columns.stream().map(s -> s.split("&&")[1]).toArray(String[]::new);
            wr.writeRecord(header);
            Map<String, Map<String, Object>> finalDictMap = dictMap;
            list.stream().forEach(item -> {
                String[] values = methodStrs.stream().map(methodStr -> {
                    Object result = "";
                    String fieldName = methodStr.split("&&")[1];
                    String methodName = "get" + StrUtil.upperFirst(fieldName);
                    result = ReflectUtil.invoke(item, methodName);
                    String dictType = converters.get(fieldName);
                    if (StrUtil.isNotBlank(dictType)) {
                        result = (String) finalDictMap.get(dictType).get(result);
                    }

                    // BigDecimal 会导致转型失败
                    if (result instanceof BigDecimal) {
                        result = ((BigDecimal) result).toPlainString();
                    }

                    String formatter = formats.get(methodStr);
                    if (StrUtil.isNotBlank(formatter)) {
                        result = String.format(formatter, result);
                    }
                    return result;
                }).toArray(String[]::new);
                try {
                    wr.writeRecord(values);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            wr.flush();
        } catch (Exception e) {
            log.error("Exception", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(separator, propertyValue)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(separator, propertyValue)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename, String suffix) {
        return IdUtil.fastSimpleUUID() + "_" + filename + "." + suffix;
    }

    public static <T> void download(String sheetName, Class<T> clazz, HttpServletResponse response, Map<Integer, List<String>> selectMap) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode = URLEncoder.encode(sheetName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLS.getValue());
        EasyExcelFactory.write(response.getOutputStream())
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
                .excelType(ExcelTypeEnum.XLSX)
                .head(clazz)
                .sheet(sheetName)
                .doWrite(new ArrayList());
    }

}
