#!/usr/bin/env bash

cp -a ~/.m2/repository/org/jruby/jruby-complete/9.2.6.0-SNAPSHOT/jruby-complete-9.2.6.0-SNAPSHOT.jar libs/
cd src/main/java
find * -type f | grep "org/jruby/" | sed -e 's/\.java//g' | sort > ../../../overridden_classes.txt
cd - >/dev/null

while read p; do
  unzip -Z1 libs/jruby-complete-9.2.6.0-SNAPSHOT.jar | grep "$p\\.class" > classes.txt
  unzip -Z1 libs/jruby-complete-9.2.6.0-SNAPSHOT.jar | egrep "$p(\\\$[^$]+)*\\.class" >> classes.txt
  if [[ -s classes.txt ]] ; then
    zip -d -@ libs/jruby-complete-9.2.6.0-SNAPSHOT.jar <classes.txt
    if [[ ! "$?" == "0" ]] ; then
      zip -d libs/jruby-complete-9.2.6.0-SNAPSHOT.jar "$p\\.class"
    fi
  fi
  rm classes.txt
done < overridden_classes.txt

rm overridden_classes.txt
