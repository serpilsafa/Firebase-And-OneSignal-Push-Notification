# Firebase-And-OneSignal-Push-Notification

I used firebase firemaworks 
---------------------------
1)  `implementation 'com.google.firebase:firebase-analytics:17.4.0' `
2)  `implementation 'com.google.firebase:firebase-database:19.3.0' `
3)  `implementation 'com.google.firebase:firebase-auth:19.3.1' `
4)  `implementation 'com.google.firebase:firebase-storage:19.1.1' `
5)  `implementation 'com.google.firebase:firebase-messaging:20.1.6' `


and also I used **OneSignal** for Push Notification. 

if you want OneSignal for your push notification You can use as follows;

**step 1:**

Create OneSignal Account and new app repository and config Firebase setting with your app on OneSignal app

... We will use user playerid for push notification. If you init user on OneSignal you can show your playerid in user details.

**step 2:**

init OneSignal on your current activity

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
               
**step 3:**

You can now send your fist push notification

        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'"+message+"'}, 'include_player_ids':     ['" +           playerId + "']}"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
                    

**Don't forget gradle config**

You need to write down firstly 
`apply plugin: 'com.onesignal.androidsdk.onesignal-gradle-plugin'`
on your gradle(app) then you can write down 
`apply plugin: 'com.google.gms.google-services'`

if you don't get like this your gradle won't work.








