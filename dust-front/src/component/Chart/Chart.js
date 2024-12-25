//지역별 미세먼지 데이터를 보여주는 차트
import React, { useEffect, useState } from "react";
import { LineChart, Line, CartesianGrid, XAxis, YAxis, Tooltip } from "recharts";
import axios from "axios";

const Chart = () => {
    const [data, setData] = useState([]); //데이터
    const [selectedRegion, setSelectedRegion] = useState("서울"); //선택된 지역

    useEffect(() => {
        fetchData(selectedRegion); //select할 때 데이터 재요청
    }, [selectedRegion]);

    //선택된 지역의 데이터를 가져오는 함수
    const fetchData = (region) => {
        axios
            .get("/api/data", { params: { sidoName: region } })
            .then((response) => {
                const regionData = response.data;
                const formattedData = Object.entries(regionData).map(([station, values]) => ({
                    station, // 측정소 이름
                    PM10: parseInt(values["PM10"], 10) || 0, //PM10
                    PM25: parseInt(values["PM2.5"], 10) || 0, //PM2.5
                }));
                setData(formattedData); //데이터 상태 업데이트
            })
            .catch((error) => console.error("Error fetching data:", error));
    };

    return (
        <div style={{ backgroundColor: "white", padding: "20px", textAlign: "center" }}>
            {/*지역 선택*/}
            <select
                value={selectedRegion}
                onChange={(e) => setSelectedRegion(e.target.value)}
                style={{
                    marginBottom: "20px",
                    padding: "8px 12px",
                    fontSize: "16px",
                    borderRadius: "4px",
                    border: "1px solid #ccc",
                }}
            >
                {/*미세먼지 지역 리스트*/}
                <option value="서울">서울</option>
                <option value="부산">부산</option>
                <option value="대구">대구</option>
                <option value="인천">인천</option>
                <option value="광주">광주</option>
                <option value="대전">대전</option>
                <option value="울산">울산</option>
                <option value="제주">제주</option>
            </select>

            {/*지역별 PM10과 PM2.5를 표시하는 차트*/}
            <div style={{ marginTop: "20px" }}>
                <LineChart width={500} height={300} data={data}>
                    <Line type="monotone" dataKey="PM10" stroke="#8884d8" /> {/*PM10 차트*/}
                    <Line type="monotone" dataKey="PM25" stroke="#82ca9d" /> {/*PM2.5 차트*/}
                    <CartesianGrid stroke="#ccc" /> {/* 격자선 */}
                    <XAxis
                        dataKey="station"
                        interval={0}
                        tick={{ fontSize: 12 }}
                    /> {/*장소*/}
                    <YAxis /> {/*PM*/}
                    <Tooltip />
                </LineChart>
            </div>
        </div>
    );
};

export default Chart;