import data.modules.AccountManagementModuleTest
import data.modules.AdminModuleTest
import data.modules.LoginModuleTest
import data.modules.RegisterModuleTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    RegisterModuleTest::class,
    LoginModuleTest::class,
    AccountManagementModuleTest::class,
    AdminModuleTest::class
)
class ModuleTestSuite
