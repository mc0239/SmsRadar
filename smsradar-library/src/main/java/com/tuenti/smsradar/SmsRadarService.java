/*
 * Copyright (c) Tuenti Technologies S.L. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tuenti.smsradar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;

import java.util.Date;


/**
 * Service created to handle the SmsContentObserver registration. This service has the responsibility of register and
 * unregister the content observer in sms content provider when it's created and destroyed.
 * <p/>
 * The SmsContentObserver will be registered over the CONTENT_SMS_URI to be notified each time the system update the
 * sms content provider.
 *
 * @author Pedro Vcente Gómez Sánchez <pgomez@tuenti.com>
 * @author Manuel Peinado <mpeinado@tuenti.com>
 */
public class SmsRadarService extends Service {

	private static final int ONE_SECOND = 1000;

	private ContentResolver contentResolver;
	private SmsObserver smsObserver;
	private AlarmManager alarmManager;
	private boolean initialized;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!initialized) {
			initializeService();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        // finish service
        initialized = false;
        // unregister content observer
        contentResolver.unregisterContentObserver(smsObserver);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
        // restart service
        Intent intent = new Intent(this, SmsRadarService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        getAlarmManager().set(AlarmManager.RTC_WAKEUP, new Date().getTime() + ONE_SECOND, pendingIntent);
	}

	private void initializeService() {
		initialized = true;

        // initialize content resolver
        if(contentResolver == null) {
            this.contentResolver = getContentResolver();
        }

        // initialize SMS observer
        if(smsObserver == null) {
            Handler handler = new Handler();
            SmsStorage smsStorage = new SharedPreferencesSmsStorage(getBaseContext());
            SmsCursorParser smsCursorParser = new SmsCursorParser(smsStorage);
            this.smsObserver = new SmsObserver(contentResolver, handler, smsCursorParser);
        }

        // register content observer
        contentResolver.registerContentObserver(Telephony.Sms.CONTENT_URI, true, smsObserver);
	}

	private AlarmManager getAlarmManager() {
		return alarmManager != null ? alarmManager : (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}

	/*
	 * Test methods. This methods has been created to modify the service dependencies in test runtime because
	 * without dependency injection we can't provide this entities.
	 */

	void setSmsObserver(SmsObserver smsObserver) {
		this.smsObserver = smsObserver;
	}

	void setContentResolver(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	void setAlarmManager(AlarmManager alarmManager) {
		this.alarmManager = alarmManager;
	}
}
