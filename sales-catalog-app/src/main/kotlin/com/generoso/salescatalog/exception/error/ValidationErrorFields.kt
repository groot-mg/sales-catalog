package com.generoso.salescatalog.exception.error

class ValidationErrorFields {
    var field: String = ""
    var messages: Array<String> = emptyArray()

    constructor()

    constructor(field: String, messages: Array<String>) {
        this.field = field
        this.messages = messages
    }

    override fun toString(): String {
        return "ValidationErrorFields(field='$field', messages=${messages.contentToString()})"
    }
}
