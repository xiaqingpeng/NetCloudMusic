package com.example.netcloudmusic.feature.githubprofile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ForkLeft
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.component.MyCenterTopAppBar
import com.example.netcloudmusic.core.design.component.MyErrorView
import com.example.netcloudmusic.core.design.component.MyLoading
import com.example.netcloudmusic.core.di.AppContainerProvider
import com.example.netcloudmusic.core.util.rememberExternalUrlOpener

@Composable
fun GithubProfileRoute(toBack: () -> Unit) {
    val viewModel: GithubProfileViewModel = viewModel(
        factory = viewModelFactory {
            initializer { GithubProfileViewModel(AppContainerProvider.instance.githubRepository) }
        },
    )
    val state by viewModel.uiState.collectAsState()
    val openUrl = rememberExternalUrlOpener()

    Scaffold(
        topBar = { MyCenterTopAppBar(title = "GitHub 主页", toBack = toBack) },
    ) { padding ->
        when (val value = state) {
            GithubProfileUiState.Loading -> MyLoading(Modifier.padding(padding))
            is GithubProfileUiState.Error -> MyErrorView(
                message = value.message,
                onRetryClick = viewModel::loadData,
                modifier = Modifier.padding(padding),
            )
            is GithubProfileUiState.Success -> GithubProfileContent(
                value,
                openUrl,
                Modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun GithubProfileContent(
    state: GithubProfileUiState.Success,
    openUrl: (String) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(modifier.fillMaxSize()) {
        item {
            Column(
                Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AsyncImage(
                    model = state.user.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(CircleShape),
                )
                Text(state.user.name ?: state.user.login, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("@${state.user.login}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                state.user.bio?.takeIf(String::isNotBlank)?.let { Text(it) }
                state.user.location?.takeIf(String::isNotBlank)?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(it, style = MaterialTheme.typography.bodySmall)
                    }
                }
                state.user.blog?.takeIf(String::isNotBlank)?.let { blog ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            openUrl(if (blog.startsWith("http")) blog else "https://$blog")
                        },
                    ) {
                        Icon(Icons.Default.Link, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(4.dp))
                        Text(blog, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            HorizontalDivider()
            Row(
                Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                GithubStat(state.user.followers, "Followers")
                GithubStat(state.user.following, "Following")
                GithubStat(state.user.publicRepos, "Repos")
            }
            HorizontalDivider()
            if (state.repos.isNotEmpty()) {
                Text("公开仓库", Modifier.padding(horizontal = 16.dp, vertical = 12.dp), fontWeight = FontWeight.SemiBold)
            }
        }
        items(state.repos, key = { it.id }) { repo ->
            Column(
                Modifier.fillMaxWidth().clickable { repo.htmlUrl?.let(openUrl) }.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (repo.fork) Icons.Default.ForkLeft else Icons.Default.Book, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(repo.name, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                }
                repo.description?.takeIf(String::isNotBlank)?.let {
                    Text(it, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    repo.language?.let { Text(it, style = MaterialTheme.typography.labelSmall) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, Modifier.size(12.dp))
                        Text(repo.stars.toString(), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun GithubStat(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (value >= 1000) "${value / 1000}k" else value.toString(), fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
