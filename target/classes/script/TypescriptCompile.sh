#!/bin/bash

scriptDirectory="$(dirname $(readlink -f $0))"
resDirectory="$(dirname "$scriptDirectory")"
tsDirectory="$(dirname "$scriptDirectory")/ts/"

cd $tsDirectory
tsc

#cp -r "$tsDirectory/shader" "$resDirectory/static/js/"