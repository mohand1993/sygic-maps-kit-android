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

package com.sygic.samples.base.idling

import com.sygic.maps.module.common.MapFragmentWrapper
import com.sygic.samples.app.activities.CommonSampleActivity
import com.sygic.sdk.map.MapView
import com.sygic.sdk.map.`object`.MapMarker
import com.sygic.sdk.map.`object`.MapObject
import com.sygic.sdk.map.data.SimpleMapDataModel

class MapPinsIdlingResource(activity: CommonSampleActivity) : BaseIdlingResource(activity) {

    override fun getName(): String = "MapPinsIdlingResource"

    override fun isIdle(): Boolean {
        activity.supportFragmentManager.fragments.forEach { fragment ->
            if (fragment is MapFragmentWrapper<*>) return getMapObjects(fragment.mapDataModel)
                .filterIsInstance<MapMarker>().isNotEmpty()
        }

        return false
    }
}
//todo: MS-6338 remove with next version (v16) of SDK
private fun getMapObjects(model: MapView.MapDataModel): Set<MapObject<*>> {
    val m = SimpleMapDataModel::class.java.getDeclaredMethod("getMapObjects")
    m.isAccessible = true
    return m.invoke(model) as Set<MapObject<*>>
}