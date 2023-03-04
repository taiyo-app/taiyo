package com.taiyoapp.taiyo.anime.presentation.util

fun formatVideoKind(kind: String)= when (kind) {
    "pv" -> "Трейлеры"
    "character_trailer" -> "Персонажи"
    "episode_preview" -> "Превью"
    "op" -> "Опенинги"
    "ed" -> "Эндинги"
    "clip" -> "Клипы"
    "op_ed_clip" -> "Муз. клипы"
    else -> "Другое"
}