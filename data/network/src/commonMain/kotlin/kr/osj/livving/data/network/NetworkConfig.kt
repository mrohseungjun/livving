package kr.osj.livving.data.network

data class NetworkConfig(
    val supabaseUrl: String = "",
    val supabaseAnonKey: String = "",
) {
    val supabaseClientUrl: String
        get() = supabaseUrl.ifBlank { DEFAULT_BASE_URL }.trimEnd()

    val ktorfitBaseUrl: String
        get() = supabaseClientUrl
            .let { baseUrl ->
                if (baseUrl.endsWith("/")) {
                    baseUrl
                } else {
                    "$baseUrl/"
                }
            }

    private companion object {
        const val DEFAULT_BASE_URL = "https://example.invalid/"
    }
}
