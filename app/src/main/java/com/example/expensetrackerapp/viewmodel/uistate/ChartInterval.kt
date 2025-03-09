package com.example.expensetrackerapp.viewmodel.uistate

import com.example.expensetrackerapp.data.Transaction

sealed class ChartInterval {
    data object OneDay : ChartInterval()
    data object FiveDay : ChartInterval()
    data object OneMonth : ChartInterval()
    data object ThreeMonth : ChartInterval()
    data object SixMonth : ChartInterval()
    data object OneYear : ChartInterval()
}