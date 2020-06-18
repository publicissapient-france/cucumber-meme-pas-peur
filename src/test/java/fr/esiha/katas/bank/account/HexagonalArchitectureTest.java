package fr.esiha.katas.bank.account;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(
    packages = "fr.esiha.katas.bank.account",
    importOptions = DoNotIncludeTests.class
)
@SuppressWarnings("unused")
public class HexagonalArchitectureTest {
    private static final String BASE_PACKAGE = "fr.esiha.katas.bank.account";

    @ArchTest
    public static final ArchRule domainCanOnlyHaveWhiteListedDependencies =
        classes().that().resideInAPackage(BASE_PACKAGE + ".domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
            BASE_PACKAGE + ".domain..",
            "java.."
        );

    @ArchTest
    public static final ArchRule drivingAdaptersShouldBeIndependent =
        slices().matching(BASE_PACKAGE + ".(driving).(*)..")
            .namingSlices("$1 adapter '$2'").should().notDependOnEachOther();

    @ArchTest
    public static final ArchRule drivenAdaptersShouldBeIndependent =
        slices().matching(BASE_PACKAGE + ".(driven).(*)..")
            .namingSlices("$1 adapter '$2'").should().notDependOnEachOther();
}
