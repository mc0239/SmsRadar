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

import android.provider.Telephony;
import android.util.Log;

/**
 * Represents the SmsType.
 * <p/>
 * RECEIVED SmsType is the equivalent to MT in a telco terminology.
 * SENT SmsType is the equivalent to MO in a telco terminology.
 * <p/>
 * Review GSM short message service to get more information: http://en.wikipedia.org/wiki/Short_Message_Service
 *
 * @author Pedro Vcente Gómez Sánchez <pgomez@tuenti.com>
 * @author Manuel Peinado <mpeinado@tuenti.com>
 */
public enum SmsType {

	UNKNOWN (-1),
	RECEIVED (Telephony.Sms.MESSAGE_TYPE_INBOX),
	DRAFT (Telephony.Sms.MESSAGE_TYPE_DRAFT),
	FAILED (Telephony.Sms.MESSAGE_TYPE_FAILED),
	OUTBOX (Telephony.Sms.MESSAGE_TYPE_OUTBOX),
	QUEUED (Telephony.Sms.MESSAGE_TYPE_QUEUED),
	SENT (Telephony.Sms.MESSAGE_TYPE_SENT);

	private final int value;

	SmsType(int value) {
		this.value = value;
	}

	/**
	 * Create a new SmsType using the sms type value represented with integers in the Sms content provider.
	 *
	 * @param value used to translate into SmsType
	 * @return new SmsType associated to the value passed as parameter
	 */
	public static SmsType fromValue(int value) {
		for (SmsType smsType : values()) {
			if (smsType.value == value) {
				return smsType;
			}
		}
		//throw new IllegalArgumentException("Invalid sms type: " + value);
		Log.e(SmsType.class.getSimpleName(), "Invalid sms type: " + value);
		return UNKNOWN;
	}

    public int getValue() {
        return value;
    }
}
