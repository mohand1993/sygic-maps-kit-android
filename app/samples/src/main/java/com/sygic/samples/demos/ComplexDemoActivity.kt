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

package com.sygic.samples.demos

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sygic.maps.module.browsemap.BROWSE_MAP_FRAGMENT_TAG
import com.sygic.maps.module.browsemap.BrowseMapFragment
import com.sygic.maps.uikit.views.poidetail.component.PoiDetailComponent
import com.sygic.samples.R
import com.sygic.samples.app.activities.CommonSampleActivity
import com.sygic.samples.demos.states.BrowseMapDemoDefaultState
import com.sygic.samples.demos.viewmodels.ComplexDemoActivityViewModel

class ComplexDemoActivity : CommonSampleActivity() {

    override val wikiModulePath: String? = null

    private lateinit var browseMapFragment: BrowseMapFragment
    private lateinit var viewModel: ComplexDemoActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_complex_demo)

        viewModel = ViewModelProviders.of(this).get(ComplexDemoActivityViewModel::class.java).apply {
            this.showPoiDetailObservable.observe(
                this@ComplexDemoActivity,
                Observer<PoiDetailComponent> { browseMapFragment.showPoiDetail(it) })
        }

        browseMapFragment = if (savedInstanceState == null) {
            placeBrowseMapFragment().apply { BrowseMapDemoDefaultState.setTo(this) }
        } else {
            supportFragmentManager.findFragmentByTag(BROWSE_MAP_FRAGMENT_TAG) as BrowseMapFragment
        }

        browseMapFragment.setSearchConnectionProvider(viewModel.searchModuleConnectionProvider)
        browseMapFragment.setNavigationConnectionProvider(viewModel.navigationModuleConnectionProvider)
    }

    // Note: You can also create this Fragment just like in other examples directly in an XML layout file, but
    // performance or other issues may occur (https://stackoverflow.com/a/14810676/3796931).
    private fun placeBrowseMapFragment() =
        BrowseMapFragment().also {
            supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentContainer, it, BROWSE_MAP_FRAGMENT_TAG)
                ?.runOnCommit {
                    viewModel.mapDataModel = it.mapDataModel
                    viewModel.cameraDataModel = it.cameraDataModel
                }
                ?.commit()
        }
}