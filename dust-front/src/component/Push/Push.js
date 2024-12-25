//실시간 미세먼지 좋음,나쁨을 표시하는 컴포넌트
import React, { useEffect, useState } from "react";
import axios from "axios";

const Push = () => {
    const [alert, setAlert] = useState(null); //데이터를 저장하는 상태

    useEffect(() => {
        //데이터를 서버에서 가져오는 함수
        const fetchAlert = async () => {
            try {
                const response = await axios.get("/api/alert"); //서버로부터 데이터를 가져옴
                setAlert(response.data); //가져온 데이터를 상태에 저장
            } catch (error) {
                console.error(error); //요청 실패 시 에러 출력
            }
        };

        //컴포넌트가 처음 렌더링될 때 즉시 데이터를 요청
        fetchAlert();

        //매 시각마다 미세먼지데이터가 공공데이터에 올라오기에
        // 현재 시간 기준으로 다음 정각까지의 남은 시간을 계산
        const now = new Date();
        const nextHour = new Date();
        nextHour.setHours(now.getHours() + 1, 0, 0, 0); //다음 정각을 설정
        const timeUntilNextHour = nextHour - now; //현재 시간과 다음 정각의 차이

        //다음 정각에 데이터 요청 시작
        const initialTimeout = setTimeout(() => {
            fetchAlert(); // 정각에 첫 요청
            const interval = setInterval(fetchAlert, 60 * 60 * 1000); //이후 1시간 간격으로 데이터 요청

            //컴포넌트가 언마운트될 때 interval 정리
            return () => clearInterval(interval);
        }, timeUntilNextHour);

        //컴포넌트가 언마운트될 때 timeout 정리
        return () => clearTimeout(initialTimeout);
    }, []);

    return (
        <div
            style={{
                backgroundColor: "white",
                borderRadius: "12px",
                width: "200px",
                boxShadow: "3px 3px 3px gray",
                padding: "10px",
                textAlign: "center",
            }}
        >
            <h2 style={{ fontSize: "18px", marginBottom: "10px" }}>미세먼지 알림</h2>
            {alert ? (
                <div>
                    <p style={{ margin: "5px 0", fontSize: "14px" }}>{alert.time}</p> {/*시간 표시 */}
                    <span
                        style={{
                            fontSize: "16px",
                            fontWeight: "bold",
                            color: alert.status === "나쁨" ? "red" : "green", //상태에 따라 텍스트 색상 변경
                        }}
                    >
                        {alert.status} {/*미세먼지 상태 표시 */}
                    </span>
                </div>
            ) : (
                <p>데이터를 불러오는 중...</p>
                )}
        </div>
    );
};

export default Push;