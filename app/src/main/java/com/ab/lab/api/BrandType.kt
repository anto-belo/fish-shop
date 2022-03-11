package com.ab.lab.api

enum class BrandType(val type: String, var viewName: String) {
    EVERYTHING("", ""),
    TETRA("tetraaqua", "Tetra"),
    SERA("sera", "Sera"),
    BEAPHAR("beaphar", "Beaphar"),
    BIOONTO("bioonto", "Bio-onto"),
    COPPENS("coppens", "Coppens"),
    PANTO("panto", "Panto"),
    TROPICAL("tropicalfish", "Tropical");

    override fun toString(): String {
        return viewName
    }
}