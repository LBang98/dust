package com.example.infrachipback.controller;

import com.example.infrachipback.service.AirService;
import com.example.infrachipback.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") //api경로로 들어오는 요청을 처리
public class AirController {

    //AirService 의존성 주입
    @Autowired
    private AirService airService; //미세먼지데이터를 처리하는 서비스
    //PushService 의존성 주입
    @Autowired
    private PushService pushService; //알림을 처리하는 서비스

    //특정 지역의 미세먼지데이터를 반환하는 API
    @GetMapping("/data")
    public Map<String, Map<String, String>> getAirQuality(@RequestParam String sidoName) {
        try {
            //airService를 통해 미세먼지데이터를 가져옴
            return airService.getAirQuality(sidoName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //시간대 별 미세먼지데이터를 반환하는 API
    @GetMapping("/hourly")
    public List<Map<String, String>> getHourlyAirQuality() throws Exception {
        //airService를 통해 시간대별 미세먼지데이터를 가져옴
        return airService.getHourlyAirQuality();
    }

    //미세먼지 좋음 나쁨 반환하는 API
    @GetMapping("/alert")
    public Map<String, String> getAlerts() {
        //최신 데이터를 업데이트
        pushService.updateLatestAlert();

        //최신 데이터를 반환
        return pushService.getLatestAlert();
    }
}
