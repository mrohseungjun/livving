package kr.osj.livving.data.network

import java.time.LocalDate

internal actual fun currentDateIso(): String = LocalDate.now().toString()
