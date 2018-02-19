package de.naju.adebar.services.conversion;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.MultiValueMap;
import de.naju.adebar.services.conversion.FormToMapConverter;

public class FormToMapConverterUnitTests {

  private FormToMapConverter converter;

  @Before
  public void setUp() {
    converter = new FormToMapConverter();
  }

  @Test
  public void extractsAllFieldsFromGetters() {
    // this also ensures the correct naming of the map keys
    SimpleForm form = new SimpleForm("Hello World", 42);
    MultiValueMap<String, Object> map = converter.convert(form);
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.getFirst("foo")).isEqualTo("Hello World");
    assertThat(map.get("foo")).hasSize(1);
    assertThat(map.getFirst("bar")).isEqualTo(42);
    assertThat(map.get("bar")).hasSize(1);
  }

  @Test
  public void ignoresNonGetterMethods() {
    FormWithExtraMethods form = new FormWithExtraMethods("Bar");
    MultiValueMap<String, Object> map = converter.convert(form);
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.getFirst("foo")).isEqualTo("Bar");
    assertThat(map.get("foo")).hasSize(1);
  }

  @Test
  public void unzipsArraysAndCollections() {
    Integer[] arr = {1, 2, 42};
    List<String> list = Arrays.asList("Hello", "World", "!");
    FormWithComplexTypes form = new FormWithComplexTypes(arr, list, 4.2);
    MultiValueMap<String, Object> map = converter.convert(form);
    assertThat(map).hasSize(3);
    assertThat(map.get("bars")).hasSize(list.size());
    assertThat(map.get("bars")).containsExactly(list.toArray());
    assertThat(map.get("foos")).hasSize(arr.length);
    assertThat(map.get("foos")).containsExactly((Object[]) arr);
    assertThat(map.getFirst("baz")).isEqualTo(4.2);
    assertThat(map.get("baz")).hasSize(1);
  }

  @Test
  public void extractsFieldsFromQueryMethods() {
    FormWithQueryMethods form = new FormWithQueryMethods("Hello World", false);
    MultiValueMap<String, Object> map = converter.convert(form);
    assertThat(map).hasSize(2);
    assertThat(map.get("foo")).hasSize(1);
    assertThat(map.getFirst("foo")).isEqualTo("Hello World");
    assertThat(map.get("bar")).hasSize(1);
    assertThat(map.getFirst("bar")).isEqualTo(false);
  }

  @Test
  public void extractsFieldsWithShortNames() {
    FormWithShortFieldNames form = new FormWithShortFieldNames("Foo bar", 4.2F);
    MultiValueMap<String, Object> map = converter.convert(form);
    assertThat(map).hasSize(2);
    assertThat(map.get("a")).hasSize(1);
    assertThat(map.getFirst("a")).isEqualTo("Foo bar");
    assertThat(map.get("b")).hasSize(1);
    assertThat(map.getFirst("b")).isEqualTo(4.2F);
  }

  @Test
  public void onlyExtractsFieldsWithPublicGetters() {
    FormWithSomeProtectedGetters form = new FormWithSomeProtectedGetters("Hello", "World");
    MultiValueMap<String, Object> map = converter.convert(form);
    assertThat(map).hasSize(1);
    assertThat(map.get("foo")).hasSize(1);
    assertThat(map.getFirst("foo")).isEqualTo("Hello");
  }

  class SimpleForm {
    private String foo;
    private int bar;

    SimpleForm(String foo, int bar) {
      this.foo = foo;
      this.bar = bar;
    }

    public String getFoo() {
      return foo;
    }

    public int getBar() {
      return bar;
    }

  }

  class FormWithExtraMethods {
    private String foo;

    FormWithExtraMethods(String foo) {
      this.foo = foo;
    }

    public String getFoo() {
      return foo;
    }

    public String greetMe() {
      return foo;
    }

    public String foo() {
      return foo;
    }

    public void doSomething() {
      // No, do nothing
    }

    public void doSomeMore(String param) {
      // Still lazy
    }

  }

  class FormWithComplexTypes {
    Integer[] foos;
    List<String> bars;
    double baz;

    FormWithComplexTypes(Integer[] foos, List<String> bars, double baz) {
      this.foos = foos;
      this.bars = bars;
      this.baz = baz;
    }

    public Integer[] getFoos() {
      return foos;
    }

    public List<String> getBars() {
      return bars;
    }

    public double getBaz() {
      return baz;
    }

  }

  class FormWithQueryMethods {
    String foo;
    boolean bar;

    FormWithQueryMethods(String foo, boolean bar) {
      this.foo = foo;
      this.bar = bar;
    }

    public String getFoo() {
      return foo;
    }

    public boolean isBar() {
      return bar;
    }

    int baz() {
      return 42;
    }

  }

  class FormWithShortFieldNames {
    String a;
    float b;

    FormWithShortFieldNames(String a, float b) {
      this.a = a;
      this.b = b;
    }

    public String getA() {
      return a;
    }

    public float getB() {
      return b;
    }

  }

  class FormWithSomeProtectedGetters {
    String foo;
    String bar;

    FormWithSomeProtectedGetters(String foo, String bar) {
      this.foo = foo;
      this.bar = bar;
    }

    public String getFoo() {
      return foo;
    }

    protected String getBar() {
      return bar;
    }
  }
}
