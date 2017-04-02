/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;


/**
 * 类DataAnalyze.java的实现描述：TODO 类实现描述 
 * @author yangshihong Feb 2, 2015 2:36:32 PM
 */
public class DataAnalyze {

    private static final int defMatchNumbers = 5;
    private static final boolean CARLICENSE_MATCH_PATTERN_NOPREFIX = true;
    private static String date = "2015-03-08";
    private static String PLAZA_ID = "1000292";
    private static String logFileName55 = "E:\\analyzeData\\log\\10.77.135.55\\cloudparking_interface.log."+date;
    private static String logFileName56 = "E:\\analyzeData\\log\\10.77.135.56\\cloudparking_interface.log."+date;
    private static final String noSpaceCarLicenseFile = "E:/analyzeData/data/"+date.replaceAll("-", "")+"NoSpaceData.txt";
    private static final String noRealtimeCarLicenseFile = "E:/analyzeData/data/"+date.replaceAll("-", "")+"NoRealtimeData.txt";
    /**
     * @param args
     */
    public static void main(String[] args) {
//
        System.out.println("***************停车日志分析开始***************");
        DataAnalyze da = new DataAnalyze();
        da.compareRealtimeAndSpaceUploadLicenseToSimilar(5);
        da.compareRealtimeAndSpaceUploadLicenseToSimilar(4);
        da.compareRealtimeAndSpaceUploadLicenseToNoPrefix();
//        da.analyzeLogGenerateSpaceUploadSQL();
//        da.analyzeCarLocationUploadData();
//        DataAnalyze.analyzeParkingLogFetchNormalRequest("ParkingDataServlet->carLocationData request","carLocationDataRequest");
//        DataAnalyze.analyzeNormalLog("MemberServlet->getMember", "getMember");
//        DataAnalyze.analyzeNormalLog("PayServlet->calPayLocal", "calPayLocal");
//        DataAnalyze.analyzeNormalLog("TradeServlet->notifyPaySuccess", "paySuccess");
//        DataAnalyze.analyzeNormalLog("ParkingDataServlet->outParkingData", "outParkingData");
        System.out.println("***************停车日志分析结束***************");
    }
    
    public  void compareRealtimeAndSpaceUploadLicenseToSimilar() {
        
        compareRealtimeAndSpaceUploadLicenseToSimilar(defMatchNumbers);

    }
    public  void compareRealtimeAndSpaceUploadLicenseToSimilar(int matchNumbers) {
        
        List<CarLicenseInfo> noSpaceCarLicenses = new ArrayList<CarLicenseInfo>();
        List<CarLicenseInfo> noRealtimeCarLicenses = new ArrayList<CarLicenseInfo>();

        fetchCarLicense(noSpaceCarLicenses, noSpaceCarLicenseFile);
        fetchCarLicense(noRealtimeCarLicenses, noRealtimeCarLicenseFile);
        int matchNum = 0;
        for (CarLicenseInfo carInfoOuter : noSpaceCarLicenses) {
            List<String> data = new ArrayList<String>();
            String noPrefixOuter = carInfoOuter.getNoPrefixCarLicense();
            
            for (CarLicenseInfo carInfoInner : noRealtimeCarLicenses) {
                String noPrefixInner = carInfoInner.getNoPrefixCarLicense();
                int similarNo = 0;
                int recycles = noPrefixOuter.length()>noPrefixInner.length()?noPrefixInner.length():noPrefixOuter.length();
                for (int index=0;index < recycles;index++) {
                    if (noPrefixOuter.charAt(index) == noPrefixInner.charAt(index)) {
                        similarNo++;
                    }
                }
                if (similarNo == matchNumbers) {
                    data.add(carInfoInner.getCarLicense());
                }
            }
            if (!data.isEmpty()) {
                matchNum++;
            }
            carInfoOuter.setSimilarCarLicenses(data);
        }
        CarLicenseInfo info = new CarLicenseInfo("汇总(按"+matchNumbers+"位相同进行匹配)","");
        List<String> tmpList = new ArrayList<String>();
        tmpList.add("匹配数:"+matchNum);
        info.setSimilarCarLicenses(tmpList);
        noSpaceCarLicenses.add(0, info);
        
        String path = String.format("E:/analyzeData/result/%s_%s_%s_SimilarCarLicense.txt", date.replaceAll("-", ""),PLAZA_ID,matchNumbers);
        writeCarInfoToFile(noSpaceCarLicenses, path);
    }
    
    public  void compareRealtimeAndSpaceUploadLicenseToNoPrefix() {
        
        List<CarLicenseInfo> noSpaceCarLicenses = new ArrayList<CarLicenseInfo>();
        List<CarLicenseInfo> noRealtimeCarLicenses = new ArrayList<CarLicenseInfo>();
        
        fetchCarLicense(noSpaceCarLicenses, noSpaceCarLicenseFile);
        fetchCarLicense(noRealtimeCarLicenses, noRealtimeCarLicenseFile);
        int matchNum = 0;
        for (CarLicenseInfo carInfoOuter : noSpaceCarLicenses) {
            List<String> data = new ArrayList<String>();
            String noPrefixOuter = carInfoOuter.getNoPrefixCarLicense();
            
            for (CarLicenseInfo carInfoInner : noRealtimeCarLicenses) {
                String noPrefixInner = carInfoInner.getNoPrefixCarLicense();
                if (noPrefixOuter.equalsIgnoreCase(noPrefixInner)) {
                    data.add(carInfoInner.getCarLicense());
                }
            }
            if (!data.isEmpty()) {
                matchNum++;
            }
            carInfoOuter.setSimilarCarLicenses(data);
        }
        
        CarLicenseInfo info = new CarLicenseInfo("汇总","");
        List<String> tmpList = new ArrayList<String>();
        tmpList.add("匹配数量:"+matchNum);
        info.setSimilarCarLicenses(tmpList);
        noSpaceCarLicenses.add(0, info);
        
        String path = String.format("E:/analyzeData/result/%s_%s_NoPrefixCarLicense.txt", date.replaceAll("-", ""),PLAZA_ID);
        writeCarInfoToFile(noSpaceCarLicenses, path);
    }
    
    
    public  void analyzeLogGenerateSpaceUploadSQL() {
        List<String> carLocationLogList = new ArrayList<String>();
        readCarLocationLogFile(carLocationLogList, logFileName55);
        readCarLocationLogFile(carLocationLogList, logFileName56);
        
        List<String> offlineSpaceSqlList = new ArrayList<String>();
        for (String log :carLocationLogList) {
            int index = log.indexOf(",");
            String createTime = log.substring(0, index);
            int beginIndex = log.indexOf("{");
            int endIndex = log.lastIndexOf("}");
            if (beginIndex == -1 || endIndex == -1) {
                continue;
            }
            String tmpStr = log.substring(beginIndex+1, endIndex);
            beginIndex = tmpStr.indexOf("{");
            endIndex = tmpStr.lastIndexOf("}");
            if (beginIndex == -1 || endIndex == -1) {
                continue;
            }
            tmpStr = tmpStr.substring(beginIndex, endIndex+1);
            CarLocation location = (CarLocation)JSONObject.toBean(JSONObject.fromObject(tmpStr), CarLocation.class);
            String template = "insert into offline_parking_space_upload(car_license,plaza_id,parking_space_floor,parking_space_number,create_time)" +
            		" values('%s',%s,'%s','%s', '%s');";
            if (PLAZA_ID.contains(String.valueOf(location.getPlazaId()))) {
                String insert = String.format(template, location.getCarLicense(),location.getPlazaId(),
                        location.getParkingSpaceFloor(),location.getParkingSpaceNumber(), createTime);
                offlineSpaceSqlList.add(insert);
            }
            
        }
        String summary = String.format("/* 总记录数[%s] */", offlineSpaceSqlList.size());
        offlineSpaceSqlList.add(0, summary);
        String path = String.format("E:/analyzeData/result/%s_%s_OfflineCarSpace.sql", date.replaceAll("-", ""),PLAZA_ID);
        writeContentToFile(offlineSpaceSqlList,path);
        
    }
    
    public  void analyzeCarLocationUploadData() {
        
        List<String> logContentList = new ArrayList<String>();
        List<String> logCarLocationList = new ArrayList<String>();
        List<String> carLicenseList = new ArrayList<String>();


        readLogFile(logContentList,logCarLocationList, logFileName55);
        readLogFile(logContentList,logCarLocationList, logFileName56);
        /** 车牌文件  */
        readCarLicenseFile(carLicenseList, noSpaceCarLicenseFile);
        
        
        List<String> resultDetialList = new ArrayList<String>();
        List<String> resultBriefList = new ArrayList<String>();
//        List<String> resultBatchList = new ArrayList<String>();
        Map<String, Integer> carMap = new HashMap<String, Integer>();
        int count = 0;
//        int batchAndCarLocationCount = 0;
        outer:for (String carLicense : carLicenseList) {
            count ++;
            String msg = String.format("--------------------------[%s]----[%s]--------------------------------", count, carLicense);
            resultDetialList.add(msg);
            String tmpCarLicense = intelProcessCarLicense(carLicense);
//            if (carMap.containsKey(carLicense)) {
//                resultDetialList.add(String.format("车牌[%s]日志同[%s]", carLicense, carMap.get(carLicense)));
//                continue outer;
//            } else {
//                carMap.put(carLicense, count);
//            }
            if (carMap.containsKey(tmpCarLicense)) {
                resultDetialList.add(String.format("车牌[%s]日志同[%s]", carLicense, carMap.get(tmpCarLicense)));
                continue outer;
            } else {
                carMap.put(tmpCarLicense, count);
            }
            
            List<String> tmpList = new ArrayList<String>();
            for (String logCont : logContentList) {
                if (logCont.indexOf(tmpCarLicense) > -1) {
                    tmpList.add(logCont);
                }
            }
            
            if (!tmpList.isEmpty()) {
                Collections.sort(tmpList);
                resultDetialList.addAll(tmpList);
            }
            
            /** 统计批量上报且有上报停车位的车牌相关日志 */
//            int batchIndex = -1;
//            int carLocationIndex = -1;
//            for(String log:tmpList) {
//                if (batchIndex == -1) {
//                    batchIndex = log.indexOf("batchInOutParkingData");
//                }
//                if (carLocationIndex == -1) {
//                    carLocationIndex = log.indexOf("carLocationData");
//                }
//            }
//            if (batchIndex > -1 && carLocationIndex > -1) {
//              batchAndCarLocationCount++;
//              resultBatchList.add(msg);
//              resultBatchList.addAll(tmpList);
//            }
            /** 统计批量上报且有上报停车位的车牌相关日志 */
        }
        
        /** 统计停车位上报请求  */
        int countRequest = 0;
        for (String carLicense : carLicenseList) {
            
            boolean isContain = false;
            String tmpCarLicense = intelProcessCarLicense(carLicense);
            inner : for (String carlog : logCarLocationList) {
                if (carlog.indexOf(tmpCarLicense) > -1) {
                    isContain = true;
                    break inner;
                }
            }
            String brief = String.format("无carLocationData请求         %s", carLicense);
            if(isContain) {
                countRequest++;
                brief = String.format("有carLocationData请求         %s", carLicense);
            }
            resultBriefList.add(brief);
        }
        /** 统计停车位上报请求  */
        
        String  summary = String.format("汇总:总车牌数[%s],发送request请求车牌数[%s]，没发送request车牌数[%s]", 
                carLicenseList.size(),countRequest, carLicenseList.size() - countRequest);
        resultBriefList.add(summary);
        
//        String  summaryBatch = String.format("批量上报车牌数(同一车牌多次批量上报统计时只算一次)[%s]",  batchAndCarLocationCount);
//        resultBatchList.add(summaryBatch);
        
        String infix = date.replaceAll("-", "")+"_"+PLAZA_ID+"_";
        writeContentToFile(resultDetialList,"E:/analyzeData/result/"+infix+"LogDetail.txt");
        writeContentToFile(resultBriefList,"E:/analyzeData/result/" +infix+"LogBrief.txt");
//        writeContentToFile(resultBatchList,"E:/analyzeData/result/" +infix+"LogBatch.txt");
    }
    
    public  void analyzeParkingLogFetchNormalRequest(String keyword,String suffix) {
        
        List<String> logContentList = new ArrayList<String>();
        readNormalRequestLogFile(logContentList,logFileName55, keyword);
        readNormalRequestLogFile(logContentList,logFileName56, keyword);
        
        String infix = date.replaceAll("-", "")+"_"+suffix;
        writeContentToFile(logContentList,"E:/analyzeData/result/"+infix+".txt");
    }
    
    public  void analyzeNormalLog(String keyword,String suffix) {
        
        List<String> logContentList = new ArrayList<String>();
        readNormalLogFile(logContentList,logFileName55,keyword);
        readNormalLogFile(logContentList,logFileName56,keyword);
        
//        String infix = date.replaceAll("-", "")+"_"+PLAZA_ID+"_"+suffix;
        String infix = date.replaceAll("-", "")+"_"+suffix;
        writeContentToFile(logContentList,"E:/analyzeData/result/"+infix+".txt");
    }


    private  String intelProcessCarLicense(String carLicense) {
        int firstCharByteLength = 1;
        try {
            if (CARLICENSE_MATCH_PATTERN_NOPREFIX) {
                firstCharByteLength = carLicense.substring(0, 1).getBytes("UTF-8").length;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String tmpCarLicense;
        if (firstCharByteLength > 1) {
            tmpCarLicense = carLicense.substring(1);
        } else {
            tmpCarLicense = carLicense;
        }
        
        return tmpCarLicense;
    }
    
    
    public static void writeContentToFile(List<String> contentList, String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for(String content : contentList){
                writer.write(content);
                writer.newLine();//换行
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    public static void writeCarInfoToFile(List<CarLicenseInfo> contentList, String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for(CarLicenseInfo carInfo : contentList){
                if (carInfo.getSimilarCarLicenses() == null || carInfo.getSimilarCarLicenses().isEmpty()) {
                    continue;
                }
                writer.write(carInfo.getCarLicense());
                writer.write("->");
                String str = "[]";
                str = carInfo.getSimilarCarLicenses().toString();
                writer.write(str);
                writer.newLine();//换行
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

    public static void readCarLicenseFile(List<String> carLicenseList, String path) {
        File carLicenseFile = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(carLicenseFile));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(tempString)) {
                    carLicenseList.add(tempString.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
    public void fetchCarLicense(List<CarLicenseInfo> carLicenseList, String path) {
        File carLicenseFile = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(carLicenseFile));
            String tempStr = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempStr = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(tempStr)) {
                    tempStr = tempStr.trim();
                    CarLicenseInfo carInfo = new CarLicenseInfo(tempStr,intelProcessCarLicense(tempStr));
                    carLicenseList.add(carInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        
    }

    public  void readLogFile(List<String> logContentList, List<String> logCarLocationList, String filePath) {
        File logFileName = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(logFileName));
            String content = null;
            // 一次读入一行，直到读入null为文件结束
            while ((content = reader.readLine()) != null) {
                if (content.indexOf("request") >= 0) {
                    logContentList.add(dealLogContent(content));
                }
                if (content.indexOf("carLocationData request") >= 0) {
                    logCarLocationList.add(dealLogContent(content));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public  void readCarLocationLogFile(List<String> logCarLocationList, String filePath) {
        File logFileName = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(logFileName));
            String content = null;
            // 一次读入一行，直到读入null为文件结束
            while ((content = reader.readLine()) != null) {
                if (content.indexOf("carLocationData request") >= 0) {
                    logCarLocationList.add(dealLogContent(content));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public  void readNormalRequestLogFile(List<String> logList, String filePath, String keyword) {
        File logFileName = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(logFileName));
            String content = null;
            int count = logList.size();
            while ((content = reader.readLine()) != null) {
                if (content.indexOf(keyword) >= 0) {
                    count++;
                    logList.add("----------------------------------"+count+"------------------------------------");
                    String tmpLog = dealLogContent(content);
                    logList.add(tmpLog);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public  void readNormalLogFile(List<String> logList, String filePath, String Keyword) {
        File logFileName = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(logFileName));
            String content = null;
            int count = logList.size()/3;
            while ((content = reader.readLine()) != null) {
                if (content.indexOf(Keyword) >= 0) {
                    String tmpLog = dealLogContent(content);
                    if (tmpLog.indexOf("request") >= 0) {
                        count++;
                        logList.add("----------------------------------"+count+"------------------------------------");
                    }
                    logList.add(tmpLog);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    private  String dealLogContent(String content) {
        int index1 = content.indexOf("[");
        int index2 = content.lastIndexOf(")");
        content = content.substring(0, index1)
                + content.substring(index2 + 1);
        return content;
    }
    
    
    class CarLicenseInfo {
        private String carLicense;
        /** 不包含省份的车牌 */
        private String noPrefixCarLicense;
        private List<String> similarCarLicenses;

        public CarLicenseInfo(String carLicense,String noPrefixCarLicense) {
            this.carLicense =carLicense;
            this.noPrefixCarLicense = noPrefixCarLicense;
        }
        public String getCarLicense() {
            return carLicense;
        }

        public void setCarLicense(String carLicense) {
            this.carLicense = carLicense;
        }

        public String getNoPrefixCarLicense() {
            return noPrefixCarLicense;
        }
        public void setNoPrefixCarLicense(String noPrefixCarLicense) {
            this.noPrefixCarLicense = noPrefixCarLicense;
        }
        public List<String> getSimilarCarLicenses() {
            return similarCarLicenses;
        }

        public void setSimilarCarLicenses(List<String> similarCarLicenses) {
            this.similarCarLicenses = similarCarLicenses;
        }
    }
}
