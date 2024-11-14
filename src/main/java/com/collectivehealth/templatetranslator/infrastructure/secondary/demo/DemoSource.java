package com.collectivehealth.templatetranslator.infrastructure.secondary.demo;

import com.collectivehealth.templatetranslator.application.secondary.DataSource;
import com.collectivehealth.templatetranslator.application.secondary.SourceInfo;
import com.collectivehealth.templatetranslator.domain.Phrases;
import org.springframework.stereotype.Component;

@Component
public class DemoSource implements DataSource {


    @Override
    public Phrases fetchPhrases(SourceInfo sourceInfo) {
        if(sourceInfo instanceof DemoValues dv){
            return  new Phrases(dv.values());
        }
        throw new RuntimeException("Invalid Info");
    }
}
