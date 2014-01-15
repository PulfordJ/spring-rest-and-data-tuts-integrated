spring-rest-and-data-tuts-integrated
====================================

This is an integrated version of the official spring data and restful tutorials. 

The tests are a bit messy and non-exhaustive but I just wanted a full vertical delivery working before I began on my own API. On the upside I think my build script is better than either tutorials.

Simply run:
```gradle
gradle clean build functionalTestAuto
```

and it will clean, compile, test and then automatically run the app via tomcat, run the functional tests, and then close the server. In addition because build does not run the integration tests you get finer control.

I've not licenced this but I ask that you consider uploading any improvements you make to this general framework.
