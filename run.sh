#!/bin/sh

## 1. 파라미터 확인
if [ -z $1 ]; then
        echo "enter command"
        echo "ex) ./run.sh {start|stop}"
        exit
fi

## 2. 환경 설정 파일 실행 (setenv.sh)
if [ -e "env.sh" ]; then
        ./setenv.sh
fi

## 3. Spring 운영환경 설정
JAR_FILE=cvslog-0.0.1.jar
JAVA_OPT="-Xmx256m -Xmx256m -Dspring.profiles.active=prod "$JAVA_OPT

## 4. 프로그램 실행
case $1 in
        start)
                echo "start application"
                nohup java $JAVA_OPT -jar $JAR_FILE > /dev/null 2> /dev/null &

                # PID 저장
                echo $! > ./application.pid
                echo "application started"

        ;;
        stop)
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
        ;;
esac