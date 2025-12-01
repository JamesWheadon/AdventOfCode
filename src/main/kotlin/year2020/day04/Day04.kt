package year2020.day04

import readInput

/*
byr (Birth Year)
iyr (Issue Year)
eyr (Expiration Year)
hgt (Height)
hcl (Hair Color)
ecl (Eye Color)
pid (Passport ID)
cid (Country ID)
 */
fun part1(input: List<String>): Int {
    val fields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    return input.fold(mutableListOf(mutableListOf<String>())) { passports, s ->
        if (s == "") {
            passports.add(mutableListOf())
        } else {
            passports.last().add(s)
        }
        passports
    }
        .map { it.reduce { acc, s -> "$acc $s" } }
        .count { passport ->
            fields.all { passport.contains("$it:") }
        }
}

/*
byr (Birth Year) - four digits; at least 1920 and at most 2002.
iyr (Issue Year) - four digits; at least 2010 and at most 2020.
eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
hgt (Height) - a number followed by either cm or in:
If cm, the number must be at least 150 and at most 193.
If in, the number must be at least 59 and at most 76.
hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
pid (Passport ID) - a nine-digit number, including leading zeroes.
cid (Country ID) - ignored, missing or not.
 */
fun part2(input: List<String>): Int {
    val fieldsRegex = listOf(
        "^byr:([0-9]{4})$" to { s: String -> s.toInt() in 1920..2002 },
        "^iyr:([0-9]{4})$" to { s: String -> s.toInt() in 2010..2020 },
        "^eyr:([0-9]{4})$" to { s: String -> s.toInt() in 2020..2030 },
        "^hgt:([0-9]{3}cm|[0-9]{2}in)$" to { s: String ->
            if (s.contains("cm")) {
                s.filter { it.isDigit() }.toInt() in 150..193
            } else {
                s.filter { it.isDigit() }.toInt() in 59..76
            }
        },
        "^hcl:(#[0-9a-f]{6})$" to { _: String -> true },
        "^ecl:([a-z]{3})$" to { s: String -> listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(s) },
        "^pid:([0-9]{9})$" to { _: String -> true }
    )
    return input.fold(mutableListOf(mutableListOf<String>())) { passports, s ->
        if (s == "") {
            passports.add(mutableListOf())
        } else {
            passports.last().add(s)
        }
        passports
    }
        .map { it.reduce { acc, s -> "$acc $s " } }
        .count { passport ->
            val fields = passport.split(" ").filter { it.isNotEmpty() }
            fieldsRegex.all { pair ->
                fields.any {
                    pair.first.toRegex().find(it)?.groups?.get(1)?.let { detail -> pair.second(detail.value) } ?: false
                }
            }
        }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day04/Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 2)

    val input = readInput("src/main/kotlin/year2020/day04/Day04")
    println(part1(input))
    println(part2(input))
}
