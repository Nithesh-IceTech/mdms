package za.co.spsi.pjtk.util;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface Holder {

    @Data @AllArgsConstructor
    public static class H1<A> implements Holder {
        private A a;
    }

    @Data @AllArgsConstructor
    public static class H2<A,B> implements Holder {
        private A a;
        private B b;
    }

    @Data @AllArgsConstructor
    public static class H3<A,B,C> {
        private A a;
        private B b;
        private C c;
    }

    @Data @AllArgsConstructor
    public static class H4<A,B,C,E> {
        private A a;
        private B b;
        private C c;
        private E e;
    }

}
