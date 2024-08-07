package com.toto.pokemonapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.toto.pokemonapp.app.mainActivityScreens.view.PokemonDetailScreen
import com.toto.pokemonapp.app.mainActivityScreens.view.PokemonListScreen
import com.toto.pokemonapp.app.mainActivityScreens.viewModel.PokemonDetailViewModel
import com.toto.pokemonapp.app.theme.PokemonAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonAppTheme {
                PokemonApp()
            }
        }
    }
}

@Composable
private fun PokemonApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "pokemon_list") {
        composable(route = "pokemon_list") {
            PokemonListScreen { id ->
                navController.navigate("pokemon_details/$id")
            }
        }
        composable(route = "pokemon_details/{id}",
            arguments = listOf(navArgument("id")
            {
                type = NavType.StringType
            }),
            //enterTransition = { slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) },
            //exitTransition = { slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) }
            ) {
            PokemonDetailScreen(navController)
        }

    }
}