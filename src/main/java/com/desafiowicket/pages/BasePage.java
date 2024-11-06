package com.desafiowicket.pages;

import com.desafiowicket.icons.IconsResourceReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;

public class BasePage extends WebPage {

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new PackageResourceReference(BasePage.class, "./global.css")));
        String svgUrl = urlFor(IconsResourceReference.get(), null).toString();
        response.render(OnDomReadyHeaderItem.forScript(
                String.format("fetch('%s').then(response => response.text()).then(svg => {"
                        + "const div = document.createElement('div');"
                        + "div.style.display = 'none';"
                        + "div.innerHTML = svg;"
                        + "document.body.insertBefore(div, document.body.firstChild);"
                        + "});", svgUrl)
        ));
    }
}
