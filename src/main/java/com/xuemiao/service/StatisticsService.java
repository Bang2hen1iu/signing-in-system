package com.xuemiao.service;

import com.xuemiao.api.Json.RangeStatisticJson;
import com.xuemiao.api.Json.StatisticJson;
import com.xuemiao.model.repository.StudentRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by dzj on 10/4/2016.
 */
@Service
public class StatisticsService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StatisticsRepositoryService statisticsRepositoryService;
    @Value("${range-statistics-dir}")
    String rangeStatisticsDir;

    public RangeStatisticJson getRangeStatistics(Date startDate, Date endDate) {
        RangeStatisticJson rangeStatisticJson = new RangeStatisticJson();
        List<StatisticJson> results = statisticsRepositoryService.getRangeStatistics(startDate, endDate);
        rangeStatisticJson.setStatisticJsons(results);

        Field[] fields = StatisticJson.class.getDeclaredFields();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("月统计数据");

        int rowIdx = 0, cellIdx = 0;

        Row row = sheet.createRow(rowIdx++);
        for (Field field : fields){
            Cell cell = row.createCell(cellIdx++);
            cell.setCellValue(field.getName());
        }

        for (StatisticJson statisticJson : results){
            row = sheet.createRow(rowIdx++);
            for (Field field : fields){
                Cell cell = row.createCell(cellIdx++);
                Object value = null;
                try{
                    value = field.get(statisticJson);
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
                if (value instanceof String){
                    cell.setCellValue((String)value);
                }
                else if (value instanceof Double){
                    cell.setCellValue((Double) value);
                }
                else if (value instanceof Integer){
                    cell.setCellValue((Integer) value);
                }
            }
        }

        String uuid = UUID.randomUUID().toString();

        try{
            FileOutputStream outputStream = new FileOutputStream(rangeStatisticsDir + uuid + ".xls");
            workbook.write(outputStream);
            rangeStatisticJson.setDownloadCode(uuid);
        }catch (IOException e){
            e.printStackTrace();
            rangeStatisticJson.setDownloadCode("");
        }

        return rangeStatisticJson;
    }

    public List<StatisticJson> getStatisticsOfThisMonth() {
        DateTime now = DateTime.now();
        DateTime dateTime = new DateTime(now.getYear(), now.getMonthOfYear(), 1, 0, 0, 0, 0);
        return statisticsRepositoryService.getStatisticsByStartDateUpToNow(new Date(dateTime.getMillis()));
    }

}
