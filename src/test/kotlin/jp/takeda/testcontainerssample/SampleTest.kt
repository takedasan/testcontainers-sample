package jp.takeda.testcontainerssample

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@SqlGroup(
    Sql(scripts = ["clean.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    Sql(scripts = ["test1.sql"])
)
class SampleTest : AbstractIt() {
    @Autowired
    lateinit var dao: CityDao

    @Test
    fun test1() {
        val cities = dao.selectAll()
        assertEquals(1, cities.size)
        assertEquals(1, cities[0].id)
        assertEquals("name1", cities[0].name)
    }
}

@Testcontainers
@ContextConfiguration(initializers = [AbstractIt.DataSourceInitializer::class])
open class AbstractIt {
    init {
        mysql.start()
    }

    companion object {
        val mysql = object : MySQLContainer<Nothing>("mysql:5.6") {
            init {
                withDatabaseName("testdb")
                withClasspathResourceMapping(
                    "docker-entrypoint-initdb.d/init.sql",
                    "/docker-entrypoint-initdb.d/init.sql",
                    BindMode.READ_ONLY
                )
            }
        }
    }

    internal class DataSourceInitializer : ApplicationContextInitializer<ConfigurableApplicationContext?> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + mysql.jdbcUrl,
                "spring.datasource.username=" + mysql.username,
                "spring.datasource.password=" + mysql.password
            )
        }
    }
}
