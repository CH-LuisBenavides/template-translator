import React, {useState} from 'react';
import {Button, TextArea} from "@vaadin/react-components";
import {useSignal} from "@vaadin/hilla-react-signals";
import {TranslatorResource} from "Frontend/generated/endpoints";
import DemoValues
    from "Frontend/generated/com/collectivehealth/templatetranslator/infrastructure/secondary/demo/DemoValues";
import TranslatedGroup
    from "Frontend/generated/com/collectivehealth/templatetranslator/domain/Translation/TranslatedGroup";
import TranslationDto
    from "Frontend/generated/com/collectivehealth/templatetranslator/infrastructure/primary/vaadin/TranslationDto";

const TwoLists = ({ori, t, index}: { ori: string | undefined, t: TranslatedGroup, index: number }) => (
    <>
        <span>{ori}</span>
        <span>{t.translated?.[index] || ""}</span>
    </>

);

const Index = () => {

    const text = useSignal('');
    const [translations, setTranslations] = useState<TranslationDto[]>([]);
    const [translated, setTranslated] = useState<TranslatedGroup[]>([]);


    const generateTemplates = async () => {

        const values: DemoValues = {values: text.value.split("\n")}
        const fetchedGroups = await TranslatorResource.getTemplates(values);


        const filtered = fetchedGroups?.filter(x => !!x) || [];

        setTranslations(filtered);
    }


    const translate = async () => {

        const values = await TranslatorResource.translations(translations);

        setTranslated(values?.filter(x => !!x) || []);

    }


    const displayGroups = () => {

        if (!translations || translations?.length === 0) return (
            <p>please add text to find the pattern</p>
        )

        return <>{
            translations
                .map((t, i) => (
                    <div key={i} style={{display: 'grid', gridTemplateColumns: '1fr', gap: '1rem', marginTop: "1rem"}}>
                        <b>{t?.group?.template}</b>
                        <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '.5rem'}}>
                            {t.group?.phrases?.map(p => <span key={Math.random()}>{p}</span>)}
                        </div>

                        <TextArea value={t.translatedTemplate} onValueChanged={(event) => {

                            const updated = translations.map(g => {
                                if (g.group?.template === t.group?.template) {
                                    g.translatedTemplate = event.detail.value;
                                    return {group: g.group, translatedTemplate: event.detail.value};
                                }
                                return g;
                            });

                            setTranslations([...updated]);
                        }}
                                  style={{width: '100%'}}
                        >
                        </TextArea>
                    </div>))
        }
            <Button onClick={translate}>Translate</Button>
        </>

    }

    const displayTranslations = () => {
        if (!translated || translated.length === 0) return;


        return translated.map((t, i) => (<div style={{display: 'grid', gridTemplateColumns:"1fr 1fr", gap:"1rem"}} key={i}>

            <b>Orginal</b> <b>Translated</b>

            {t.original?.map((ori, index)=>{
                return <TwoLists ori={ori} index={index} t={t} key={Math.random()}/>
            })}

        </div>))


    }


    return (

        <div style={{display: 'flex', flexDirection: 'column', width: '80dvw', minWidth: "60dvw"}}>
            <TextArea
                style={{width: '100%'}}
                label="Text to translate"
                value={text.value}
                onValueChanged={(event) => {
                    text.value = event.detail.value;
                }}
            />
            <Button onClick={generateTemplates}>Create Template</Button>

            {displayGroups()}
            {displayTranslations()}
        </div>

    );
};

export default Index;