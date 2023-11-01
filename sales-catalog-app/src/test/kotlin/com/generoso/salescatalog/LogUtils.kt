package com.generoso.salescatalog

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.assertj.core.api.Assertions
import org.assertj.core.groups.Tuple
import org.slf4j.LoggerFactory
import java.util.function.Function


object LogUtils {

    fun getListAppenderForClass(clazz: Class<*>): ListAppender<ILoggingEvent> {
        val logger = LoggerFactory.getLogger(clazz) as Logger
        val loggingEventListAppender = ListAppender<ILoggingEvent>()
        loggingEventListAppender.start()
        logger.addAppender(loggingEventListAppender)
        return loggingEventListAppender
    }

    fun assertMessageWasInLogs(appender: ListAppender<ILoggingEvent>, message: String?, level: Level) {
        Assertions.assertThat(appender.list)
            .extracting(
                Function<ILoggingEvent, Any> { obj: ILoggingEvent -> obj.formattedMessage },
                Function<ILoggingEvent, Any> { obj: ILoggingEvent -> obj.level })
            .contains(Tuple.tuple(message, level))
    }
}

