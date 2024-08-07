package com.toto.pokemonapp.app.mainActivityScreens.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toto.pokemonapp.AppUtils.extractIdFromUrl
import com.toto.pokemonapp.app.common.AppBar
import com.toto.pokemonapp.app.mainActivityScreens.model.ResultsResponseModel
import com.toto.pokemonapp.app.mainActivityScreens.viewModel.PokemonListViewModel
import com.toto.pokemonapp.app.theme.MagentaExtraLight
import com.toto.pokemonapp.app.theme.PokemonAppTheme
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun PokemonListScreen(navigationCallback: (String) -> Unit) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        AppBar("Pokemon List", Icons.Default.Home) {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }) { padding ->
        PokemonListContent(listState, padding) {
            navigationCallback.invoke(it)
        }
    }
}

@Composable
fun PokemonListContent(
    listState: LazyListState,
    paddingValues: PaddingValues,
    navigationCallback: (String) -> Unit
) {
    val viewModel: PokemonListViewModel = viewModel()
    val pokemonList = viewModel.pokemonListState.value
    val isFetching by viewModel.isFetching

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        LazyColumn(state = listState, contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
            items(pokemonList) { pokemon ->
                PokemonCard(result = pokemon, navigationCallback = navigationCallback)
            }

            item {
                if (viewModel.nextUrl != null) {
                    LaunchedEffect(Unit) {
                        viewModel.fetchPokemonList()
                    }
                }
            }

            if (isFetching) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonCard(result: ResultsResponseModel, navigationCallback: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable {
                val id = extractIdFromUrl(result.url)
                navigationCallback.invoke(id!!)
            }
    ) {
        Column(
            modifier = Modifier
                .background(MagentaExtraLight)
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = result.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Expand row icon",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MealCategoriesScreenPreview() {
    PokemonAppTheme {
        PokemonListScreen { }
    }
}