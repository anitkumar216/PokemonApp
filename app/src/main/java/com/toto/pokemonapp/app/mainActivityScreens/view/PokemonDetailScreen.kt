package com.toto.pokemonapp.app.mainActivityScreens.view

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.toto.pokemonapp.R
import com.toto.pokemonapp.app.common.AppBar
import com.toto.pokemonapp.app.mainActivityScreens.model.PokemonDetailsResponseModel
import com.toto.pokemonapp.app.mainActivityScreens.model.Stats
import com.toto.pokemonapp.app.mainActivityScreens.viewModel.PokemonDetailViewModel
import com.toto.pokemonapp.app.theme.MagentaDark
import com.toto.pokemonapp.app.theme.MagentaExtraLight
import com.toto.pokemonapp.app.theme.MagentaLight
import kotlin.math.min

@Preview
@Composable
fun PokemonDetailScreenPreview() {
    PokemonDetailScreen(null)
}

@Composable
fun PokemonDetailScreen(navController: NavHostController?) {
    val viewModel: PokemonDetailViewModel = viewModel()
    val pokemon = viewModel.pokemonState.value

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(topBar = {
        AppBar("Pokemon Details", Icons.Default.ArrowBack) {
            navController?.navigateUp()
        }
    }) { padding ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                ) {
                    PokemonInfo(pokemon)
                }
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                ) {
                    PokemonBaseStats(pokemon?.stats)
                }
            }
        } else {
            PokemonDetailContent(pokemon, padding)
        }
    }
}

@Composable
fun PokemonDetailContent(pokemon: PokemonDetailsResponseModel?, paddingValues: PaddingValues) {
    val scrollState = rememberLazyListState()
    val offset = min(
        a = 1f,
        b = 1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )
    val size by animateDpAsState(targetValue = max(70.dp, 140.dp * offset), label = "")

    Surface(
        modifier = Modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Surface(tonalElevation = 4.dp, color = MagentaLight) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            modifier = Modifier.padding(16.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = Color(0xFF3B003B)
                            ),
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(pokemon?.sprites?.backDefault ?: "")
                                    .transformations(CircleCropTransformation())
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.ic_launcher_background),
                                contentDescription = stringResource(R.string.app_name),
                                modifier = Modifier
                                    .size(size)
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = if (pokemon?.name != null)
                                    pokemon.name.replaceFirstChar { it.uppercase() }
                                else
                                    "Unknown Pokemon",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (pokemon?.baseExperience != null)
                                    "#${pokemon.baseExperience}"
                                else
                                    "#000",
                                fontSize = 16.sp,
                                color = Color.White,
                            )
                        }

                    }

                    Spacer(modifier = Modifier.size(4.dp))

                    if (pokemon?.types != null && pokemon.types.isNotEmpty()) {
                        LazyRow(contentPadding = PaddingValues(16.dp)) {
                            items(pokemon.types) { type ->
                                TypeBadge(type = type.type?.name!!.replaceFirstChar { it.uppercase() })
                            }
                        }
                    }
                }

            }

            Text(
                text = "Base Stats",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Magenta)
                    .padding(16.dp)

            )

            if (pokemon?.stats != null && pokemon.stats.isNotEmpty()) {
                LazyColumn(state = scrollState, modifier = Modifier.fillMaxWidth()) {
                    items(pokemon.stats) { stat ->
                        BaseStats(stats = stat, maxValue = 100)
                    }
                }
            }

        }
    }

}

@Composable
fun PokemonInfo(pokemon: PokemonDetailsResponseModel?) {
    Surface(tonalElevation = 4.dp, color = MagentaLight, modifier = Modifier.fillMaxSize()) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.wrapContentWidth()
            ) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color(0xFF3B003B)
                    ),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon?.sprites?.backDefault ?: "")
                            .transformations(CircleCropTransformation())
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier
                            .size(140.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = if (pokemon?.name != null)
                            pokemon.name.replaceFirstChar { it.uppercase() }
                        else
                            "Unknown Pokemon",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (pokemon?.baseExperience != null)
                            "#${pokemon.baseExperience}"
                        else
                            "#000",
                        fontSize = 16.sp,
                        color = Color.White,
                    )
                }

            }

            Spacer(modifier = Modifier.size(4.dp))

            if (pokemon?.types != null && pokemon.types.isNotEmpty()) {
                LazyRow(contentPadding = PaddingValues(16.dp)) {
                    items(pokemon.types) { type ->
                        TypeBadge(type = type.type?.name!!.replaceFirstChar { it.uppercase() })
                    }
                }
            }
        }

    }
}

@Composable
fun PokemonBaseStats(stats: List<Stats>?) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Base Stats",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Magenta)
                .padding(16.dp)

        )

        if (!stats.isNullOrEmpty()) {
            LazyColumn(modifier = Modifier.wrapContentWidth()) {
                items(stats) { stat ->
                    BaseStats(stats = stat, maxValue = 100)
                }
            }
        }
    }
}

@Composable
fun TypeBadge(type: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, bottom = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                modifier = Modifier
                    .background(MagentaDark)
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = type,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BaseStats(stats: Stats, maxValue: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MagentaExtraLight)
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val modifiedText = stats.stat?.name?.uppercase() ?: ""
                Text(
                    text = modifiedText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(text = stats.baseStat.toString(), color = Color.Black)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (stats.baseStat?.toFloat() ?: 0f) / maxValue)
                            .height(20.dp)
                            .background(Color.Magenta, shape = RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}
