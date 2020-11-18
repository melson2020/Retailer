package com.melson.webserver.utils;

/**
 * @Author Nelson
 * @Description
 * @Date 2020/10/19
 */

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.melson.webserver.Vo.ExcelExportResultVo;
import com.melson.webserver.dto.ProductStorageDto;
import com.melson.webserver.entity.ProductBatch;
import com.melson.webserver.entity.ProductStorage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

public class PoiUtils {
    // 显示的导出表的标题
    private String title;
    // 导出表的列名
    private String[] rowName;
    // 表的内容
    private List<Object[]> dataList;

    // 构造方法，传入要导出的数据
    public PoiUtils(String title, String[] rowName, List<Object[]> dataList) {
        this.dataList = dataList;
        this.rowName = rowName;
        this.title = title;
    }

    public PoiUtils() {
    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 14);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        SetCenterBorderStyle(style, font);
        //设置背景色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getContentStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置模板基本样式;
        HSSFCellStyle style = workbook.createCellStyle();
        SetCenterBorderStyle(style, font);
        return style;

    }

    public HSSFCellStyle getSubTableCellStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 8);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置模板基本样式;
        HSSFCellStyle style = workbook.createCellStyle();
        SetCenterBorderStyle(style, font);
        return style;

    }

    //获取带边框的居中样式
    private void SetCenterBorderStyle(HSSFCellStyle style, HSSFFont font) {
        font.setFontName("Courier New");
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    }


    public HSSFCellStyle GetTitleCellStyle(HSSFWorkbook workbook, int fontSize, boolean bold, short aligment) {
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) fontSize);
        // 字体加粗
        if (bold) font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        font.setFontName("Courier New");
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(aligment);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }


    public ExcelExportResultVo ExceptStorageCountDetail(String titleName, String subTitle, String footer, String[] headerColunm, List<ProductStorageDto> storageDtoList, String path) {
        ExcelExportResultVo vo = new ExcelExportResultVo();
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
        HSSFSheet sheet = workbook.createSheet("sheet1"); // 创建sheet
        //创建一行 作为表头
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(40);
        HSSFCell cellTiltle = titleRow.createCell(0);
        //将表头行单元格合并
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerColunm.length - 1));
        //获取表头样式
        HSSFCellStyle titleStyle = GetTitleCellStyle(workbook, 18, true, HSSFCellStyle.ALIGN_CENTER);
        //设置表头行样式和内容
        cellTiltle.setCellStyle(titleStyle);
        cellTiltle.setCellValue(titleName);
        //创建第二行 作为子标题行
        HSSFRow subTitleRow = sheet.createRow(1);
        subTitleRow.setHeightInPoints(28);
        HSSFCell subTitleCell = subTitleRow.createCell(0);
        //获取子标题行样式
        HSSFCellStyle subTitleStyle = GetTitleCellStyle(workbook, 10, false, HSSFCellStyle.ALIGN_RIGHT);
        subTitleStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        //设置子标题行样式和值
        subTitleCell.setCellValue(subTitle);
        subTitleCell.setCellStyle(subTitleStyle);

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headerColunm.length - 1));
        HSSFRow headerNameRow = sheet.createRow(2);
        headerNameRow.setHeightInPoints(28);
        HSSFCellStyle headerStyle = getColumnTopStyle(workbook);
        for (int i = 0; i < headerColunm.length; i++) {
            HSSFCell headerNameCell = headerNameRow.createCell(i);
            headerNameCell.setCellValue(headerColunm[i]);
            headerNameCell.setCellStyle(headerStyle);
        }
        int rowCount = 3;
        HSSFCellStyle cellStyle = getContentStyle(workbook);
        Map<Integer, List<ProductStorageDto>> dtoMap = new HashMap<>();
        for (ProductStorageDto storage : storageDtoList) {
            List<ProductStorageDto> existList = dtoMap.get(storage.getProductId());
            if (existList == null) {
                existList = new ArrayList<>();
                existList.add(storage);
                dtoMap.put(storage.getProductId(), existList);
            } else {
                existList.add(storage);
            }
        }
        //从小到大排序
        List<Integer> sortList = new ArrayList<>();
        for (Integer productId : dtoMap.keySet()) {
            sortList.add(productId);
        }
        //正序
        sortList.sort(Comparator.comparingInt(Integer::intValue));
        for (Integer productId : sortList) {
            List<ProductStorageDto> dtoList = dtoMap.get(productId);
            Integer lastRow = rowCount + dtoList.size() - 1;
            sheet.addMergedRegion(new CellRangeAddress(rowCount, lastRow, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, lastRow, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, lastRow, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, lastRow, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, lastRow, 7, 7));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, lastRow, 8, 8));
            sheet.addMergedRegion(new CellRangeAddress(rowCount, lastRow, 10, 10));
            for (ProductStorageDto storage : dtoList) {
                HSSFRow contentRow = sheet.createRow(rowCount);
                contentRow.setHeightInPoints(23);
                HSSFCell cell1 = contentRow.createCell(0);
                cell1.setCellValue(storage.getProductId());
                cell1.setCellStyle(cellStyle);
                HSSFCell cell2 = contentRow.createCell(1);
                cell2.setCellValue(storage.getProductName());
                cell2.setCellStyle(cellStyle);
                HSSFCell cell3 = contentRow.createCell(2);
                cell3.setCellValue(storage.getType());
                cell3.setCellStyle(cellStyle);
                HSSFCell cell4 = contentRow.createCell(3);
                cell4.setCellValue(storage.getSpecification());
                cell4.setCellStyle(cellStyle);
                HSSFCell cell5 = contentRow.createCell(4);
                cell5.setCellValue(storage.getBatchNo());
                cell5.setCellStyle(cellStyle);
                HSSFCell cellSupplyId = contentRow.createCell(5);
                cellSupplyId.setCellValue(storage.getSupplyId()==null?"":storage.getSupplyId().toString());
                cellSupplyId.setCellStyle(cellStyle);
                HSSFCell cell6 = contentRow.createCell(6);
                cell6.setCellValue(storage.getSupplyName());
                cell6.setCellStyle(cellStyle);
                HSSFCell cell7 = contentRow.createCell(7);
                cell7.setCellStyle(cellStyle);
                cell7.setCellValue(storage.getCount());
                HSSFCell cell8 = contentRow.createCell(8);
                cell8.setCellStyle(cellStyle);
                cell8.setCellValue(storage.getTotalCount());
                HSSFCell cell9 = contentRow.createCell(9);
                cell9.setCellStyle(cellStyle);
                cell9.setCellValue(storage.getCountUnit());
                HSSFCell cell10 = contentRow.createCell(10);
                cell10.setCellStyle(cellStyle);
                HSSFCell cell11 = contentRow.createCell(11);
                cell11.setCellStyle(cellStyle);
                rowCount++;
            }
        }
        HSSFRow footRow = sheet.createRow(rowCount);
        footRow.setHeightInPoints(28);
        HSSFCell cell1 = footRow.createCell(0);
        cell1.setCellValue(footer);
        HSSFCellStyle footStyle = GetTitleCellStyle(workbook, 10, false, HSSFCellStyle.ALIGN_LEFT);
        footStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cell1.setCellStyle(footStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, headerColunm.length - 1));
        // 让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < headerColunm.length; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(path);
            workbook.write(out);//保存Excel文件
            out.close();//关闭文件流
            vo.setStatus(1);
        } catch (Exception ex) {
            vo.setStatus(-1);
            vo.setMessage(ex.getMessage());
        }
        return vo;
    }
}

