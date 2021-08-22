package com.akerimtay.rickandmorty

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.akerimtay.rickandmorty.character.CharacterDetailParameters
import com.akerimtay.rickandmorty.character.CharacterDetailScreen
import com.akerimtay.rickandmorty.character.CharactersScreen
import com.akerimtay.rickandmorty.common.lifecycleIsResumed
import com.akerimtay.rickandmorty.episode.EpisodesScreen
import com.akerimtay.rickandmorty.location.LocationsScreen
import com.akerimtay.rickandmorty.main.MainNavigationItem
import com.akerimtay.rickandmorty.settings.SettingsScreen

object Routes {
    const val MAIN = "main"
    const val MAIN_CHARACTER = "main/character"
    const val CHARACTER_DETAIL = "character_detail"
    const val MAIN_LOCATION = "main/locations"
    const val LOCATION_DETAIL = "location_detail"
    const val MAIN_EPISODE = "main/episodes"
    const val EPISODE_DETAIL = "episode_detail"
    const val MAIN_SETTINGS = "main/settings"
    const val SEARCH = "search"
}

@Composable
fun JetMoviesNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Routes.MAIN,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation(
            route = Routes.MAIN,
            startDestination = MainNavigationItem.Characters.route
        ) {
            composable(MainNavigationItem.Characters.route) { from ->
                CharactersScreen(
                    onCharacterClick = { characterId ->
                        openCharacterDetail(
                            backStackEntry = from,
                            navController = navController,
                            characterId = characterId
                        )
                    }
                )
            }
            composable(MainNavigationItem.Locations.route) {
                LocationsScreen()
            }
            composable(MainNavigationItem.Episodes.route) {
                EpisodesScreen()
            }
            composable(MainNavigationItem.Settings.route) {
                SettingsScreen()
            }
        }
        composable(
            route = "${Routes.CHARACTER_DETAIL}/{${CharacterDetailParameters.CHARACTER_ID_KEY}}",
            arguments = listOf(navArgument(CharacterDetailParameters.CHARACTER_ID_KEY) { type = NavType.LongType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val characterId = arguments.getLong(CharacterDetailParameters.CHARACTER_ID_KEY)
            CharacterDetailScreen(
                characterId = characterId,
                upPress = { navController.navigateUp() }
            )
        }
    }
}

private fun openCharacterDetail(
    backStackEntry: NavBackStackEntry,
    navController: NavHostController,
    characterId: Long
) {
    if (backStackEntry.lifecycleIsResumed()) {
        navController.navigate("${Routes.CHARACTER_DETAIL}/$characterId")
    }
}