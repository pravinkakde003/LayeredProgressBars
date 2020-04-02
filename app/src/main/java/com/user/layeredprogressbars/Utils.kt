package com.user.layeredprogressbars

import android.content.res.Resources

internal object Utils {
    /**
     * Convert the specified dimen value to pixels, used for fonts, not views.
     *
     * @param resources An instance of Android [Resources]
     * @param sp        A dimen value specified in sp
     * @return A float value corresponding to the specified sp converted to pixels
     */
    @JvmStatic
    fun sp2px(resources: Resources, sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }
}