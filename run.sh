#!/bin/bash
base=`dirname $0`
echo $base

short_pat=$base/data/short_pat.txt
long_pat=$base/data/long_pat.txt

target=$base/target/SojUdfPerf-1.0-SNAPSHOT-jar-with-dependencies.jar
short_32k_tgz=$base/data/short_32k.tgz
long_32k_tgz=$base/data/long_32k.tgz

short_32k_txt=short_32k.txt
long_32k_txt=long_32k.txt

function run_core() {
  local iter=$1
  local pat=$2
  local input=$3
  local out=$4
  local i=1
  while [ $i -le $iter ];
  do
    java -jar $target -p $pat -s $input >> $out
    i=$(($i+1)) 
  done
}

function run_short_core() {
  local iter=$1
  local batch=$2
  local short32k=$3
  local timestamp=`date +%Y%m%d%H%M%S`
  local out=short_${batch}_${timestamp}
  local tmp=/tmp/sojnvl_data_${timestamp}.txt
  head -n $batch $short32k > $tmp
  run_core $iter $short_pat $tmp $out
  rm $tmp
}

function run_short() {
  local iter=$1
  local batch=$2 #1000, 2000, 4000, 8000, 16000, 32000
  tar zxvf $short_32k_tgz
  run_short_core $iter $batch $short_32k_txt
  rm $short_32k_txt
}

function run_long_core() {
  local iter=$1
  local batch=$2
  local long32k=$3
  local timestamp=`date +%Y%m%d%H%M%S`
  local out=long_${batch}_${timestamp}
  local tmp=/tmp/sojnvl_data_${timestamp}.txt
  head -n $batch $long32k > $tmp
  run_core $iter $long_pat $tmp $out
  rm $tmp
}

function run_long() {
  local iter=$1
  local batch=$2 #1000, 2000, 4000, 8000, 16000, 32000
  tar zxvf $long_32k_tgz
  run_long_core $iter $batch $long_32k_txt
  rm $long_32k_txt
}

if [ $# -ne 2 ]
then
   echo "Specify <long|short> <1000|2000|4000|8000|16000|32000>"
   exit 1
fi

input=$1
batch=$2

func="run_$input"

eval $func 5 $batch
