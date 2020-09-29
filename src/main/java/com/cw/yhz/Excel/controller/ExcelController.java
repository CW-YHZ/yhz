package com.cw.yhz.Excel.controller;

import com.ouhl.utildemo.Excel.Utils.ExcelUtil;
import com.ouhl.utildemo.Excel.pojo.ExcelReplaceDataVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/excel")
public class ExcelController {
    /**
     * 功能描述：excel 导出
     *
     * @param request
     * @param response
     */
    @RequestMapping("exportExcel")
    public void exportProductionTraceability(HttpServletRequest request, HttpServletResponse response) {
        String timeType = request.getParameter("name"),     //查询类型
                year = request.getParameter("type");        //编号

        List<Map<String, Object>> listMap = null;
        String[] title = {"编号", "名称"};        //excel标题
        String fileName = "excel导出" + System.currentTimeMillis() + ".xls";  //excel文件名
        String sheetName = "sheet-1";           //sheet名

        String[][] content = new String[listMap.size()][title.length];
        for (int i = 0; i < listMap.size(); i++) {
            content[i] = new String[title.length];
            Map<String, Object> map = listMap.get(i);
            content[i][0] = map.get("id").toString();
            content[i][1] = map.get("name").toString();
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：excel 导入
     *
     * @param file 前端传递过来的文件
     */
    @ResponseBody
    @RequestMapping("/export")
    public List<Object> export(@RequestParam("file") MultipartFile file) {
        return ExcelUtil.getReadExport(file);
    }

    /**
     * 功能描述：excel 上传模板、写入数据、导出报表
     *
     * @param file 前端传递过来的文件
     */
    @RequestMapping("/getReadExports")
    public void getReadExports(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        Map map = new HashMap();
        List<ExcelReplaceDataVO> list = new ArrayList<>();
        list.add(new ExcelReplaceDataVO(0, 0, "a", "b"));
        Workbook wb = ExcelUtil.replaceModel(list, file);
        //响应到客户端
        try {
            this.setResponseHeader(response, file.getOriginalFilename());
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：发送响应流方法（用于 excel 导出）
     *
     * @param response
     * @param fileName
     */
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
