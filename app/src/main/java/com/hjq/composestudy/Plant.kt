package com.hjq.composestudy

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@Composable
fun PlantDescription(desc: String) {
    val htmlDesc = remember(desc) {
        HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView(factory = {
        TextView(it).apply {
            movementMethod = LinkMovementMethod.getInstance()
        }
    }, update = {
        it.text = htmlDesc
    })
}


@Composable
fun RallyNavHost(
    navHostController: NavHostController,
    modifier: Modifier
) {
//    NavHost(
//        navController = navHostController,
//        startDestination = Overview.name,
//        modifier = modifier
//    ) {

//    }

}