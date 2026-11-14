package com.example.netcloudmusic.feature.code

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.util.ResourceUtil
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.default_avatar
import netcloudmusic.shared.generated.resources.hint_scan_my_code
import netcloudmusic.shared.generated.resources.my_profile
import netcloudmusic.shared.generated.resources.unknown_error
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CodeRoute(toBack: () -> Unit) {
    val viewModel: CodeViewModel = viewModel(
        factory = viewModelFactory {
            initializer { CodeViewModel(AppContainerProvider.instance.userRepository) }
        },
    )
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                toBack = toBack,
                title = stringResource(Res.string.my_profile),
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {

        when (val data = user) {
            null -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            else -> {
                val userId = data.id
                if (userId.isNullOrBlank()) {
                    Text(
                        text = stringResource(Res.string.unknown_error),
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    val qrValue = "https://www.ixuea.com?u=$userId"
                    Column(
                        modifier = Modifier.fillMaxSize().padding(top = 72.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Text(
                            text = stringResource(Res.string.my_profile),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 40.dp),
                        ) {
                            AsyncImage(
                                model = data.icon?.let(ResourceUtil::r),
                                contentDescription = null,
                                placeholder = painterResource(Res.drawable.default_avatar),
                                error = painterResource(Res.drawable.default_avatar),
                                modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.small),
                            )
                            Text(
                                text = data.nickname ?: "未知用户",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 12.dp),
                            )
                        }
                        androidx.compose.foundation.Image(
                            painter = rememberQrCodePainter(qrValue),
                            contentDescription = "个人二维码",
                            modifier = Modifier.padding(top = 36.dp).size(265.dp),
                        )
                        Text(
                            text = stringResource(Res.string.hint_scan_my_code),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(top = 28.dp),
                        )
                    }
                }
            }
        }
    }
}}
