package com.example.infrachipback.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PushService {

    //AirService를 통해 미세먼지데이터를 가져옴
    @Autowired
    private AirService airService;

    //최신 알림 데이터를 저장하는 변수
    @Getter
    private Map<String, String> latestAlert = new HashMap<>();

    //최신 미세먼지 데이터를 가져와 알림 상태를 업데이트하는 메서드
    public void updateLatestAlert() {
        try {
            //시간대별 미세먼지 데이터를 가져옴
            List<Map<String, String>> airData = airService.getHourlyAirQuality();

            //데이터가 존재할 경우
            if (!airData.isEmpty()) {
                Map<String, String> latestData = airData.get(0); //최신 데이터를 가져옴
                String pm10 = latestData.get("PM10"); //PM10
                String pm25 = latestData.get("PM2.5"); //PM2.5

                //PM 데이터가 유효한 경우
                if (pm10 != null && pm25 != null) {
                    try {
                        int pm10Value = Integer.parseInt(pm10); //PM10을 정수로
                        int pm25Value = Integer.parseInt(pm25); //PM2.5을 정수로

                        latestAlert.clear(); //초기화
                        latestAlert.putAll(latestData); //최신 데이터를 저장

                        //상태를 좋음 또는 나쁨으로 설정
                        if (pm10Value > 10 || pm25Value > 5) {
                            latestAlert.put("status", "나쁨");
                        } else {
                            latestAlert.put("status", "좋음");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("에러");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
