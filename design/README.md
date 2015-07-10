# Design Files
This directory contains image assets for the design of the Chasing Pictures app.

## Folder `gimp`
This folder contains image assets that have been trimmed and scaled to have an appropriate size for converting them to the different sizes used by Android. They are in *.xcf GIMP file format. Before scaling these assets, export them as PNG files.

## Folder `png-originals`
This folder contains the original image assets output by the design process. If an image is not available in GIMP format, the version from this folder should be used after trimming it to the right size (if necessary).

## Script `androidImageAssets.sh`
This script scales an image file to the appropriate sizes used in Android.
1. Put the images to be scaled in a subfolder (e.g. “original”)
2. Call `./androidImageAssets.sh`
3. Enter the name of the subfolder (“original”)
4. Enter the target width/height *(whichever is larger!)* (in dp) of the image asset. For best results, this is the original size of the image divided by 4.
5. Use `cp -R drawable-* ../ChasingPictures/app/src/main/res/` to copy the generated assets to the project folder.
6. Run `rm -rf drawable-*` and `rm original/*` to remove all files before handling more assets.
