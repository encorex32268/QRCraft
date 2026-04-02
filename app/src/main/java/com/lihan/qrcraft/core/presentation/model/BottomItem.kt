package com.lihan.qrcraft.core.presentation.model

import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.Route

data class BottomItem(
    val resId: Int,
    val route: Route,
    val name: String,
)

val bottomItems = listOf(
    BottomItem(
        resId = R.drawable.clock_refresh,
        route = Route.History,
        name = "History"
    ),
    BottomItem(
        resId = R.drawable.scan,
        route = Route.Scan,
        name = "Scan"
    ),
    BottomItem(
        resId = R.drawable.plus_circle,
        route = Route.Generate,
        name = "Generate"
    )
)