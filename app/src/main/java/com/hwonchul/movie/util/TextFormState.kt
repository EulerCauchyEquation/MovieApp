package com.hwonchul.movie.util

/**
 * 한줄평 저장하기 폼 형태
 */
class TextFormState {
  val contentError: Int?
  val isDataValid: Boolean

  constructor(contentError: Int?) {
    this.contentError = contentError
    isDataValid = false
  }

  constructor(isDataValid: Boolean) {
    contentError = null
    this.isDataValid = isDataValid
  }
}