#!/bin/bash

echo "================================"
echo "  MCP Gateway 서버 시작"
echo "  포트: 8082"
echo "================================"

# 기존 8082 포트 프로세스 종료
PID=$(lsof -ti :8082)
if [ -n "$PID" ]; then
  echo "기존 서버 종료 중... (PID: $PID)"
  kill -9 $PID
  sleep 1
fi

cd "$(dirname "$0")"

JAR_FILE="build/libs/mcp-gateway-0.0.1-SNAPSHOT.jar"

# jar 파일 없으면 먼저 빌드
if [ ! -f "$JAR_FILE" ]; then
  echo "jar 파일 없음. 빌드 중..."
  ./gradlew bootJar -x test
fi

echo "서버 시작 중..."
java -jar $JAR_FILE
