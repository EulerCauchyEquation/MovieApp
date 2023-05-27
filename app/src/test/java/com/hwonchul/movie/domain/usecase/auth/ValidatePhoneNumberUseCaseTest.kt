package com.hwonchul.movie.domain.usecase.auth

import com.hwonchul.movie.exception.InvalidCategoryException
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
class ValidatePhoneNumberUseCaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var validateUseCase: ValidatePhoneNumberUseCaseImpl

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `유효한 한국 전화번호 테스트`() {
        val result = validateUseCase("01012345678", "+82")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `국가코드는 다른 경우`() {
        val result = validateUseCase("01012345678", "+52")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidCategoryException)
    }

    @Test
    fun `전화번호의 숫자 개수가 맞지 않는 경우`() {
        val result = validateUseCase("0101234567", "+82")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidFormatException)
    }

    @Test
    fun `전화번호, 국가코드 모두 유효하지 않는 경우`() {
        val result = validateUseCase("010123456", "+52")
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertTrue(exception is InvalidCategoryException)
    }
}