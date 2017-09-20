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


import android.database.Cursor;
import android.provider.Telephony;
import android.support.annotation.NonNull;


/**
 * Works as cursor parser to get sms info from a cursor obtained from sms inbox/sent content provider.
 * <p/>
 * This entity will be called from SmsObserver with a cursor created over sms inbox or sms sent content provider to
 * extract the sms information and return an Sms object with the most important info we can get from sms content
 * provider.
 * <p/>
 * This entity can't be stateless because the SmsObserver it's called more than one time when the sms
 * content provider receive a incoming or outgoing sms. SmsCursorParser keep a reference of the last sms id parsed
 * and use it to parse only the correct incoming or outgoing sms. This implementation is based on a
 * lastSmsIdProcessed var that is updated each time an sms it's parsed.
 *
 * @author Pedro Vcente Gómez Sánchez <pgomez@tuenti.com>
 * @author Manuel Peinado <mpeinado@tuenti.com>
 */
class SmsCursorParser {

	private SmsStorage smsStorage;

	SmsCursorParser(SmsStorage smsStorage) {
		this.smsStorage = smsStorage;
	}

	Sms parse(@NonNull Cursor cursor) {

        if(cursor.getCount() <= 0) return null;

		Sms smsParsed = extractSmsInfoFromCursor(cursor);

        Sms received = new Sms(smsParsed.getSmsId(), null, smsParsed.getDate(), null, null);
        Sms stored = smsStorage.getLastSmsIntercepted();

        if(!received.equals(stored)) {
            smsStorage.updateLastSmsIntercepted(smsParsed.getSmsId(), smsParsed.getDate());
        } else {
            smsParsed = null;
        }

        return smsParsed;
	}

	private Sms extractSmsInfoFromCursor(Cursor cursor) {
		int smsId = cursor.getInt(cursor.getColumnIndex(Telephony.Sms._ID));
		String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
		String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));
		String msg = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
		int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));

		return new Sms(smsId, address, date, msg, SmsType.fromValue(type));
	}

}
