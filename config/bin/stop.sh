function get_default_ip() {
  local ip=$( /sbin/ifconfig | grep "inet " | awk '{print $2}' | sed 's/addr://' | grep "$NETWORK" | head -1 )
  [ ! -z "$ip" ] && echo $ip && return
  /sbin/ifconfig | grep "inet " | awk '{print $2}' | sed 's/addr://' | grep -v "127.0.0.1" | head -1
}

SCRIPT_DIR=`pwd`
PID_FILE="$SCRIPT_DIR/.service_pid"
[ ! -f "$PID_FILE" ] && echo "no pid file" && exit 0
stop_pid=$(cat $PID_FILE)

echo "use kill to kill the service: ${stop_pid}"
kill $stop_pid

i=0
while ps -p $stop_pid
do
sleep 2
echo wait stop service
((i++))
if (($i>4));then
echo break!
break
fi
done

if ps -p $stop_pid
then
echo ""
echo "kill the service: ${stop_pid}"
kill -9 $stop_pid
fi

