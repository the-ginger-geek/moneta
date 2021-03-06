package za.co.gingergeek.moneta.helpers

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {

    fun hideKeyboard(activity: Activity?) {
        activity?.let {
            var view = activity.currentFocus
            if (view == null) view = View(activity)
            val imm = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}