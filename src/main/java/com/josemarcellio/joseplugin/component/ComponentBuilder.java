package com.josemarcellio.joseplugin.component;

import com.josemarcellio.joseplugin.component.module.*;

public class ComponentBuilder {
    
    private final SingleComponentBuilder singleComponentBuilder = new SingleComponentBuilder();
    private final TagResolverComponentBuilder tagResolverComponentBuilder = new TagResolverComponentBuilder();

    public SingleComponentBuilder singleComponentBuilder() {
        return singleComponentBuilder;
    }

    public TagResolverComponentBuilder tagResolverBuilder() {
        return tagResolverComponentBuilder;
    }
}
