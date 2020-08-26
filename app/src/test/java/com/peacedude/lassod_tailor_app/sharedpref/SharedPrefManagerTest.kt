package com.peacedude.lassod_tailor_app.sharedpref

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SharedPrefManagerTest : FunSpec({
    val h = hashMapOf("hello" to "world")
    h["love"] = "Love"
    h["crazy"] = "Crazy"
    h["bola"] = "Bola"
    val fake = FakeSharedPrefManager(h)
    test("Should return an empty storage") {
        fake.clearData()
        h.size shouldBe 0
        h.contains("hello") shouldBe false
    }
    test("Should save the data with the provided key") {
        fake.saveData("Bola", "bola")
        h.contains("bola") shouldBe true
        h["bola"] shouldBe "Bola"
    }
    test("Should return a list of the values with the provided key") {
        val result = fake.saveData("Bola", "bola")
        result.size shouldBeGreaterThan 0
        result[0] shouldBe "Bola"
    }
    test("Should return true after successfully removing a value by key") {
        val result = fake.clearByKey<String>( "bola")
        result shouldBe true
    }
    test("Should return false if key is not present") {
        val result = fake.clearByKey<String>( "kola")
        result shouldBe false
    }
})

