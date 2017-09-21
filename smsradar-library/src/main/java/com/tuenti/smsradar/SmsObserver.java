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


import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;


/**
 * ContentObserver created to handle the sms content provider changes. This entity will be called each time the
 * system changes the sms content provider state.
 * <p/>
 * SmsObserver analyzes the change and studies if the protocol used is null or not to identify if the sms is incoming
 * or outgoing.
 * <p/>
 * SmsObserver will analyze the sms inbox and sent content providers to get the sms information and will notify
 * SmsListener.
 * <p/>
 * The content observer will be called each time the sms content provider be updated. This means that all
 * the sms state changes will be notified. For example, when the sms state change from SENDING to SENT state.
 *
 * @author Pedro Vcente Gómez Sánchez <pgomez@tuenti.com>
 * @author Manuel Peinado <mpeinado@tuenti.com>
 */
class SmsObserver extends ContentObserver {

	private ContentResolver contentResolver;
	private SmsCursorParser smsCursorParser;

	SmsObserver(ContentResolver contentResolver, Handler handler, SmsCursorParser smsCursorParser) {
		super(handler);
		this.contentResolver = contentResolver;
		this.smsCursorParser = smsCursorParser;
	}

	@Override
	public boolean deliverSelfNotifications() {
		return true;
	}

	@Override
	public void onChange(boolean selfChange, Uri uri) {
		super.onChange(selfChange, uri);

		Cursor cursor = null;
		try {
			cursor = getSmsContentObserverCursor();
			if (cursor != null && cursor.moveToFirst()) {
                Sms sms = smsCursorParser.parse(cursor);
                notifySmsListener(sms);
			}
		} finally {
			if(cursor != null) cursor.close();
		}
	}

	private void notifySmsListener(Sms sms) {
		if (sms != null && SmsRadar.smsListener != null) {
            Log.v(this.getClass().getSimpleName(), "Notify about SMS: " + sms.toString());
            switch (sms.getType()) {
                case SENT:
                    SmsRadar.smsListener.onSmsSent(sms);
                    break;
                case RECEIVED:
                    SmsRadar.smsListener.onSmsReceived(sms);
                    break;
                case FAILED:
                    SmsRadar.smsListener.onSmsFailed(sms);
                    break;
                default:
                	Log.v(this.getClass().getSimpleName(), "Recieved SMS of some other type: " + sms.toString());
                    break;
            }
		}
	}

	private Cursor getSmsContentObserverCursor() {
		return contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER);
	}

}