package com.aliens.command.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.aliens.command.excel.model.FieldType;
import com.aliens.command.excel.model.TableData;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/10.
 */
public class JsonConverter {

    public void convert(TableData tableData, String dstDirPath) {
        List<Map<String, Object>>  datas = tableData.getDataArray();

        if (!tableData.haveField()) {
            return;
        }

        JSONArray array = new JSONArray();
        JSONObject rowData;
        for (Map<String, Object> data : datas) {
            rowData = new JSONObject(true);
            rowData.putAll(data);
            array.add(rowData);
        }

        String filePath = dstDirPath + File.separator + tableData.getName() + ".json";
        FileOutputStream fileWriter = null;
        //FileWriter fileWriter = null;
        try {
            fileWriter = new FileOutputStream(filePath);
            //fileWriter = new FileWriter();

            boolean haveId = tableData.getField(0).getFieldType() == FieldType.ID;
            if (array.size() == 1 && !haveId) {
                JSON.writeJSONString(fileWriter, IOUtils.UTF8, array.get(0));
            } else {
                JSON.writeJSONString(fileWriter, IOUtils.UTF8, array);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
