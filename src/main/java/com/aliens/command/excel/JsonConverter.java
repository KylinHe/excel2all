package com.aliens.command.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliens.command.excel.model.TableData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by hejialin on 2018/3/10.
 */
public class JsonConverter {

    public void convert(TableData tableData, String dstDirPath) {
        List<Map<String, Object>>  datas = tableData.getDataArray();

        JSONArray array = new JSONArray();
        JSONObject rowData;
        for (Map<String, Object> data : datas) {
            rowData = new JSONObject(true);
            rowData.putAll(data);
            array.add(rowData);
        }

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(dstDirPath + File.separator + tableData.getAlias() + ".json");
            if (array.size() == 1) {
                //JSON.writeJSONStringTo(array.get(0), fileWriter);
                JSON.writeJSONString(fileWriter, array.get(0));
            } else {
                JSON.writeJSONString(fileWriter, array);
                //JSON.writeJSONStringTo(array, fileWriter);
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
