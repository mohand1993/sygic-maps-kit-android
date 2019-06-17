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

package com.sygic.samples.base

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import com.sygic.samples.app.activities.CommonSampleActivity
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseRobot(private val activity: CommonSampleActivity, @IdRes private val parentViewId: Int) {

    init {
        onView(withId(parentViewId)).check(matches(isDisplayed()))
    }

    fun isViewDisplayed(@IdRes viewId: Int) {
        onView(allOf(withId(viewId), withParent(withId(parentViewId)))).check(matches(isDisplayed()))
    }

    fun isViewNotDisplayed(@IdRes viewId: Int) {
        onView(allOf(withId(viewId), withParent(withId(parentViewId)))).check(matches(not(isDisplayed())))
    }

    fun clickOnView(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(ViewActions.click())
    }

    fun isToastVisible() {
        onView(withId(android.R.id.message)).inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
    }
}