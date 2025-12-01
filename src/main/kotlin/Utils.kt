import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("$name.txt").readLines()

fun <T> check(actual: T, expected: T) {
    if (actual != expected) {
        throw FailedCheck("value was $actual")
    }
}

data class FailedCheck(override val message: String) : Exception()
