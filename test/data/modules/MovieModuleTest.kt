package data.modules

import eu.bbsapps.forgottenfilmsapi.database
import eu.bbsapps.forgottenfilmsapi.productionDatabase
import eu.bbsapps.forgottenfilmsapi.testDatabase
import org.junit.After
import org.junit.Before

class MovieModuleTest {

    @Before
    fun initialize() {
        database = testDatabase
    }

    @After
    fun cleanUp() {
        database = productionDatabase
    }
}