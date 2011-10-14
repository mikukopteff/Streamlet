#!/usr/bin/env bash

cd $(dirname $0)
java -Xmx256m -XX:MaxPermSize=512m -jar sbt-launch-*.jar "$@"

