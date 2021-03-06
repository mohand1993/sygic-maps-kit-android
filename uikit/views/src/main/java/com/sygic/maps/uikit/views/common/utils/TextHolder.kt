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

package com.sygic.maps.uikit.views.common.utils

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import com.sygic.maps.uikit.views.common.extensions.EMPTY_STRING
import com.sygic.maps.uikit.views.common.extensions.NO_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
open class TextHolder private constructor(
    @StringRes private var textResource: Int = NO_ID,
    private var textString: String = EMPTY_STRING
) : Parcelable {

    companion object {

        val empty = TextHolder()

        @JvmStatic
        fun from(@StringRes resId: Int): TextHolder {
            return TextHolder(textResource = resId)
        }

        @JvmStatic
        fun from(text: String): TextHolder {
            return TextHolder(textString = text)
        }

        @JvmStatic
        fun from(@StringRes resId: Int, text: String): TextHolder {
            return TextHolder(textResource = resId, textString = text)
        }

        @JvmStatic
        fun from(@StringRes resId: Int, number: Int): TextHolder {
            return TextHolder(textResource = resId, textString = number.toString())
        }
    }

    open fun getText(context: Context): String {
        if (textResource != NO_ID && textString.isNotEmpty()) {
            return String.format(context.getString(textResource), textString)
        }

        if (textResource != NO_ID) {
            return context.getString(textResource)
        }

        return textString
    }

    fun isEmpty() = textResource == NO_ID && textString == EMPTY_STRING
    fun isNotEmpty() = !isEmpty()
}