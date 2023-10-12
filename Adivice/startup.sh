# [] 中为可选
	# > 覆盖日志， >> 追加日志 : 将日志写进 logs/.log 文件
	# & 后台运行
	cmd=`nohup /home/caidouadivice/jdk1.8.0_11/bin/java -jar /home/caidouadivice/advice_store-0.0.1-SNAPSHOT.jar > consoleMsg.log 2>&1 &`
	# 执行
	${cmd}
	echo "启动完成"

