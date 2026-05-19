package kr.osj.livving.data.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun platformHttpClientEngineFactory(): HttpClientEngineFactory<*> = CIO
