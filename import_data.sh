#!/bin/sh

rm -rf derby-database/
./datum.sh import data/*.rules data/*.templates data/*.names
