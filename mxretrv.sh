#!/bin/bash

if [ ! -f /usr/local/bin/mxretrv-1.0-SNAPSHOT-jar-with-dependencies.jar ]; then
    echo "JAR File not found in /usr/local/bin/"
    echo "Make sure to execute install.sh before running this program"
    exit 1
fi

cdv=`pwd`

if [ ! "$cdv" = "/usr/local/bin/" ]; then
    echo "Make sure to execute install.sh before running this program"
    exit 1
fi



java -jar /usr/local/bin/mxretrv-1.0-SNAPSHOT-jar-with-dependencies.jar $@