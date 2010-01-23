#!/bin/sh
export CLASSPATH=dist/datum.jar:lib/*
JAVA="java -Djava.util.logging.config.file=logging.properties"

if [ "$1" = "import" ]; then
    shift
    $JAVA cc.vidr.datum.tools.Import "$@"
else
    $JAVA cc.vidr.datum.tools.Console
fi
