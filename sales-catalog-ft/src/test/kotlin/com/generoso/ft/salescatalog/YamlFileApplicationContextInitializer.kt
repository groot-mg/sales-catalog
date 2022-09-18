package com.generoso.ft.salescatalog

import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.PropertySource
import java.io.IOException
import java.util.*
import java.util.function.Consumer

class YamlFileApplicationContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val propertySources: List<PropertySource<*>> = loadPropertySources(applicationContext)
        val profileToPropertySourcesMap: Map<String?, List<PropertySource<*>>> =
            createProfileToPropertySourcesMap(propertySources)
        val environment = applicationContext.environment
        val commonPropertySources = profileToPropertySourcesMap[null]
        val activeNonCommonPropertySourcesInPriorityOrder: Set<PropertySource<*>> =
            getActiveNonCommonPropertySourcesInPriorityOrder(environment, profileToPropertySourcesMap)

        val propertySourcesInPriorityOrder: MutableList<PropertySource<*>> = LinkedList()
        propertySourcesInPriorityOrder.addAll(commonPropertySources!!)
        propertySourcesInPriorityOrder.addAll(activeNonCommonPropertySourcesInPriorityOrder)

        val propertySourcesInReversePriorityOrder = Lists.reverse(propertySourcesInPriorityOrder)
        propertySourcesInReversePriorityOrder.forEach(Consumer { propertySource: PropertySource<*>? ->
            environment.propertySources.addFirst(propertySource!!)
        })
    }

    private fun loadPropertySources(applicationContext: ConfigurableApplicationContext): List<PropertySource<*>> {
        return try {
            val resourceLocation = "classpath:/test.yml"
            val resource = applicationContext.getResource(resourceLocation)
            val sourceLoader = YamlPropertySourceLoader()
            sourceLoader.load(resourceLocation, resource)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun createProfileToPropertySourcesMap(
        propertySources: List<PropertySource<*>>
    ): Map<String?, List<PropertySource<*>>> {
        val profileToPropertySourcesMap: MutableMap<String?, MutableList<PropertySource<*>>> = HashMap()
        propertySources.forEach(Consumer { propertySource: PropertySource<*> ->
            getProfiles(propertySource).forEach(Consumer { profile: String? ->
                addPropertySource(profileToPropertySourcesMap, propertySource, profile)
            })
        })
        return profileToPropertySourcesMap
    }

    private fun getProfiles(propertySource: PropertySource<*>): Iterable<String?> {
        val profilesObj = propertySource.getProperty("spring.profiles") ?: return setOf<String?>(null)
        return getProfiles(profilesObj)
    }

    private fun getProfiles(propertySourceValue: Any): Iterable<String> {
        if (propertySourceValue is String) {
            return propertySourceValue.split("\\s*,")
        } else if (propertySourceValue is Collection<*>) {
            @Suppress("UNCHECKED_CAST")
            return propertySourceValue as List<String>
        }

        throw UnsupportedOperationException(
            "Unsupported type of spring profiles property source value - " +
                    "$propertySourceValue"
        )
    }

    private fun addPropertySource(
        profileToPropertySourcesMap: MutableMap<String?, MutableList<PropertySource<*>>>,
        propertySource: PropertySource<*>, profile: String?
    ) {
        profileToPropertySourcesMap.computeIfAbsent(profile) { ArrayList() }.add(propertySource)
    }

    private fun getActiveNonCommonPropertySourcesInPriorityOrder(
        environment: ConfigurableEnvironment, profileToPropertySourcesMap: Map<String?, List<PropertySource<*>>>
    ): Set<PropertySource<*>> {
        val activeProfilesArray = environment.activeProfiles
        var activeProfiles: Iterable<String> = emptyList()

        if (activeProfilesArray.isNotEmpty()) {
            activeProfiles = Arrays.asList(*activeProfilesArray)
        } else {
            val commonPropertySources = profileToPropertySourcesMap[null]!!
            val commonPropertySourcesActiveProfilesOptional = commonPropertySources.stream()
                .map { ps: PropertySource<*> -> ps.getProperty("spring.profiles.active") }
                .filter { obj: Any? -> Objects.nonNull(obj) }
                .findFirst()
            if (commonPropertySourcesActiveProfilesOptional.isPresent) {
                val commonPropertySourcesActiveProfilesObj = commonPropertySourcesActiveProfilesOptional.get()
                activeProfiles = getProfiles(commonPropertySourcesActiveProfilesObj)
                log.warn(
                    "no active profiles have explicitly set, so using the common PropertySource(s) active profile(s) {}",
                    activeProfiles
                )
            }
        }

        val activeNonCommonPropertySourcesInPriorityOrder: MutableSet<PropertySource<*>> = LinkedHashSet()
        for (activeProfile in activeProfiles) {
            val activeProfilePropertySources = profileToPropertySourcesMap[activeProfile]
            if (activeProfilePropertySources != null) {
                activeNonCommonPropertySourcesInPriorityOrder.addAll(activeProfilePropertySources)
            }
        }
        return activeNonCommonPropertySourcesInPriorityOrder
    }
}