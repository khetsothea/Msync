/*
 * Copyright 2017 Ralph Bergmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.the4thfloor.msync.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.android.gms.plus.PlusShare
import com.google.firebase.analytics.FirebaseAnalytics
import eu.the4thfloor.msync.BuildConfig
import eu.the4thfloor.msync.BuildConfig.PREF_ACTION_SHARE_FIREBASE
import eu.the4thfloor.msync.BuildConfig.PREF_ACTION_SHARE_GOOGLE_PLUS
import eu.the4thfloor.msync.BuildConfig.PREF_ACTION_SYNC_NOW
import eu.the4thfloor.msync.MSyncApp
import eu.the4thfloor.msync.R
import eu.the4thfloor.msync.utils.createSyncJobs
import javax.inject.Inject


class PrefActionsActivity : Activity() {

    @Inject lateinit var fa: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MSyncApp.graph.inject(this)

        intent?.dataString?.let {
            if (PREF_ACTION_SYNC_NOW.equals(it, ignoreCase = true)) {
                createSyncJobs(true)
            } else if (PREF_ACTION_SHARE_GOOGLE_PLUS.equals(it, ignoreCase = true)) {
                val shareIntent = PlusShare.Builder(this)
                    .setType("text/plain")
                    .setText(getString(R.string.google_plus_share_message))
                    .setContentUrl(Uri.parse(BuildConfig.FIREBASE_DEEPLINK))
                    .intent
                startActivity(shareIntent)
                fa.logEvent("share_on_google_plus", null)
            } else if (PREF_ACTION_SHARE_FIREBASE.equals(it, ignoreCase = true)) {
                createSyncJobs(true)
                val intent = AppInviteInvitation.IntentBuilder(getString(R.string.firebase_app_invite_title))
                    .setMessage(getString(R.string.firebase_app_invite_message))
                    .setDeepLink(Uri.parse(BuildConfig.FIREBASE_DEEPLINK))
                    .setEmailSubject(getString(R.string.firebase_app_invite_email_subject))
                    .setEmailHtmlContent(getString(R.string.firebase_app_invite_email_html))
                    .build()
                startActivityForResult(intent, 0)
                fa.logEvent("invite_friends", null)
            }
        }

        finish()
    }
}
