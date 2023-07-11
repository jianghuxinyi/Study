package com.example.jetnews.glance

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.jetnews.glance.ui.JetnewsGlanceAppWidget

class JetnewsGlanceAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = JetnewsGlanceAppWidget()
}