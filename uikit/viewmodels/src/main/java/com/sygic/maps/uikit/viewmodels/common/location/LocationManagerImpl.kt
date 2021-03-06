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

package com.sygic.maps.uikit.viewmodels.common.location

import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sygic.maps.uikit.viewmodels.common.location.livedata.LocationProviderCheckLiveEvent
import com.sygic.maps.uikit.viewmodels.common.location.livedata.LocationRequestLiveEvent
import com.sygic.maps.uikit.viewmodels.common.position.PositionManagerClient

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class LocationManagerImpl(positionManagerClient: PositionManagerClient) : LocationManager {

    private var wasNoGPSDialogAlreadyShown: Boolean = false

    private val providerCheck: LocationProviderCheckLiveEvent by lazy { LocationProviderCheckLiveEvent() }
    private val locationRequest: LocationRequestLiveEvent by lazy { LocationRequestLiveEvent() }

    override val positionOnMapEnabled = object : MutableLiveData<Boolean>(false) {

        override fun setValue(value: Boolean) {
            if (value != this.value) {
                super.setValue(value)
                positionManagerClient.sdkPositionUpdatingEnabled.value = value
            }
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<LocationManager.LocationRequesterCallback>) {
        providerCheck.observe(owner)
        locationRequest.observe(owner, observer)
    }

    /**
     * Returns the current enabled/disabled status of the GPS provider.
     *
     * If the user has enabled this provider in the Settings menu, true
     * is returned to the {@link androidx.lifecycle.Observer}, false otherwise
     *
     * @return current GPS status
     */
    override fun checkGpsEnabled(observer: Observer<Boolean>) {
        providerCheck.checkEnabled(android.location.LocationManager.GPS_PROVIDER, observer)
    }

    override fun requestToEnableGps(onSuccess: () -> Unit, onDenied: () -> Unit) {
        checkGpsEnabled(Observer { gpsEnabled ->
            if (!gpsEnabled) {
                requestToEnableGpsInternal(object : LocationManager.EnableGpsCallback {
                    override fun onResult(@EnableGpsResult result: Int) {
                        when (result) {
                            EnableGpsResult.ENABLED -> onSuccess()
                            EnableGpsResult.DENIED -> onDenied()
                        }
                    }
                })
            } else {
                onSuccess()
            }
        })
    }

    private fun requestToEnableGpsInternal(enableGpsCallback: LocationManager.EnableGpsCallback) {
        if (!wasNoGPSDialogAlreadyShown) {
            locationRequest.value = object : LocationManager.LocationRequesterCallback {
                override fun onActivityResult(requestCode: Int, resultCode: Int) {
                    when (requestCode) {
                        GOOGLE_API_CLIENT_REQUEST_CODE, SETTING_ACTIVITY_REQUEST_CODE -> {
                            checkGpsEnabled(Observer { gpsEnabled ->
                                enableGpsCallback.onResult(if (gpsEnabled) EnableGpsResult.ENABLED else EnableGpsResult.DENIED)
                            })
                        }
                    }
                }
            }
            wasNoGPSDialogAlreadyShown = true
        } else {
            enableGpsCallback.onResult(EnableGpsResult.DENIED)
        }
    }
}
