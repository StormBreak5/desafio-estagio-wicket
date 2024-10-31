package com.desafiowicket.icons;

import org.apache.wicket.request.resource.PackageResourceReference;

public class IconsResourceReference extends PackageResourceReference {
    private static final long serialVersionUID = 1L;

    private static final IconsResourceReference INSTANCE = new IconsResourceReference();

    private IconsResourceReference() {
        super(IconsResourceReference.class, "icons.svg");
    }

    public static IconsResourceReference get() {
        return INSTANCE;
    }
}
