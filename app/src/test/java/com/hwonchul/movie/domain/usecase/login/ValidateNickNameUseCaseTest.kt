package com.hwonchul.movie.domain.usecase.login

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
class ValidateNickNameUseCaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var validateUseCase: ValidateNickNameUseCaseImpl

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `유효한 닉네임`() {
        val result = validateUseCase("외국인노동자")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `닉네임 길이가 너무 짧은 경우`() {
        val result = validateUseCase("가나")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }

    @Test
    fun `닉네임 길이가 너무 긴 경우`() {
        val result = validateUseCase("abcd아델라인efgh아델라인")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }

    @Test
    fun `닉네임에 특수문자가 들어간 경우`() {
        val result = validateUseCase("@아델라인#")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }

    @Test
    fun `닉네임에 띄어쓰기가 들어간 경우`() {
        val result = validateUseCase("외국인 노동자")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }
}