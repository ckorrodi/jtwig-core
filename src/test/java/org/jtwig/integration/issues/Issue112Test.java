package org.jtwig.integration.issues;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.exceptions.CalculationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.jtwig.configuration.ConfigurationBuilder.configuration;

public class Issue112Test {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // 1.A
    @Test
    public void outputNonexistentVarReturnsEmpty() throws Exception {
        JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{{ nonexistent }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 1.B
    @Test
    public void outputNonexistentVarThrowsException() throws Exception {
        expectedException.expectMessage(containsString("Variable 'nonexistent' undefined"));

        JtwigModel model = new JtwigModel();

        JtwigTemplate
                .inlineTemplate("{{ nonexistent }}", configuration()
                        .withStrictMode(true)
                        .build())
                .render(model);

    }

    // 2.A
    @Test
    public void selectionExampleWithStrictValidation() throws Exception {
        JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{{ undefinedVar.length }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 2.B
    @Test
    public void nestedSelectionExampleWithStrictValidation() throws Exception {
        JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{{ undefinedVar.length.another }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 2.C
    @Test
    public void selectionExampleWithoutStrictValidation() throws Exception {
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage(containsString("Variable 'undefinedVar' undefined"));

        JtwigModel model = new JtwigModel();

        JtwigTemplate
                .inlineTemplate("{{ undefinedVar.length }}", configuration()
                        .withStrictMode(true)
                        .build())
                .render(model);
    }

    // 3.A
    @Test
    public void operationExampleWithoutStrictValidation () throws Exception {JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{{ undefinedVar + 3 }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("3")));
    }

    // 3.B
    @Test
    public void operationExampleWithStrictValidation () throws Exception {
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage(containsString("Variable 'undefinedVar' undefined"));

        JtwigModel model = new JtwigModel();

        JtwigTemplate
                .inlineTemplate("{{ undefinedVar + 3 }}", configuration()
                        .withStrictMode(true)
                        .build())
                .render(model);
    }

    // 3.C
    @Test
    public void operationWithNonexistentVarThrowsException() throws Exception {
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage(containsString("Variable 'b' undefined"));

        JtwigModel model = new JtwigModel();

        JtwigTemplate
                .inlineTemplate("{% set a = 5 %}{{ a - b  }}", configuration()
                        .withStrictMode(true)
                        .build())
                .render(model);
    }

    // 3.D
    @Test
    public void subtractOperationWithNullVarInStrictMode() throws Exception {JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a - b }}", configuration()
                        .withStrictMode(true)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("5")));
    }

    // 3.E
    @Test
    public void subtractOperationWithNullVarInNonStrictMode() throws Exception {
        JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a - b }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("5")));
    }

    // 3.F
    @Test
    public void divOperationWithNullVarInStrictMode() throws Exception {
        expectedException.expect(ArithmeticException.class);
        expectedException.expectMessage(containsString("Division by zero"));

        JtwigModel model = new JtwigModel();

        JtwigTemplate
                .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a / b }}", configuration()
                        .withStrictMode(true)
                        .build())
                .render(model);
    }

    // 3.G
    @Test
    public void divOperationWithNullVarInNonStrictMode() throws Exception {
        expectedException.expect(ArithmeticException.class);
        expectedException.expectMessage(containsString("Division by zero"));

        JtwigModel model = new JtwigModel();

        JtwigTemplate
                .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a / b }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);
    }

    // 4.A
    @Test
    public void outputNullVarReturnsEmpty() throws Exception {
        JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{% set nothing = null %}{{ nothing }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 4.B
    @Test
    public void outputNullVarThrowsException() throws Exception {JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{% set nothing = null %}{{ nothing }}", configuration()
                        .withStrictMode(true)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("")));
    }


    @Test
    public void booleanValuesShouldBePrintItsIntegerRepresentation() throws Exception {JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{{ true }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void comparisonBetweenUndefinedAndZero() throws Exception {JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{{ nothing == 0 }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void comparisonBetweenUndefinedAndNull() throws Exception {
        JtwigModel model = new JtwigModel();

        String result = JtwigTemplate
                .inlineTemplate("{{ nothing == null }}", configuration()
                        .withStrictMode(false)
                        .build())
                .render(model);

        assertThat(result, is(equalTo("1")));
    }
}
