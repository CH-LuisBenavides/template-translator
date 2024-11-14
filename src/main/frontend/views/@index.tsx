import React, {useState} from 'react';
import {AppLayout, Button, Details, TextArea, VerticalLayout} from "@vaadin/react-components";
import {useSignal} from "@vaadin/hilla-react-signals";
import {TranslatorResource} from "Frontend/generated/endpoints";
import DemoValues
    from "Frontend/generated/com/collectivehealth/templatetranslator/infrastructure/secondary/demo/DemoValues";
import GroupDto
    from "Frontend/generated/com/collectivehealth/templatetranslator/infrastructure/primary/vaadin/GroupDto";
import Translation from "Frontend/generated/com/collectivehealth/templatetranslator/domain/Translation";
import TranslatedGroup
    from "Frontend/generated/com/collectivehealth/templatetranslator/domain/Translation/TranslatedGroup";

const TwoLists = ({ori, t, index}: { ori: string | undefined, t: TranslatedGroup, index: number }) => (
    <>
        <span>{ori}</span>
        <span>{t.translated?.[index] || ""}</span>
    </>

);

const Index = () => {

    const text = useSignal('');
    const translations = useSignal<string[]>([]);
    const [groups, setGroups] = useState<GroupDto[]>([]);
    const [translated, setTranslated] = useState<TranslatedGroup[]>([]);


    const generateTemplates = async () => {

        const values: DemoValues = {values: text.value.split("\n")}
        const fetchedGroups = await TranslatorResource.getTemplates(values);


        const filtered = fetchedGroups?.filter(x => !!x) || [];

        setGroups(filtered);


    }


    const translate = async () => {

        const templated = groups.map((group, i) => {
            const t: Translation = {group, translatedTemplate: translations.value[i]};
            return t;
        });

        const values = await TranslatorResource.translations(templated);

        setTranslated(values?.filter(x => !!x) || []);


    }


    const displayGroups = () => {

        if (!groups || groups?.length === 0) return (
            <p>please add text to find the pattern</p>
        )

        return <>{
            groups
                .map((group, i) => (
                    <div key={i} style={{display: 'grid', gridTemplateColumns: '1fr', gap: '1rem', marginTop: "1rem"}}>
                        <b>{group.template}</b>
                        <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '.5rem'}}>
                            {group.phrases?.map(p => <span key={Math.random()}>{p}</span>)}

                        </div>

                        <TextArea value={translations.value[i]} onValueChanged={(event) => {
                            translations.value[i] = event.detail.value;
                        }}
                                  style={{width: '100%'}}
                        >
                        </TextArea>
                    </div>))
        }
            <Button onClick={translate}>Translate</Button>
        </>

    }

    const displayTranlations = () => {
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


            {displayTranlations()}
        </div>

    );
};

export default Index;