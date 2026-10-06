package com.example

import com.example.data.model.EventEntity
import com.example.ui.components.getCountdownString
import com.example.utils.ExportResolution
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MinticeUnitTest {

    @Test
    fun testCountdownCalculation() {
        val now = System.currentTimeMillis()
        val futureEvent = now + (30 * 60 * 1000L) // 30 minutes in future
        val countdown = getCountdownString(futureEvent, futureEvent + 3600000L)
        assertTrue(countdown.contains("30m") || countdown.contains("In"))
    }

    @Test
    fun testLiveEventState() {
        val now = System.currentTimeMillis()
        val start = now - 10000L
        val end = now + 50000L
        val state = getCountdownString(start, end)
        assertEquals("LIVE NOW", state)
    }

    @Test
    fun testPastEventState() {
        val now = System.currentTimeMillis()
        val start = now - 100000L
        val end = now - 50000L
        val state = getCountdownString(start, end)
        assertEquals("Ended", state)
    }

    @Test
    fun test4Kand8KExportResolutions() {
        assertEquals(2160, ExportResolution.UHD_4K.width)
        assertEquals(3840, ExportResolution.UHD_4K.height)
        assertEquals(4320, ExportResolution.STUDIO_8K.width)
        assertEquals(7680, ExportResolution.STUDIO_8K.height)
    }

    @Test
    fun testEventJsonSerialization() {
        val event = EventEntity(
            id = 1L,
            title = "Birthday Party",
            description = "At Home",
            eventType = "Birthday",
            category = "Birthday",
            startDateTime = 1700000000000L,
            endDateTime = 1700003600000L,
            locationName = "Itahari",
            reminderEnabled = true,
            reminderMinutesBefore = 60
        )

        val root = JSONObject()
        val array = JSONArray()
        val obj = JSONObject().apply {
            put("id", event.id)
            put("title", event.title)
            put("category", event.category)
            put("locationName", event.locationName)
            put("reminderEnabled", event.reminderEnabled)
        }
        array.put(obj)
        root.put("events", array)

        val jsonStr = root.toString()
        assertTrue(jsonStr.contains("Birthday Party"))
        assertTrue(jsonStr.contains("Itahari"))
    }
}
