#!/bin/bash

# Taken from: http://stackoverflow.com/a/22881244

read -p "Please enter the subfolder of the original images? " folder
read -p "How many DP (width) should the image have? " dp

for i in $(find $folder/. -type f -name "*[A-Z]*"); do mv "$i" "$(echo $i | tr A-Z a-z)"; done

mkdir drawable-ldpi
mkdir drawable-mdpi
mkdir drawable-tvdpi
mkdir drawable-hdpi
mkdir drawable-xhdpi
mkdir drawable-xxhdpi
mkdir drawable-xxxhdpi

cp $folder/* drawable-ldpi/
cp $folder/* drawable-mdpi/
cp $folder/* drawable-tvdpi/
cp $folder/* drawable-hdpi/
cp $folder/* drawable-xhdpi/
cp $folder/* drawable-xxhdpi/
cp $folder/* drawable-xxxhdpi/

sips -Z $(echo $dp*3/4 | bc) drawable-ldpi/*
sips -Z $(echo $dp | bc) drawable-mdpi/*
sips -Z $(echo $dp*4/3 | bc) drawable-tvdpi/*
sips -Z $(echo $dp*3/2 | bc) drawable-hdpi/*
sips -Z $(echo $dp*2 | bc) drawable-xhdpi/*
sips -Z $(echo $dp*3 | bc) drawable-xxhdpi/*
sips -Z $(echo $dp*4 | bc) drawable-xxxhdpi/*
