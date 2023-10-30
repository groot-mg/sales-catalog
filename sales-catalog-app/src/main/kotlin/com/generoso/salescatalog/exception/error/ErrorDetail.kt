package com.generoso.salescatalog.exception.error

import java.time.LocalDateTime

open class ErrorDetail {
    var status: Int? = null
    var detail: String? = null
    var dateTime: LocalDateTime? = null

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        private var status: Int? = null
        private var detail: String? = null
        private var dateTime: LocalDateTime? = null

        fun status(status: Int): Builder {
            this.status = status
            return this
        }

        fun detail(detail: String?): Builder {
            this.detail = detail
            return this
        }

        fun dateTime(dateTime: LocalDateTime): Builder {
            this.dateTime = dateTime
            return this
        }

        fun build(): ErrorDetail {
            val errorDetail = ErrorDetail()
            errorDetail.status = status
            errorDetail.detail = detail
            errorDetail.dateTime = dateTime
            return errorDetail
        }
    }
}
