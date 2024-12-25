//시간대별 미세먼지 데이터를 보여주는 차트
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Line } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from "chart.js";

//차트에 필요한 기능 등록
ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

const AirQualityChart = () => {
    const [airData, setAirData] = useState([]); //시간대별 데이터 저장
    const [loading, setLoading] = useState(true); //로딩 상태

    useEffect(() => {
        const fetchAirQuality = async () => {
            try {
                const response = await axios.get("/api/hourly"); //API 호출
                setAirData(response.data); //데이터 저장
                setLoading(false); //로딩 상태 해제
            } catch (error) {
                console.error(error);
                setLoading(false); //로딩 상태 해제
            }
        };

        fetchAirQuality();
    }, []);

    if (loading) {
        return <div>Loading...</div>; //로딩 중일 때 표시
    }

    const labels = airData.map((data) => data.time); //시간
    const pm10Data = airData.map((data) => parseInt(data.PM10) || 0); //PM10
    const pm25Data = airData.map((data) => parseInt(data["PM2.5"]) || 0); //PM2.5

    const chartData = {
        labels: labels,
        datasets: [
            {
                label: "PM10",
                data: pm10Data,
                borderColor: "rgba(255, 99, 132, 1)",
                backgroundColor: "rgba(255, 99, 132, 0.2)",
                fill: true,
            },
            {
                label: "PM2.5",
                data: pm25Data,
                borderColor: "rgba(54, 162, 235, 1)",
                backgroundColor: "rgba(54, 162, 235, 0.2)",
                fill: true,
            },
        ],
    };

    return (
        <div style={{ width: "40%", margin: "0 auto" }}>
            <h2 style={{ textAlign: "center" }}>시간대별 미세먼지</h2>
            {/*시간대별 차트*/}
            <Line data={chartData} />
        </div>
    );
};

export default AirQualityChart;