#!/bin/bash
jarName=/data/im/admin-0.1.jar
logFile=/data/im/log/admin/admin.log
sqlFile=/data/im/log/sql/admin.log
profile=test
slowSqlMs=5000
ps -ef|grep -v grep|grep $jarName|while read u p o
do
kill -9 $p
done
exec java -jar -Dfile.encoding=UTF-8 -jar -Xms1G -Xmx1G -Dlogging.file=$logFile -Dp6spy.config.logfile=$sqlFile -Dp6spy.config.executionThreshold=$slowSqlMs -Dspring.profiles.active=$profile $jarName > /dev/null &
