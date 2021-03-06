package uk.gov.justice.services.test.utils.core.reflection;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class ReflectionUtilTest {

    @Test
    public void shouldBeWellDefinedUtilityClass() {
        assertUtilityClassWellDefined(ReflectionUtil.class);
    }

    @Test
    public void shouldReturnListOfMethods() throws Exception {
        final List<Method> methods = ReflectionUtil.methodsOf(TestClass.class);

        assertThat(methods.size(), is(2));
        assertThat(methods.get(0).getName(), is("method1"));
        assertThat(methods.get(1).getName(), is("method2"));
    }

    @Test
    public void shouldReturnTheFirstMethod() throws Exception {
        final Optional<Method> method = ReflectionUtil.firstMethodOf(TestClass.class);

        assertThat(method.isPresent(), is(true));
        assertThat(method.get().getName(), is("method1"));
    }

    @Test
    public void shouldReturnTheNamedMethod() throws Exception {
        final Optional<Method> method = ReflectionUtil.methodOf(TestClass.class, "method2");

        assertThat(method.isPresent(), is(true));
        assertThat(method.get().getName(), is("method2"));
    }

    @Test
    public void shouldReturnAnnotatedMethod() throws Exception {
        final Optional<Method> method = ReflectionUtil.annotatedMethod(AnnotatedTestClass.class, TestAnnotation.class);

        assertThat(method.isPresent(), is(true));
        assertThat(method.get().getName(), is("method2"));
    }

    @Test
    public void shouldReturnOptionalEmptyIfNoMethodWithAnnotation() throws Exception {
        final Optional<Method> method = ReflectionUtil.annotatedMethod(TestClass.class, TestAnnotation.class);

        assertThat(method, is(Optional.empty()));
    }

    @Test
    public void shouldGetAndSetFieldValues() throws Exception {
        final TestClass testClass = new TestClass();

        final Optional<Object> fieldValue = ReflectionUtil.fieldValue(testClass, "field1");

        assertThat(fieldValue.isPresent(), is(true));
        assertThat(fieldValue.get(), is(instanceOf(String.class)));
        assertThat(fieldValue.get(), is("value1"));

        ReflectionUtil.setField(testClass, "field1", "new value");
        final Optional<String> resultValue = ReflectionUtil.fieldValueAs(testClass, "field1", String.class);

        assertThat(resultValue.isPresent(), is(true));
        assertThat(resultValue.get(), is("new value"));
    }

    @Documented
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface TestAnnotation {
    }

    private class TestClass {

        private String field1 = "value1";

        public void method1() {
        }

        public boolean method2() {
            return true;
        }
    }

    private class AnnotatedTestClass {

        public void method1() {
        }

        @TestAnnotation
        public boolean method2() {
            return true;
        }
    }
}