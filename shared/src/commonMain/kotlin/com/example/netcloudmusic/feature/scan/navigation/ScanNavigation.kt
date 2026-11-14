package com.example.netcloudmusic.feature.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.netcloudmusic.feature.scan.ScanRoute
import com.example.netcloudmusic.ui.navigation.myComposable

const val SCAN_ROUTE = "scan"

fun NavController.navigateToScan() = navigate(SCAN_ROUTE)

fun NavGraphBuilder.scanScreen(
    toBack: () -> Unit,
    onScanResult: (String) -> Unit,
) {
    myComposable(SCAN_ROUTE) {
        ScanRoute(toBack = toBack, onScanResult = onScanResult)
    }
}
