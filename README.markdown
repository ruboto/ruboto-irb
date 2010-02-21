#Ruboto-IRB

[http://ruboto.com](http://ruboto.com)

To get this working on Android, you'll need to do two things:

Copy local.properties.EXAMPLE to local.properties and adjust the SDK location.

This is particularly true if you are doing development with the command line and vi, and not wussing out by using some girlyman IDE.  Just sayin'.

For example,

     $ cp local.properties.EXAMPLE local.properties
     $ vi local.properties

  Modify the "dx" tool for the target platform to support 1024M of memory. Edit SDK_PATH/platforms/android-1.x/tools/dx, uncomment javaopts and set it to javaOpts="-Xmx1024M" (you may need a higher value than 1024 on some systems).

     $ ant debug     # build package
     $ ant reinstall # build and install package
     $ adb -[e|d] install -r bin/IRB-debug.apk # manual install (emulator, device)

There are also some prebuilt packages available: [github.com/headius/ruboto-irb/downloads](http://github.com/headius/ruboto-irb/downloads).

That's it! Have fun!

##Credits

Ruby Icon obtained from [Ruby Visual Identity Team](http://rubyidentity.org/), CC ShareAlike 2.5.
