#!/bin/bash
jarName=/data/im/scheduler-0.1.jar
logFile=/data/im/log/scheduler/scheduler.log
sqlFile=/data/im/log/sql/scheduler.log
profile=test
slowSqlMs=5000
ps -ef|grep -v grep|grep $jarName|while read u p o
do
kill -9 $p
done
exec ${JAVA_HOME}/bin/java -jar -Dfile.encoding=UTF-8 -jar -Xms1G -Xmx1G -Dlogging.file=$logFile -Dp6spy.config.logfile=$sqlFile -Dp6spy.config.executionThreshold=$slowSqlMs -Dspring.profiles.active=$profile $jarName > /dev/null &
