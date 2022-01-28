import data.modules.*
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    RegisterModuleTest::class,
    LoginModuleTest::class,
    AccountManagementModuleTest::class,
    AdminModuleTest::class,
    FilmsModuleTest::class
)
class ModuleTestSuite