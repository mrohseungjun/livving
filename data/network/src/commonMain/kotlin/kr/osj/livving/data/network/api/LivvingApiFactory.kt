package kr.osj.livving.data.network.api

import de.jensklingenberg.ktorfit.Ktorfit

fun provideLivvingApi(ktorfit: Ktorfit): LivvingApi = ktorfit.createLivvingApi()
