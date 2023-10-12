port=${1}
	if [ ! -n "$port" ]; then
	echo "请输入项目端口号!"
	else
	echo "停止端口[${port}]的springboot程序..."
	cmd=`curl -i -X POST http://127.0.0.1:${port}/adviceshop/shutdown`
	ret=${cmd}
	echo "$ret"
	fi

