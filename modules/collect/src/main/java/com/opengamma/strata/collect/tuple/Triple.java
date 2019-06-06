/*
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.collect.tuple;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;

/**
 * An immutable triple consisting of three elements.
 * <p>
 * This implementation refers to the elements as 'first', 'second' and 'third'.
 * The elements cannot be null.
 * <p>
 * Although the implementation is immutable, there is no restriction on the objects
 * that may be stored. If mutable objects are stored in the triple, then the triple
 * itself effectively becomes mutable.
 * <p>
 * This class is immutable and thread-safe if the stored objects are immutable.
 *
 * @param <A> the first element type
 * @param <B> the second element type
 * @param <C> the third element type
 */
@BeanDefinition(builderScope = "private")
public final class Triple<A, B, C>
    implements ImmutableBean, Tuple, Comparable<Triple<A, B, C>>, Serializable {

  /**
   * The first element in this triple.
   */
  @PropertyDefinition(validate = "notNull")
  private final A first;
  /**
   * The second element in this triple.
   */
  @PropertyDefinition(validate = "notNull")
  private final B second;
  /**
   * The third element in this triple.
   */
  @PropertyDefinition(validate = "notNull")
  private final C third;

  //-------------------------------------------------------------------------
  /**
   * Obtains a triple inferring the types.
   * 
   * @param <A> the first element type
   * @param <B> the second element type
   * @param <C> the third element type
   * @param first  the first element
   * @param second  the second element
   * @param third  the third element
   * @return a triple formed from the three parameters
   */
  public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
    return new Triple<A, B, C>(first, second, third);
  }

  //-------------------------------------------------------------------------
  /**
   * Returns a combiner of triple instances.
   * <p>
   * This is useful if you have a stream of {@code Triple<A, B, C>} and would like to call reduce.
   * <p>
   * e.g
   * <pre>{@code tripleList.stream()
   *     .reduce(Triple.combining(A::combinedWith, B::combinedWith, C::combinedWith))
   * }</pre>
   *
   * @param <A>  the type of the first values
   * @param <B>  the type of the second values
   * @param <C>  the type of the third values
   * @param combinerFirst  the combiner of first values
   * @param combinerSecond  the combiner of second values
   * @param combinerThird  the combiner of third values
   * @return the combiner of triple instances
   */
  public static <A, B, C> BinaryOperator<Triple<A, B, C>> combining(
      BiFunction<? super A, ? super A, ? extends A> combinerFirst,
      BiFunction<? super B, ? super B, ? extends B> combinerSecond,
      BiFunction<? super C, ? super C, ? extends C> combinerThird) {

    return (triple1, triple2) -> triple1.combinedWith(triple2, combinerFirst, combinerSecond, combinerThird);
  }

  /**
   * Combines this instance with another.
   *
   * @param <Q>  the type of the first value in the other instance
   * @param <R>  the type of the second value in the other instance
   * @param <S>  the type of the third value in the other instance
   * @param combinerFirst  the combiner of first values
   * @param combinerSecond  the combiner of second values
   * @param combinerThird  the combiner of third values
   * @return the combined triple instance
   */
  public <Q, R, S> Triple<A, B, C> combinedWith(
      Triple<Q, R, S> other,
      BiFunction<? super A, ? super Q, ? extends A> combinerFirst,
      BiFunction<? super B, ? super R, ? extends B> combinerSecond,
      BiFunction<? super C, ? super S, ? extends C> combinerThird) {

    return Triple.of(
        combinerFirst.apply(first, other.getFirst()),
        combinerSecond.apply(second, other.getSecond()),
        combinerThird.apply(third, other.getThird()));
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the number of elements held by this triple.
   * 
   * @return size 3
   */
  @Override
  public int size() {
    return 3;
  }

  /**
   * Gets the elements from this triple as a list.
   * <p>
   * The list returns each element in the triple in order.
   * 
   * @return the elements as an immutable list
   */
  @Override
  public ImmutableList<Object> elements() {
    return ImmutableList.of(first, second, third);
  }

  //-------------------------------------------------------------------------
  /**
   * Compares the triple based on the first element followed by the second
   * element followed by the third element.
   * <p>
   * The element types must be {@code Comparable}.
   * 
   * @param other  the other triple
   * @return negative if this is less, zero if equal, positive if greater
   * @throws ClassCastException if either object is not comparable
   */
  @Override
  public int compareTo(Triple<A, B, C> other) {
    return ComparisonChain.start()
        .compare((Comparable<?>) first, (Comparable<?>) other.first)
        .compare((Comparable<?>) second, (Comparable<?>) other.second)
        .compare((Comparable<?>) third, (Comparable<?>) other.third)
        .result();
  }

  /**
   * Gets the triple using a standard string format.
   * <p>
   * The standard format is '[$first, $second, $third]'. Spaces around the values are trimmed.
   * 
   * @return the triple as a string
   */
  @Override
  public String toString() {
    return new StringBuilder()
        .append('[')
        .append(first)
        .append(", ")
        .append(second)
        .append(", ")
        .append(third)
        .append(']')
        .toString();
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code Triple}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static Triple.Meta meta() {
    return Triple.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code Triple}.
   * @param <R>  the first generic type
   * @param <S>  the second generic type
   * @param <T>  the second generic type
   * @param cls1  the first generic type
   * @param cls2  the second generic type
   * @param cls3  the third generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R, S, T> Triple.Meta<R, S, T> metaTriple(Class<R> cls1, Class<S> cls2, Class<T> cls3) {
    return Triple.Meta.INSTANCE;
  }

  static {
    MetaBean.register(Triple.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private Triple(
      A first,
      B second,
      C third) {
    JodaBeanUtils.notNull(first, "first");
    JodaBeanUtils.notNull(second, "second");
    JodaBeanUtils.notNull(third, "third");
    this.first = first;
    this.second = second;
    this.third = third;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Triple.Meta<A, B, C> metaBean() {
    return Triple.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the first element in this triple.
   * @return the value of the property, not null
   */
  public A getFirst() {
    return first;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the second element in this triple.
   * @return the value of the property, not null
   */
  public B getSecond() {
    return second;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the third element in this triple.
   * @return the value of the property, not null
   */
  public C getThird() {
    return third;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
      return JodaBeanUtils.equal(first, other.first) &&
          JodaBeanUtils.equal(second, other.second) &&
          JodaBeanUtils.equal(third, other.third);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(first);
    hash = hash * 31 + JodaBeanUtils.hashCode(second);
    hash = hash * 31 + JodaBeanUtils.hashCode(third);
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code Triple}.
   * @param <A>  the type
   * @param <B>  the type
   * @param <C>  the type
   */
  public static final class Meta<A, B, C> extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code first} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<A> first = (DirectMetaProperty) DirectMetaProperty.ofImmutable(
        this, "first", Triple.class, Object.class);
    /**
     * The meta-property for the {@code second} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<B> second = (DirectMetaProperty) DirectMetaProperty.ofImmutable(
        this, "second", Triple.class, Object.class);
    /**
     * The meta-property for the {@code third} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<C> third = (DirectMetaProperty) DirectMetaProperty.ofImmutable(
        this, "third", Triple.class, Object.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "first",
        "second",
        "third");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 97440432:  // first
          return first;
        case -906279820:  // second
          return second;
        case 110331239:  // third
          return third;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends Triple<A, B, C>> builder() {
      return new Triple.Builder<>();
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends Triple<A, B, C>> beanType() {
      return (Class) Triple.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code first} property.
     * @return the meta-property, not null
     */
    public MetaProperty<A> first() {
      return first;
    }

    /**
     * The meta-property for the {@code second} property.
     * @return the meta-property, not null
     */
    public MetaProperty<B> second() {
      return second;
    }

    /**
     * The meta-property for the {@code third} property.
     * @return the meta-property, not null
     */
    public MetaProperty<C> third() {
      return third;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 97440432:  // first
          return ((Triple<?, ?, ?>) bean).getFirst();
        case -906279820:  // second
          return ((Triple<?, ?, ?>) bean).getSecond();
        case 110331239:  // third
          return ((Triple<?, ?, ?>) bean).getThird();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code Triple}.
   * @param <A>  the type
   * @param <B>  the type
   * @param <C>  the type
   */
  private static final class Builder<A, B, C> extends DirectPrivateBeanBuilder<Triple<A, B, C>> {

    private A first;
    private B second;
    private C third;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 97440432:  // first
          return first;
        case -906279820:  // second
          return second;
        case 110331239:  // third
          return third;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder<A, B, C> set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 97440432:  // first
          this.first = (A) newValue;
          break;
        case -906279820:  // second
          this.second = (B) newValue;
          break;
        case 110331239:  // third
          this.third = (C) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Triple<A, B, C> build() {
      return new Triple<>(
          first,
          second,
          third);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("Triple.Builder{");
      buf.append("first").append('=').append(JodaBeanUtils.toString(first)).append(',').append(' ');
      buf.append("second").append('=').append(JodaBeanUtils.toString(second)).append(',').append(' ');
      buf.append("third").append('=').append(JodaBeanUtils.toString(third));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
