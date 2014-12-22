#!/bin/sh

# HEAP size
JAVA_MEM=1g

# GC log
GC_LOGGING=ON

# misc-seg home
M_HOME=`dirname $0`/..
M_HOME=`cd $M_HOME; pwd`

# classpath
M_CLASSPATH=$M_HOME/lib/*

# APP_MAIN
APP_MAIN="com.dmteam.Bootstrap"

########################################## find java #######################################

if [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA=`which java`
fi

if [ ! -x "$JAVA" ]; then
    echo "Could not find any executable java binary. Please install java in your PATH or set JAVA_HOME"
    exit 1
fi


########################################## JVM config #######################################

JAVA_OPTS="$JAVA_OPTS -Xms${JAVA_MEM}"
JAVA_OPTS="$JAVA_OPTS -Xmx${JAVA_MEM}"

JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"

JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"

# Disables explicit GC
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"

####################################### GC log config ######################################

if [ "x$GC_LOGGING" = "xON" ]; then
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintClassHistogram"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintTenuringDistribution"
  JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
  JAVA_OPTS="$JAVA_OPTS -Xloggc:${M_HOME}/logs/gc.log"
fi


# Causes the JVM to dump its heap on OutOfMemory.

JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -XX:HeapDumpPath=$M_HOME/logs/heapdump.hprof"


############################## DEFINE operations ################################

do_start()
{
    exec "$JAVA" $JAVA_OPTS -Dpath.home="$M_HOME" -cp "$M_CLASSPATH" $APP_MAIN
}

do_stop()
{
    echo "################## [misc-seg] system shutdown... #####################"

    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    echo "[misc-seg INFO] PID: "$javaps

    if [ -n "$javaps" ]; then
        PID=`echo $javaps | awk '{print $1}'`
    else
        echo "[misc-seg ERROR] no instance found. Nothing to do!"
        return
    fi

    touch "$M_HOME/.shutdown.watcher"

    while true; do
        if [ -z `ps -ef | grep $PID`]; then
            echo "[misc-seg INFO] shutdown succeed."
            exit 0
        else
            echo "[misc-seg INFO] shutdown waiting ..."
            sleep 1
        fi
    done
}



############################## Let's Go, what do you want ? ################################
case $1 in
    "start")
        do_start
        ;;
    "stop")
        do_stop
        ;;
    *)
        echo "[misc-seg ERROR] please input [start] / [stop] command."
        exit 0
        ;;
esac

