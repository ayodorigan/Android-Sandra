# Castify Android and Amazon TV App
This is an app for VOD and runs on both android and amazon tv <br/>
It supports v4 and v5 castify versions <br/>
It customizes to the json settings hence flexibile to changes <br/>

## Manual Set up
Chane the following: <br/>
\*  `CHANNEL_URL`  in `com/tv/mars/tv/utils/GlobalVars.java` <br/>
\*  `APPLICATION_NAME`  in `values/strings.xml` <br/>
\*  `banner.png` in `drawable/banner.png`should be 320 x 180. <br/>
\*  `mipmap-mdpi/ic_launcher.png` should be 48 x 48. <br/>
\*  `mipmap-hdpi/ic_launcher.png` should be 72 x 72. <br/>
\*  `mipmap-xhdpi/ic_launcher.png`should be 96 x 96. <br/>
\*  `mipmap-xxhdpi/ic_launcher.png` should be 144 x 144. <br/>
\*  `mipmap-xxxhdpi/ic_launcher.png`  should be 192 x 192 <br/>
\* You should also change the package name of the app <br/>

## Auto-Setup
You can also do an automatic setup with our customized [Builder](https://github.com/Castify-ai/Android-Auto-Builder) which was made speicifally for this app. <br/>
The [Builder](https://github.com/Castify-ai/Android-Auto-Builder) will auutomatically set everything for you and respond back to you with a zipped file containing a signed .apk and .aab files which you can then submit to google playstore and Amazon Store.

