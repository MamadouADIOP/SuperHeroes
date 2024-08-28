package com.example.superheroes

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.superheroes.model.Hero
import com.example.superheroes.model.HeroesRepository
import com.example.superheroes.ui.theme.SuperheroesTheme

@Composable
fun SuperHeroItem(hero: Hero, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(start = dimensionResource(id = R.dimen.item_padding), end = dimensionResource(id = R.dimen.item_padding)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.card_elevation))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.item_height))
                    .padding(dimensionResource(id = R.dimen.item_padding))
            ) {
                HeroInformation(hero.nameRes, hero.descriptionRes, Modifier.weight(1f))
                Spacer(Modifier.width(dimensionResource(id = R.dimen.item_padding)))
                HeroIcon(
                    imageRes = hero.imageRes,
                    Modifier.size(50.dp)
                )
            }
        }
    }
}

@Composable
fun HeroInformation(
    @StringRes nameRes: Int,
    @StringRes descriptionRes: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = stringResource(id = nameRes), style = MaterialTheme.typography.displaySmall)
        Text(
            text = stringResource(id = descriptionRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SuperHeroesList(heroes: List<Hero>, modifier: Modifier = Modifier) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    // Fade in entry animation for the entire list
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
        modifier = modifier
    ) {
    LazyColumn {
        itemsIndexed(heroes) { index, hero ->
            SuperHeroItem(
                hero = hero,
                Modifier
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_small),
                        start = dimensionResource(id = R.dimen.item_padding),
                        end = dimensionResource(id = R.dimen.item_padding)
                    )
                    .animateEnterExit(
                        enter = slideInVertically(
                            animationSpec = spring(
                                stiffness = StiffnessVeryLow,
                                dampingRatio = DampingRatioLowBouncy
                            ),
                            initialOffsetY = { it * (index + 1) } // staggered entrance
                        )
                    )
            )

        }}
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperHeroesTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(modifier = modifier, title = {

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        })
}

@Composable
fun HeroIcon(@DrawableRes imageRes: Int, modifier: Modifier = Modifier) {
    Box(modifier.size(dimensionResource(id = R.dimen.image_size))) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.clip(MaterialTheme.shapes.small)
        )
    }

}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HeroesScreenPreview() {
    SuperheroesTheme {
        SuperHeroesList(HeroesRepository.heroes)
    }
}


@Preview
@Composable
fun WoofDarkThemePreview() {
    SuperheroesTheme(darkTheme = true) {
        SuperHeroesList(HeroesRepository.heroes)
    }
}