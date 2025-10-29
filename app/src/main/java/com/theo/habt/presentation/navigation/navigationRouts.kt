package com.theo.habt.presentation.navigation

import com.theo.habt.presentation.navigation.NavigationRouts.*
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRouts {

    @Serializable
    data object Home  : NavigationRouts

    @Serializable
    data object Analytic : NavigationRouts

    @Serializable
    data object AddHabit : NavigationRouts


}

