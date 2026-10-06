package com.example.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.model.EventEntity
import com.example.data.model.MemoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ExportResolution(val label: String, val width: Int, val height: Int) {
    HD_1080P("1080p (Full HD)", 1080, 1920),
    QHD_2K("2K (1440p)", 1440, 2560),
    UHD_4K("4K (Ultra HD - 3840×2160)", 2160, 3840),
    STUDIO_8K("8K (Master Ultra - 7680×4320)", 4320, 7680)
}

object ExportManager {

    suspend fun exportEventCard(
        context: Context,
        event: EventEntity,
        resolution: ExportResolution
    ): Uri? = withContext(Dispatchers.IO) {
        val width = resolution.width
        val height = resolution.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Scale factors relative to 1080p baseline
        val scale = width / 1080f

        // Draw Warm Pastel Gradient Background
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                Color.parseColor("#FAF5EE"),
                Color.parseColor("#F5EBE1"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Top Brand Header
        val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D96B82")
            textSize = 42f * scale
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("✦ Mintice ✦", width / 2f, 180f * scale, brandPaint)

        val taglinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#7A6F6C")
            textSize = 24f * scale
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Plan it. Remember it. Live it. ♡", width / 2f, 230f * scale, taglinePaint)

        // Central Card Frame
        val cardMargin = 80f * scale
        val cardTop = 320f * scale
        val cardBottom = height - 260f * scale
        val cardRect = RectF(cardMargin, cardTop, width - cardMargin, cardBottom)

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            setShadowLayer(30f * scale, 0f, 15f * scale, Color.parseColor("#20000000"))
        }
        canvas.drawRoundRect(cardRect, 48f * scale, 48f * scale, cardPaint)

        // Category Banner inside Card
        val catPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FDECEF")
        }
        val catRect = RectF(cardMargin + 40f * scale, cardTop + 50f * scale, width - cardMargin - 40f * scale, cardTop + 140f * scale)
        canvas.drawRoundRect(catRect, 24f * scale, 24f * scale, catPaint)

        val catTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D96B82")
            textSize = 28f * scale
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("CATEGORY: ${event.category.uppercase()}", width / 2f, cardTop + 106f * scale, catTextPaint)

        // Title
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#2C2422")
            textSize = 58f * scale
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(event.title, width / 2f, cardTop + 260f * scale, titlePaint)

        // Date and Time
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val dateText = dateFormat.format(Date(event.startDateTime))
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val timeText = if (event.isAllDay) "All Day Occasion" else "${timeFormat.format(Date(event.startDateTime))} - ${timeFormat.format(Date(event.endDateTime))}"

        val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#5A504D")
            textSize = 34f * scale
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(dateText, width / 2f, cardTop + 360f * scale, datePaint)

        val timePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#7A6F6C")
            textSize = 30f * scale
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(timeText, width / 2f, cardTop + 420f * scale, timePaint)

        // Location
        if (event.locationName.isNotBlank()) {
            val locPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#D96B82")
                textSize = 32f * scale
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("📍 ${event.locationName}", width / 2f, cardTop + 510f * scale, locPaint)
        }

        // Notes if any
        if (event.description.isNotBlank()) {
            val notePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#5A504D")
                textSize = 28f * scale
                typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("\"${event.description}\"", width / 2f, cardTop + 620f * scale, notePaint)
        }

        // Washi tape accent on card top
        val tapePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E0FACFD8")
        }
        val tapeRect = RectF(width / 2f - 120f * scale, cardTop - 25f * scale, width / 2f + 120f * scale, cardTop + 35f * scale)
        canvas.drawRoundRect(tapeRect, 8f * scale, 8f * scale, tapePaint)

        // Footer Resolution watermark
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#A89E9B")
            textSize = 22f * scale
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Exported in ${resolution.label} • Mintice Organizer", width / 2f, height - 100f * scale, footerPaint)

        // Save to cache file and return Uri
        try {
            val imagesFolder = File(context.cacheDir, "exports").apply { mkdirs() }
            val file = File(imagesFolder, "mintice_event_${System.currentTimeMillis()}_${resolution.name}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
