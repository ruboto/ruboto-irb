#Ruboto-IRB

[http://ruboto.org](http://ruboto.org)

##Installation

The simplest way to get started with ruboto-irb is to install a prebuilt binary package.  These packages can be found at [github.com/ruboto/ruboto-irb/downloads](http://github.com/ruboto/ruboto-irb/downloads).

If you'd like more control over the build and installation process, you can do it manually using the following instructions.

###Customize the local.properties file

Copy *local.properties.EXAMPLE* to *local.properties* and edit *local.properties* to specify the SDK's location on your system.  For example:

     $ cp local.properties.EXAMPLE local.properties
     $ vi local.properties
     # In the editor, change the sdk.dir to point to the correct directory.

(The *vi* command in this and other sample command lines can be replaced with any editor command, such as *nano* or *mate*.)

###Override default memory settings (SDK Version 4 only):

  If your are using API SDK version 4 (or earlier?), you'll probably need to override the low default memory setting for that platform (more recent SDKs default to 1024 MB and generally do not need to be changed). To do this:

     $ vi $ANDROID_SDK_PATH/platforms/android-4/tools/dx
     #
     # *$ANDROID_SDK_PATH* should point to, or be replaced by, the root of your Android SDK directory tree. 
     # Uncomment the line starting with *javaOpts* and set it to 1024 MB, or higher, if necessary, e.g:
     # javaOpts="-Xmx1024M"

###Update the project:

     android update project -n rubuto-irb --path . --subprojects 

###Build and install the project:

  To build the debug version, issue the command:

     $ ant debug     # build package

  To both build the debug version and install it on the currently selected target device or emulator, do this:

     $ ant debug install   # build and install package

  If you'd like control of the install at a lower level, you can use *adb*:

     $ adb -[e|d] install -r bin/IRB-debug.apk # manual install (emulator, device)

NOTE: To install it to an emulator image, you need to have the emulator running. 

## Resources

You can also check out the [JRuby on Android](http://groups.google.com/group/ruboto) Google group for more information.

That's it! Have fun!

##Screenshot

![Ruboto-IRB screenshot](https://github.com/ruboto/ruboto-irb/raw/master/doc/ruboto-screenshot-landscape.png)

##Credits

Ruby Icon obtained from [Ruby Visual Identity Team](http://rubyidentity.org/), CC ShareAlike 2.5.
Editor/script support from Pascal Chatterjee's [jruby-for-android](http://code.google.com/p/jruby-for-android/) project, adapted by [Scott Moyer](http://github.com/rscottm/).
