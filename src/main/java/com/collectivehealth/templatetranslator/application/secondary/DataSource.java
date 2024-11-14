package com.collectivehealth.templatetranslator.application.secondary;

import com.collectivehealth.templatetranslator.domain.Phrases;

public interface DataSource {

    Phrases fetchPhrases(SourceInfo sourceInfo);

}
