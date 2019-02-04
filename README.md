**For a very basic sample on how to listen for text messages, see [this gist](https://gist.github.com/mc0239/b016667f4fa5bc5228214ea0514ec2a6).**

# SMS Radar [![Build Status](https://travis-ci.org/tuenti/SmsRadar.svg?branch=master)](https://travis-ci.org/tuenti/SmsRadar) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tuenti.smsradar/library/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tuenti.smsradar/library) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SmsRadar-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1328)

Reading incoming and outgoing text messages using an Android application across different Android SDK versions is not trivial. If you don't want to appear as SMS application and your application has to read incoming and outgoing SMSes, this is your library.

This Android library offers you two main classes to use to start listening text messages. SMS Radar is implemented on top of an Android service. To start / stop listening text messages use ``SmsRadar.initializeSmsRadarService(context, listener)`` with a ``Context`` instance and a ``SmsListener`` implementation.

Each time the device owner receives or sends SMS the ``SmsListener`` used to initialize ``SmsRadarService`` service will be notified in on ``onSmsSent(Sms sms)`` or ``onSmsReceived(Sms sms)`` methods.

The SMS information provided on ``SmsListener`` methods is:

* SMS address. MSISDN used to send/receive the SMS.
* SMS date. SMS local creation date.
* SMS content. Message sent in the SMS.
* SMS type.  See ``SmsType`` enum.

This library works on Android 4.4 (API 19) or higher versions.

Download
--------

Download the project, compile it using ```mvn clean install``` import ``smsradar-1.0.4.jar`` into your project.

Or declare it into your pom.xml

```xml
<dependency>
    <groupId>com.tuenti.smsradar</groupId>
    <artifactId>library</artifactId>
    <version>1.0.4</version>
</dependency>
```


Or into your build.gradle
```groovy
dependencies{
    compile 'com.tuenti.smsradar:library:1.0.4'
}
```


Usage
-----

Declare permission to read SMS content provider in your AndroidManifest:

```xml
<uses-permission android:name="android.permission.READ_SMS"/>
```

Declare SmsRadar service in your AndroidManifest:

```xml
<service
		android:name="com.tuenti.smsradar.SmsRadarService" />
```

To use SMS Radar library you only have to initialize ``SmsRadar`` using a Context instance and one ``SmsListener``:

```java
SmsRadar.initializeSmsRadarService(context, new SmsListener() {
			@Override
			public void onSmsSent(Sms sms) {
				showSmsToast(sms);
			}

			@Override
			public void onSmsReceived(Sms sms) {
				showSmsToast(sms);
			}
		});
```

You can stop the SMS observer like in this sample:

```java
SmsRadar.stopSmsRadarService(context);
```

Credits & Contact
-----------------

SmsRadar was created by [Android team at Tuenti Technologies S.L.](http://github.com/tuenti). You can follow Tuenti
engineering team on Twitter [@tuentieng](http://twitter.com/tuentieng).


License
-------

SmsRadar is available under the Apache License, Version 2.0. See LICENSE.txt file for more info.
