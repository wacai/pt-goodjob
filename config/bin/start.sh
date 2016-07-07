function get_default_ip() {
  local ip=$( /sbin/ifconfig | grep "inet " | awk '{print $2}' | sed 's/addr://' | grep "$NETWORK" | head -1 )
  [ ! -z "$ip" ] && echo $ip && return
  /sbin/ifconfig | grep "inet " | awk '{print $2}' | sed 's/addr://' | grep -v "127.0.0.1" | head -1
}

function get_jmx_port() {
  if [ $(is_available_port $JMX_PORT) = "yes" ];then
    echo $JMX_PORT && return
  fi

  for port in `seq 9999 10000 49999`;do
    if [ $(is_available_port $port) = "yes" ];then
      echo $port && return
    fi
  done

  for port in `seq 50999 500 59999`;do
    if [ $(is_available_port $port) = "yes" ];then
      echo $port && return
    fi
  done

  echo cannot found available jmx port && exit 1
}

function is_available_port() {
  local port=$1
  if [[ "$OSTYPE" = *linux* ]];then
    r=$( netstat -ant | awk '$4~":'$port'$"' )
  elif [[ "$OSTYPE" = *darwin* ]];then
    r=$( netstat -ant | awk '$6=="LISTEN"' | grep "\.$port " )
  else
    echo unknown system && exit 1
  fi

  if [ -z "$r" ];then
    echo "yes" # available
  else
    echo "no" # port has been used
  fi
}

[ -z $JAVA_HOME ] && export JAVA_HOME=/data/program/java8
JAVA_CMD="${JAVA_HOME}/bin/java"
SCRIPT_DIR=`pwd`
SERVICE_HOME="$( dirname $SCRIPT_DIR )"
LOG_DIR="${SCRIPT_DIR}"
LOG="$LOG_DIR/stdout.log"
PID_FILE="$SCRIPT_DIR/.service_pid"

# JVM
JAVA_OPTS="$JAVA_OPTS -verbose:gc -Xloggc:$LOG_DIR/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOG_DIR/java.hprof"
JAVA_OPTS="$JAVA_OPTS -XX:ErrorFile=$LOG_DIR/java_error.log"
JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=true"
JAVA_OPTS="$JAVA_OPTS -Dserver.tomcat.access-log-enabled=true"

# spring boot
JAVA_OPTS="$JAVA_OPTS -Dmanagement.port=-1"
JAVA_OPTS="$JAVA_OPTS -Dendpoints.shutdown.enabled=true"
JAVA_OPTS="$JAVA_OPTS -Dshell.telnet.enabled=false"

# JMX
JMX_PORT=${JMX_PORT:-9999}
JMX_OPTS="-Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

JMX_OPTS="$JMX_OPTS -Djava.rmi.server.hostname=$(get_default_ip)"
jmx_port=$(get_jmx_port)
JMX_OPTS="$JMX_OPTS -Dcom.sun.management.jmxremote.port=$jmx_port"
JMX_OPTS="$JMX_OPTS -Dcom.sun.management.jmxremote.rmi.port=$jmx_port"
JAVA_OPTS="$JAVA_OPTS $JMX_OPTS"


"$JAVA_CMD" $JAVA_OPTS -jar ./*.jar >$LOG 2>$LOG &
pid="$!"
sleep 1
if kill -0 "$pid" >/dev/null 2>&1; then
	echo $pid > "$PID_FILE"
    echo "service started, java pid: $pid"
else
    echo "start failed" 
fi
