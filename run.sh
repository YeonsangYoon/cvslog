#!/bin/sh

## 1. 파라미터 확인
if [ -z $1 ]; then
        echo "enter command"
        echo "ex) ./run.sh {start|stop}"
        exit
fi

## 2. script가 위치한 디렉토리로 이동
cd "$(dirname "$0")" || exit 1

## 3. 환경 설정 파일 실행 (setenv.sh)
if [ -e "setenv.sh" ]; then
        . "./setenv.sh"
fi

## 4. Spring 운영환경 설정
JAR_FILE=cvslog-0.0.1.jar
JAVA_OPT="-Xmx256m -Xmx256m -Dspring.profiles.active=prod "$JAVA_OPT

## 5. 프로그램 실행
start() {
        echo "start application"
        nohup java $JAVA_OPT -jar $JAR_FILE > /dev/null 2> /dev/null &

        # PID 저장
        echo $! > ./application.pid
        echo "applcation started"
}

stop() {
        # 프로그램이 실행중이 아니면 종료
        if [ -f ./application.pid ]; then
                echo "stop application"

                # Process ID 식별
                PID=`cat ./application.pid`
                echo "application is running as $PID"

                # 종료
                kill -15 $PID
                echo "application stopped"

                # pid 파일 삭제
                rm ./application.pid
        else
                echo "application not running"
        fi
}

case $1 in
        start)
                start
        ;;
        stop)
                stop
        ;;
        restart)
                stop
                sleep 10
                start
        ;;
esac