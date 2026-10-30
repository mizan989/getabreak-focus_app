package com.getabreak.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * A day of time on one website, as DetoxDroid counted it while the browser showed it. Kept apart
 * from the app tables: the time is part of a browser's screen time, not time on top of it.
 * @see com.getabreak.app.features.UsageStatsTracker.websiteTimeRecorder
 */
@Entity(
    tableName = "daily_website_usage",
    indices = [Index(value = ["date"])]
)
data class DailyWebsiteUsage(
    @PrimaryKey
    @ColumnInfo(name = "row_id")
    val rowId: String,
    val date: LocalDate,
    /** The site, e.g. "youtube.com", see [com.getabreak.app.websites.Websites.siteOf]. */
    val site: String,
    val timeMs: Long,
    val visitCount: Int
) {
    companion object {
        fun createRowId(date: LocalDate, site: String): String = "${date}_$site"
    }
}
