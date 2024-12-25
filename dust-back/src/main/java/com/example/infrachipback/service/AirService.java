package com.example.infrachipback.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AirService {

    //application.properties에서 시크릿키 받아옴
    @Value("${air.service.key}")
    private String serviceKey; //시크릿 키

    //특정 시도의 실시간 미세먼지 데이터를 가져오는 메서드
    public Map<String, Map<String, String>> getAirQuality(String sidoName) throws Exception {

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); //시크릿키
        urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); //JSON 형식 반환
        urlBuilder.append("&" + URLEncoder.encode("sidoName", "UTF-8") + "=" + URLEncoder.encode(sidoName, "UTF-8")); //장소
        urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8"));

        //HTTP 연결 및 요청
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        //응답 데이터
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); //정상
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream())); //오류
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        //JSON으로 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(sb.toString());
        //필요한 데이터를 추출
        JsonNode items = root.path("response").path("body").path("items");

        //      미세먼지 데이터를 저장할 Map 생성
        Map<String, Map<String, String>> airData = new HashMap<>();
        if (items.isArray()) {
            for (JsonNode item : items) {
                String stationName = item.path("stationName").asText(); //장소
                String pm10Value = item.path("pm10Value").asText(); //PM10
                String pm25Value = item.path("pm25Value").asText(); //PM2.5

                //장소 데이터를 저장할 Map 생성
                Map<String, String> data = new HashMap<>();
                data.put("PM10", pm10Value);
                data.put("PM2.5", pm25Value);
                //장소별 데이터 저장
                airData.put(stationName, data);
            }
        }

        return airData; //미세먼지데이터 반환
    }

    //시간대별 대기 질 데이터를 가져오는 메서드
    public List<Map<String, String>> getHourlyAirQuality() throws Exception {
        //장소
        String stationName = "중구";

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); //시크릿키
        urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); //JSON 형식 반환
        urlBuilder.append("&" + URLEncoder.encode("stationName", "UTF-8") + "=" + URLEncoder.encode(stationName, "UTF-8")); //장소 이름
        urlBuilder.append("&" + URLEncoder.encode("dataTerm", "UTF-8") + "=" + URLEncoder.encode("daily", "UTF-8")); //하루 데이터
        urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8"));

        //HTTP 연결 및 요청
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        //응답 데이터
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); //정상
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream())); //오류
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        //JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(sb.toString());
        JsonNode items = root.path("response").path("body").path("items");

        //시간대별 데이터를 저장할 List 생성
        List<Map<String, String>> airQualityData = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode item : items) {
                Map<String, String> data = new HashMap<>();
                String dataTime = item.path("dataTime").asText(); // 시간
                String pm10Value = item.path("pm10Value").asText(); // PM10
                String pm25Value = item.path("pm25Value").asText(); // PM2.5

                data.put("time", dataTime); //시간
                data.put("PM10", pm10Value); //PM10
                data.put("PM2.5", pm25Value); //PM2.5

                airQualityData.add(data); //리스트에 데이터 추가
            }
        }
        return airQualityData; //시간대별 미세먼지 데이터 반환
    }
}
