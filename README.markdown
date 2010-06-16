#Ruboto-IRB

[http://ruboto.com](http://ruboto.com)

To get this working on Android, you'll need to do two things:

Copy local.properties.EXAMPLE to local.properties and adjust the SDK location.

This is particularly true if you are doing development with the command line and vi, and not wussing out by using some girlyman IDE.  Just sayin'.

For example,

     $ cp local.properties.EXAMPLE local.properties
     $ vi local.properties

  Modify the "dx" tool for the target platform to support 1024M of memory. Edit SDK_PATH/platforms/android-1.x/tools/dx, uncomment javaopts and set it to javaOpts="-Xmx1024M" (you may need a higher value than 1024 on some systems). This is only needed for API SDK Version 4, more recent SDKs default to 1GB.

     $ ant debug     # build package
     $ ant install   # build and install package
     $ adb -[e|d] install -r bin/IRB-debug.apk # manual install (emulator, device)

There are also some prebuilt packages available: [github.com/ruboto/ruboto-irb/downloads](http://github.com/ruboto/ruboto-irb/downloads).

You can also check out the [JRuby on Android](http://groups.google.com/group/ruboto) Google group for more information.

That's it! Have fun!

##Screenshot

![Ruboto-IRB screenshot](http://cloud.github.com/downloads/ruboto/ruboto-irb/ruboto-screenshot-landscape.png)

##Credits

Ruby Icon obtained from [Ruby Visual Identity Team](http://rubyidentity.org/), CC ShareAlike 2.5.
Editor/script support from Pascal Chatterjee's [jruby-for-android](http://code.google.com/p/jruby-for-android/) project, adapted by [Scott Moyer](http://github.com/rscottm/).

