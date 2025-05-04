package com.tksimeji.kunectron;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DispenserGui {
    boolean autoReload() default false;

    boolean serverSideTranslation() default false;

    boolean markupExtensions() default false;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Player {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Title {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Element {
        int[] index() default {};
        IndexGroup[] groups() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Policy {
        int[] index() default {};
        boolean player() default false;
        IndexGroup[] groups() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface DefaultPolicy {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface PlayerDefaultPolicy {
    }
}
