/*
 * Copyright (c) 2019 Sygic a.s. All rights reserved.
 *
 * This project is licensed under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sygic.maps.uikit.viewmodels.common.sound

import androidx.annotation.RestrictTo
import androidx.lifecycle.MutableLiveData
import com.sygic.maps.uikit.viewmodels.common.navigation.NavigationManagerClient
import com.sygic.maps.uikit.viewmodels.common.voice.VoiceManagerClient
import com.sygic.maps.uikit.views.common.utils.SingletonHolder
import com.sygic.sdk.navigation.NavigationManager

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class SoundManagerImpl private constructor(
    private val voiceManagerClient: VoiceManagerClient,
    private val navigationManagerClient: NavigationManagerClient
) : SoundManager {

    companion object : SingletonHolder<SoundManagerImpl>() {
        @JvmStatic
        fun getInstance(voiceManager: VoiceManagerClient, navManager: NavigationManagerClient) =
            getInstance { SoundManagerImpl(voiceManager, navManager) }
    }

    override var soundsEnabled = MutableLiveData<Boolean>(false)

    private fun setNavigationAudioWarningsEnabled(enabled: Boolean) {
        navigationManagerClient.setAudioBetterRouteListener(if (enabled) NavigationManager.AudioBetterRouteListener { false } else null)
        navigationManagerClient.setAudioIncidentListener(if (enabled) NavigationManager.AudioIncidentListener { false } else null)
        navigationManagerClient.setAudioRailwayCrossingListener(if (enabled) NavigationManager.AudioRailwayCrossingListener { false } else null)
        navigationManagerClient.setAudioSharpCurveListener(if (enabled) NavigationManager.AudioSharpCurveListener { false } else null)
        navigationManagerClient.setAudioSpeedLimitListener(if (enabled) NavigationManager.AudioSpeedLimitListener { false } else null)
        navigationManagerClient.setAudioTrafficListener(if (enabled) NavigationManager.AudioTrafficListener { false } else null)
    }

    private fun setNavigationAudioInstructionsEnabled(enabled: Boolean) {
        navigationManagerClient.setAudioInstructionListener(if (enabled) NavigationManager.AudioInstructionListener { false } else null)
    }

    override fun setDefaultVoice() = voiceManagerClient.setDefaultVoice()

    init {
        soundsEnabled.observeForever { enabled ->
            setNavigationAudioWarningsEnabled(enabled)
            setNavigationAudioInstructionsEnabled(enabled)
        }
    }
}