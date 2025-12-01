package year2020.day21

import check
import readInput

fun part1(input: List<String>): Int {
    val foods = input.map { s ->
        s.split(" (contains ").let { Food(it[0].split(" "), it[1].removeSuffix(")").split(", ")) }
    }
    val allergens = foods.flatMap { it.allergens }.distinct()
    val allergenIngredients = allergens.flatMap { allergen ->
        val allergenFoods = foods.filter { it.allergens.contains(allergen) }
        allergenFoods.flatMap { it.ingredients }.distinct().filter { ingredient ->
            allergenFoods.all { it.ingredients.contains(ingredient) }
        }
    }
    return foods.sumOf { food -> food.ingredients.count { !allergenIngredients.contains(it) } }
}

fun part2(input: List<String>): String {
    val foods = input.map { s ->
        s.split(" (contains ").let { Food(it[0].split(" "), it[1].removeSuffix(")").split(", ")) }
    }
    val allergens = foods.flatMap { it.allergens }.distinct()
    val allergenIngredients = allergens.map { allergen ->
        val allergenFoods = foods.filter { it.allergens.contains(allergen) }
        Allergen(allergen, allergenFoods.flatMap { it.ingredients }.distinct().filter { ingredient ->
            allergenFoods.all { it.ingredients.contains(ingredient) }
        }.toMutableList())
    }
    while (allergenIngredients.any { it.possibleIngredients.size > 1 }) {
        val known = allergenIngredients.filter { it.possibleIngredients.size == 1 }
        allergenIngredients.filter { it.possibleIngredients.size != 1 }.forEach { allergen ->
            allergen.possibleIngredients.removeAll(known.flatMap { it.possibleIngredients })
        }
    }
    return allergenIngredients.sortedBy { it.name }.joinToString(",") { it.possibleIngredients.first() }
}

data class Food(val ingredients: List<String>, val allergens: List<String>)
data class Allergen(val name: String, val possibleIngredients: MutableList<String>)

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day21/Day21_test")
    check(part1(testInput), 5)
    check(part2(testInput), "mxmxvkd,sqjhc,fvjkl")

    val input = readInput("src/main/kotlin/year2020/day21/Day21")
    println(part1(input))
    println(part2(input))
}
