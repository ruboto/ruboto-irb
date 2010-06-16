# How It Works

## The IRB

The IRB only uses IRB.java and HistoryEditText.java, which are conveniently separated into the org.ruboto.irb package now. It also obvious needs the jruby jars (in libs/). 

IRB.java does the heavy lifting, and HistoryEditText.java is what allows you to hit up and down on the dpad/joystick/tackball to cycle through history. 

## Scripts

Scripts have a little bit of a more complicated setup. In short, the Android API calls methods on RubotoActivity.java, which then calls the Ruby methods/blocks that you provide. 

### RubotoActivity.java

RubotoActivity.java extends Android's Activity class, which is how you write an [Acitivity](http://developer.android.com/reference/android/app/Activity.html). 

RubotoActivity.java is built from a RubotoActivity.erb file and a YAML file that lists all of the callbacks that it handles, complete with arguments, return types, and which interface that method is helping to implement. This allows the ERB file to do everything it needs to. 

RubotoActivity.java overrides *every* callback that Activity offers and keeps track of which one the Ruby script actually wants. (The Ruby script calls a method on RubotoActivity.java to tell it that it wants a certain callback, see below.) When a callback gets called, RubotoActivity.java then checks to see if the Ruby script has asked RubotoActivity.java to send the callback along to it. 

### ruboto.rb

ruboto.rb is really where magic happens. It's not used to run the irb at all (I think), but it's going to be the basis for writing full apps with Ruby, at least unless jrubyc --java becomes able to extend classes. It's basically the Ruby interface to RubotoActivity.java. 

When you use ruboto.rb, you call handle_name_of_callback methods that take blocks. These methods tell RubotoActivity.java that it needs to push that callback to the Ruby script and then define a method that RubotoActivity.java will call. 

It also has some sugar syntax to make it easier to write menus. I'm liking this route more the more I look at it, even though RubotoActivity.erb is scary. It should be easy to do more sugar like that **and to allow people to make plugins that are basically just defining a few extra methods of sugar**. That's big. 


