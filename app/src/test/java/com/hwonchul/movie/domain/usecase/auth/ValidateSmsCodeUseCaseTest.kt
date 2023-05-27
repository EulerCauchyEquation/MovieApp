package com.hwonchul.movie.domain.usecase.auth

import com.hwonchul.movie.exception.InvalidFormatException
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class ValidateSmsCodeUseCaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var validateUseCase: ValidateSmsCodeUseCaseImpl

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `유효한 smsCode`() {
        val result = validateUseCase("123456")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `smsCode 숫자가 모자른 경우`() {
        val result = validateUseCase("1234")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }

    @Test
    fun `smsCode 숫자가 많은 경우`() {
        val result = validateUseCase("1234567")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }

    @Test
    fun `smsCode 다른 문자가 있는 경우`() {
        val result = validateUseCase("12A@67")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }
}