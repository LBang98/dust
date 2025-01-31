# Java 17 기반의 경량 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일을 Docker 컨테이너 내부로 복사
COPY build/libs/*.jar app.jar

# Spring Boot 애플리케이션이 사용하는 포트 개방
EXPOSE 8080

# 애플리케이션 실행 (urandom 설정 포함)
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
