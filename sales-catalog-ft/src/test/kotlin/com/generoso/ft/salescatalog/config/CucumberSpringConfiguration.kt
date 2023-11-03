package com.generoso.ft.salescatalog.config

import com.generoso.ft.salescatalog.YamlFileApplicationContextInitializer
import com.generoso.ft.salescatalog.state.ScenarioState
import com.generoso.ft.salescatalog.utils.PostgresDao
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    classes = [TestConfiguration::class, LocalSalesCatalogServer::class],
    initializers = [YamlFileApplicationContextInitializer::class]
)
@CucumberContextConfiguration
class CucumberSpringConfiguration(
    private val postgresDao: PostgresDao,
    private val scenarioState: ScenarioState
) {

    @Before
    fun setup() {
        postgresDao.setupDatabase()
    }

    @After
    fun cleanup() {
        postgresDao.cleanUpTables()
        scenarioState.requestTemplate?.reset()
    }
}