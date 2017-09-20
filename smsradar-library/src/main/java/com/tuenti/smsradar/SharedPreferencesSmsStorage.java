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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SmsStorage implementation based on shared preferences.
 *
 * @author Pedro Vicente Gómez Sánchez <pgomez@tuenti.com>
 * @author Manuel Peinado <mpeinado@tuenti.com>
 */
class SharedPreferencesSmsStorage implements SmsStorage {

    private static final String
            PREFRENCES_ID = "smsradar_preferences",
	        LAST_SMS_PARSED_ID = "last_sms_parsed_id",
	        LAST_SMS_PARSED_TIME = "last_sms_parsed_time";

	private static final int
            DEFAULT_SMS_PARSED_VALUE = -1;

	private SharedPreferences preferences;

	SharedPreferencesSmsStorage(Context context) {
		this.preferences = context.getSharedPreferences(PREFRENCES_ID, Context.MODE_PRIVATE);
	}

	@Override
	public void updateLastSmsIntercepted(int smsId, String date) {
        Editor editor = preferences.edit();
		editor.putInt(LAST_SMS_PARSED_ID, smsId);
		editor.putString(LAST_SMS_PARSED_TIME, date);
		editor.apply();
	}

	@Override
	public Sms getLastSmsIntercepted() {
		return new Sms(preferences.getInt(LAST_SMS_PARSED_ID, DEFAULT_SMS_PARSED_VALUE), null, preferences.getString(LAST_SMS_PARSED_TIME, null), null, null);
	}

    @Override
    public void clearLastSmsIntercepted() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAST_SMS_PARSED_ID, DEFAULT_SMS_PARSED_VALUE);
        editor.putString(LAST_SMS_PARSED_TIME, null);
        editor.apply();
    }
}
