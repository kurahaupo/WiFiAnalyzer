/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.vrem.wifianalyzer.wifi.channelavailable

import android.os.Build
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry.Companion.find
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ChannelAvailableAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = INSTANCE.settings
    private val currentLocale = Locale.getDefault()
    private val wiFiChannelCountry = find(currentLocale.country)
    private val fixture = ChannelAvailableAdapter(mainActivity, listOf(wiFiChannelCountry))

    @Before
    fun setUp() {
        whenever(settings.languageLocale()).thenReturn(currentLocale)
    }

    @After
    fun tearDown() {
        verify(settings, atLeastOnce()).languageLocale()
        INSTANCE.restore()
    }

    @Test
    fun testGetView() {
        // setup
        val resources = mainActivity.resources
        val wiFiBand2 = resources.getString(WiFiBand.GHZ2.textResource)
        val wiFiBand5 = resources.getString(WiFiBand.band5GHz.textResource)
        val wiFiBand6 = resources.getString(WiFiBand.band6GHz.textResource)
        val expected = "${wiFiChannelCountry.countryCode()} - ${wiFiChannelCountry.countryName(currentLocale)}"
        val expectedGHZ2 = wiFiChannelCountry.channelsGHZ2().joinToString(",")
        val expected5GHz = wiFiChannelCountry.channels5GHz().joinToString(",")
        val expected6GHz = wiFiChannelCountry.channels6GHz().joinToString(",")
        val viewGroup = mainActivity.findViewById<ViewGroup>(android.R.id.content)
        // execute
        val actual = fixture.getView(0, null, viewGroup)
        // validate
        assertNotNull(actual)
        assertEquals(expected, actual.findViewById<TextView>(R.id.channel_available_country).text)
        assertEquals("$wiFiBand2 : ", actual.findViewById<TextView>(R.id.channel_available_title_ghz_2).text)
        assertEquals(expectedGHZ2, actual.findViewById<TextView>(R.id.channel_available_ghz_2).text)
        assertEquals("$wiFiBand5 : ", actual.findViewById<TextView>(R.id.channel_available_title5_g_hz).text)
        assertEquals(expected5GHz, actual.findViewById<TextView>(R.id.channel_available5_g_hz).text)
        assertEquals("$wiFiBand6 : ", actual.findViewById<TextView>(R.id.channel_available_title6_g_hz).text)
        assertEquals(expected6GHz, actual.findViewById<TextView>(R.id.channel_available6_g_hz).text)
    }

}
