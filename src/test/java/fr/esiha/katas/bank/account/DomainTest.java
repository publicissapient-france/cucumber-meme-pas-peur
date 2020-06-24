package fr.esiha.katas.bank.account;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    strict = true,
    features = "classpath:features",
    plugin = "pretty",
    tags = "not @unimplemented"
)
public class DomainTest {
}
