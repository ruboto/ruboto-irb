#Ruboto-IRB

[http://ruboto.com](http://ruboto.com)

To get this working on Android, you'll need to do two things:

  * Copy local.properties.EXAMPLE to local.properties and adjust the SDK location.

This is particularly true if you are doing development with the command line and vi, and not wussing out by using some girlyman IDE.  Just sayin'.

For example,

     $ cp local.properties.EXAMPLE local.properties
     $ vi local.properties

 * Modify the "dx" tool for the target platform to support 1024M of memory. Edit SDK_PATH/platforms/android-1.x/tools/dx, uncomment javaopts and set it to javaOpts="-Xmx1024M". (you may need a higher value than 1024 on some systems)

     $ ant debug     # build package
     $ ant reinstall # build and install package
     $ adb -[e|d] install -r bin/IRB-debug.apk # manual install (emulator, device)

That's it! Have fun!

##Appendix

Some useful Android stuff:

See [http://developer.android.com/guide/developing/other-ide.html](http://developer.android.com/guide/developing/other-ide.html) for help on running assorted commands from a shell.

Much of what follows was stolen from that site.

You need an emmulator (AKA an AVD, or Android Virtual Device) to test the app (unless you want to actaully deploy to your phone over and over)

If you have already created one, skip this.  Otherwise, here's what to do to get an AVD:


 First, you need to select a "deployment target." To view available targets, execute:

    android list targets
 
For example:

		james@james06:~/ngprojects/ruboto-irb$ android list targets
		Available Android targets:
		id: 1
		     Name: Android 1.1
		     Type: Platform
		     API level: 2
		     Skins: HVGA-P, QVGA-L, QVGA-P, HVGA (default), HVGA-L
		id: 2
		     Name: Android 1.5
		     Type: Platform
		     API level: 3
		     Skins: HVGA-P, QVGA-L, QVGA-P, HVGA (default), HVGA-L
		id: 3
		     Name: Google APIs
		     Type: Add-On
		     Vendor: Google Inc.
		     Description: Android + Google APIs
		     Based on Android 1.5 (API level 3)
		     Libraries:
		      * com.google.android.maps (maps.jar)
		          API for Google Maps
		     Skins: QVGA-P, HVGA-L, HVGA (default), QVGA-L, HVGA-P
   

Find the target that matches the Android platform upon which you'd like to run your application. Note the integer value of the id ‚Äî you'll use this in the next step.

    android create avd --name <your_avd_name> --target <targetID>

For example:

    android create avd --name super-bad-avd --target 3

You should get prompted to answer a few questions. 


Now kick off an AVD ...

    emulator -avd <your_avd_name>

... and install the .apk on the emulator:

    adb install /path/to/your/application.apk

The path is typically in the ./bin directory of your project folder

    adb install ./bin/MySuperBadApp.apk

If there is more than one emulator running, you must specify the emulator upon which to install the application, 
by its serial number, with the -s option. For example:

    adb -s emulator-5554 install ./bin/MySuperBadApp.apk

If you don't know serial numbers of the running emulators, you can get a list using

    adb devices

As you develop your app, you'll need to reploy to the emulator.  If you get an error saying the app is already installed ...

Failure [INSTALL_FAILED_ALREADY_EXISTS]

... you can try using the -r option (reinstall, keeping data)

    adb install -r ./bin/MySuperBadApp.apk

to replace it.

Or uninstall it (use the -k option to keep data and cache directories, if you want that)

    adb uninstall -k com.your.app.package.Name 

Some debugging help:

    adb -s <avd-serial> logcat

will cat the log of the specified emulator.  Works for the actual device as well.  Use 

    adb devices

to get the serials.

More help at [http://developer.android.com/guide/developing/tools/adb.html](http://developer.android.com/guide/developing/tools/adb.html)

##Credits

Ruby Icon obtained from [Ruby Visual Identity Team](http://rubyidentity.org/), CC ShareAlike 2.5.
