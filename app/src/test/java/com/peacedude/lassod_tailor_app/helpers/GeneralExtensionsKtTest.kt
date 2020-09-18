package com.peacedude.lassod_tailor_app.helpers

import com.peacedude.lassod_tailor_app.sharedpref.FakeSharedPrefManager
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*


internal class GeneralExtensionsKtTest : FunSpec({
    val anyClasstitle = AnyClass().getName()
    test("Should return a string of the class name path") {
        anyClasstitle.contains("AnyClass") shouldBe true
    }

})