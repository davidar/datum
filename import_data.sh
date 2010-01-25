#!/bin/sh

rm -rf derby-database/
./datum.sh import data/*.facts data/*.rules data/*.templates data/*.names
