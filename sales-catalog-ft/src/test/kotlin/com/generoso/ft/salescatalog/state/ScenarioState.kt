package com.generoso.ft.salescatalog.state

import com.generoso.ft.salescatalog.client.RequestTemplate
import org.springframework.stereotype.Component
import java.net.http.HttpResponse

@Component
class ScenarioState {
    var requestTemplate: RequestTemplate? = null
        get() {
            if (field == null) {
                throw RuntimeException("The request template wasn't provided.")
            }
            return field
        }

    var actualResponse: HttpResponse<String>? = null
        get() {
            if (field == null) {
                throw RuntimeException("The actual response wasn't provided.")
            }
            return field
        }

    val actualResponseBody: String
        get() = actualResponse!!.body()
}