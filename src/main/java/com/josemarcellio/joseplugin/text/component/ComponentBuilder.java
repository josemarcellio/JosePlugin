package com.josemarcellio.joseplugin.text.component;

import com.josemarcellio.joseplugin.text.component.module.*;

public class ComponentBuilder {

    private final MultiComponentBuilder multiComponentBuilder = new MultiComponentBuilder();
    private final PlaceholderComponentBuilder placeholderComponentBuilder = new PlaceholderComponentBuilder();
    private final SingleComponentBuilder singleComponentBuilder = new SingleComponentBuilder();
    private final SinglePlaceholderComponentBuilder singlePlaceholderComponentBuilder = new SinglePlaceholderComponentBuilder();
    private final TagResolverComponentBuilder tagResolverComponentBuilder = new TagResolverComponentBuilder();
    private final TagResolverComponentPlaceholderBuilder tagResolverComponentPlaceholderBuilder = new TagResolverComponentPlaceholderBuilder();

    public MultiComponentBuilder multiComponentBuilder(String... text) {
        return multiComponentBuilder.text(text);
    }

    public PlaceholderComponentBuilder placeholderComponentBuilder(String... text) {
        return placeholderComponentBuilder.text(text);
    }

    public SingleComponentBuilder singleComponentBuilder(String text) {
        return singleComponentBuilder.text(text);
    }

    public SinglePlaceholderComponentBuilder singlePlaceholderComponentBuilder (String text) {
        return singlePlaceholderComponentBuilder.text(text);
    }

    public TagResolverComponentBuilder tagResolverBuilder(String text) {
        return tagResolverComponentBuilder.text(text);
    }

    public TagResolverComponentPlaceholderBuilder tagResolverPlaceholderBuilder(String text) {
        return tagResolverComponentPlaceholderBuilder.text(text);
    }
}
