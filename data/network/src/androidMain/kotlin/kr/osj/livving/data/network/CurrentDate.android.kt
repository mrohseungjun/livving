package kr.osj.livving.data.network

import java.time.LocalDate

actual fun currentDateIso(): String = LocalDate.now().toString()
