package com.desafiowicket.icons;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class SvgIcon extends WebMarkupContainer {
    private static final long serialVersionUID = 1L;
    private final String iconId;

    public SvgIcon(String id, String iconId) {
        super(id);
        this.iconId = iconId;
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        checkComponentTag(tag, "svg");
        tag.put("class", getDefaultModelObjectAsString() + " icon " + iconId);
        tag.put("aria-hidden", "true");
    }
}