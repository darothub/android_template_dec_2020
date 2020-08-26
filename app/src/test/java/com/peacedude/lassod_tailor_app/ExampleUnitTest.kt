package com.peacedude.lassod_tailor_app

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase.Companion.test
import io.kotest.matchers.shouldBe
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

}
class MyTests : FunSpec({
    test("String length should return the length of the string") {
        "sammy".length shouldBe 5
        "".length shouldBe 0
    }
})
