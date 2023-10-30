package com.generoso.salescatalog.exception.error

import java.time.LocalDateTime


class ValidationErrorDetails : ErrorDetail() {
    var field: String? = null
    var fieldMessage: String? = null

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        private var status: Int? = null
        private var detail: String? = null
        private var dateTime: LocalDateTime? = null
        private var field: String? = null
        private var fieldMessage: String? = null

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

        fun field(field: String?): Builder {
            this.field = field
            return this
        }

        fun fieldMessage(fieldMessage: String?): Builder {
            this.fieldMessage = fieldMessage
            return this
        }

        fun build(): ValidationErrorDetails {
            val validationErrorDetails = ValidationErrorDetails()
            validationErrorDetails.detail = detail
            validationErrorDetails.dateTime = dateTime
            validationErrorDetails.status = status
            validationErrorDetails.field = field
            validationErrorDetails.fieldMessage = fieldMessage
            return validationErrorDetails
        }
    }
}

