package com.example.rickandmortyapp.screens.episodeListScreen.headers

import com.example.rickandmortyapp.domain.models.Episode

sealed class EpisodeUiModel {

    class Item(val episode: Episode) : EpisodeUiModel()

    class Header(val text: String) : EpisodeUiModel()

}