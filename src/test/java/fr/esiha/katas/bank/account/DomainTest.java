package fr.esiha.katas.bank.account;

import fr.esiha.katas.bank.account.domain.AccountService;
import fr.esiha.katas.bank.account.driven.inmemory.InMemoryAccountRepository;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.picocontainer.PicoFactory;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    strict = true,
    features = "classpath:features",
    plugin = "pretty",
    tags = "not @unimplemented",
    objectFactory = DomainTest.Configuration.class
)
public class DomainTest {
    public static final class Configuration implements ObjectFactory {
        private final ObjectFactory delegate;

        public Configuration() {
            delegate = new PicoFactory();
            delegate.addClass(AccountService.class);
            delegate.addClass(InMemoryAccountRepository.class);
        }

        @Override
        public void start() {
            delegate.start();
        }

        @Override
        public void stop() {
            delegate.stop();
        }

        @Override
        public boolean addClass(final Class<?> glueClass) {
            return delegate.addClass(glueClass);
        }

        @Override
        public <T> T getInstance(final Class<T> glueClass) {
            return delegate.getInstance(glueClass);
        }
    }
}
