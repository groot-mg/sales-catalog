package com.generoso.salescatalog.exception.error

import java.time.LocalDateTime


class ValidationErrorDetails : ErrorDetail() {
    var validations: Array<ValidationErrorFields>? = null

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        private var status: Int? = null
        private var detail: String? = null
        private var dateTime: LocalDateTime? = null
        private var validations: Array<ValidationErrorFields>? = null

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

        fun validations(validations: Array<ValidationErrorFields>?): Builder {
            this.validations = validations
            return this
        }

        fun build(): ValidationErrorDetails {
            val validationErrorDetails = ValidationErrorDetails()
            validationErrorDetails.detail = detail
            validationErrorDetails.dateTime = dateTime
            validationErrorDetails.status = status
            validationErrorDetails.validations = validations
            return validationErrorDetails
        }
    }

    override fun toString(): String {
        return "ValidationErrorDetails(validations=${validations?.contentToString()})"
    }
}

