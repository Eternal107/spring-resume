package net.study.resume.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.Sm2TagRuleBundle;

public class CustomSiteMeshFilter extends ConfigurableSiteMeshFilter {
    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        // Assigning default decorator if no path specific decorator found
        builder.addDecoratorPath("/*", "/WEB-INF/template/page-template.jsp")
                .addDecoratorPath("/fragment/*", "/WEB-INF/template/fragment-template.jsp")
                .addTagRuleBundle(new Sm2TagRuleBundle());
    }
}