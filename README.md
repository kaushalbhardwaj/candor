# Candor
Candor is an experimentation platform, empowering you to test, learn and deploy better products in the market

## Setup
Make sure you have the jcenter repo in your project level `build.gradle`  
```gradle
allprojects {
    repositories {
        jcenter()
    }
}
```

Add the dependency to your lib/app `build.gradle`  
```gradle
dependencies {
    implementation 'com.github.kaushalbhardwaj:candor........
}
```

## Integration
Initialize Candor in your main activity. Usually this should be done in onCreate method of the main activity.

```java
String projectToken = YOUR_PROJECT_TOKEN; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad" 
Candor candor = Candor.getInstance(this, projectToken);
```
Remember to replace `YOUR_PROJECT_TOKEN` with the token provided to you by Candor (TODO have to know more)

## Activate Experiment
```java
String experiment = KEY_EXPERIMENT;
String userId = USER_ID;
Variant variant = candor.activateExperiment(experiment, userId);
MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
```
Remember to replace `KEY_EXPERIMENT` the key of the experiment you want to experiment in your app
Remember to replace `USER_ID` the user id of the user

After activating the experiment you will get the variant for the user which you can use to do different things
(TODO much better can be written with the code)

## Tracking
After installing the library into your Android app, you can track events in your app using the name of the event and properties associated with the event.

With the `candor` object created in [the last step](#integration) a call to `track` is all you need to send additional events to Candor.

```java
JSONObject properties = new JSONObject();
props.put("Property name", "Property Value");
props.put("Property 2", "Property Value 2");
candor.track("Event name", properties);
```

